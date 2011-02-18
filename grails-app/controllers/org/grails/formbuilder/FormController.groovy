/* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.grails.formbuilder

import org.codehaus.groovy.grails.web.pages.FastStringWriter
import freemarker.template.Template

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class FormController {
	  def freemarkerConfig
	  def formTemplateService
	  def domainClassService
	  
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
				if (!params.sort) {
				    params.order = "desc" 
					  params.sort = "dateCreated"
				}
        [formInstanceList: Form.list(params), formInstanceTotal: Form.count()]
    }

    def create = {
        def formInstance = new Form()
        formInstance.properties = params
		    flash.formCounter = Form.executeQuery('select max(f.id) from Form f')[0]
			  flash.formCounter = !flash.formCounter ? 1 : flash.formCounter + 1
		    formInstance.name = "form${flash.formCounter}"
			  formInstance.settings = ""
		    renderView("create", formInstance, 
				           formTemplateService.getCreateViewTemplate(request, flash, formInstance))
      }
	 
	 private renderView(name, formInstance, templateText) {
		 // println "$name:\n$templateText"
		 FastStringWriter out = new FastStringWriter()
		 new Template(name,
			 new StringReader(templateText),
			  freemarkerConfig.configuration)
				.process([formInstance: formInstance, flash:flash], out)
			   render out.toString()
	   }

	 	 
    def save = {
        def formInstance = new Form(params)
		    def fieldsToBeDeleted = formInstance.fieldsList.findAll { !it || it.status == FieldStatus.D }
			  if (fieldsToBeDeleted) {
			      formInstance.fieldsList.removeAll(fieldsToBeDeleted)
			    }
			  formInstance.domainClass = domainClassService.getDomainClass(formInstance)
			  formInstance.persistableFieldsCount = formTemplateService.getPersistableFieldsCount(formInstance.fieldsList) 
			  // domainClassService.registerDomainClass formInstance.domainClass.source
		if (formInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'form.label', default: 'Form'), formInstance.id])}"
        redirect(action: "show", id: formInstance.id)
        }
        else {
			  formInstance.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
		    renderView("create", formInstance, 
				           formTemplateService.getCreateViewTemplate(request, flash, formInstance))
        }
    }
	  
    def show = {
        def formInstance = Form.get(params.id)
		
        if (!formInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
            redirect(action: "list")
        }
        else {
            // TODO: workaround for default sort order not working
			      formInstance.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
			      renderView("show", formInstance, 
				           formTemplateService.getShowViewTemplate(request, flash, formInstance))
        }
    }

    def edit = {
        def formInstance = Form.get(params.id)
        if (!formInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
            redirect(action: "list")
        }
        else {
            renderView("edit", formInstance, 
				           formTemplateService.getEditViewTemplate(request, flash, formInstance))
        }
    }

    def update = {
        def formInstance = Form.get(params.id)
        if (formInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (formInstance.version > version) {
                    formInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'form.label', default: 'Form')] as Object[], "Another user has updated this Form while you were editing")
                    render(view: "edit", model: [formInstance: formInstance])
                    return
                }
            }
			      def oldPersistentFields = formInstance.fieldsList.findAll { field ->
						  field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1
					    }
				  
            formInstance.properties = params
						def fieldsToBeDeleted = formInstance.fieldsList.findAll { !it || it.status == FieldStatus.D }
						if (fieldsToBeDeleted) {
							formInstance.fieldsList.removeAll(fieldsToBeDeleted)
						  }
            updateDomainClassSource(formInstance, oldPersistentFields)
            if (!formInstance.hasErrors() && formInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'form.label', default: 'Form'), formInstance.id])}"
                redirect(action: "show", id: formInstance.id)
            }
            else {
						formInstance.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
						renderView("edit", formInstance, 
						           formTemplateService.getEditViewTemplate(request, flash, formInstance))
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
            redirect(action: "list")
        }
    }

  def updateDomainClassSource(Form formInstance,  def oldPersistentFields) {
	  Integer newPersistableFieldsCount = formTemplateService.getPersistableFieldsCount(formInstance.fieldsList)
	  // println "newPersistableFieldsCount = ${newPersistableFieldsCount}, formInstance.persistableFieldsCount = ${formInstance.persistableFieldsCount}"
	  if (newPersistableFieldsCount != formInstance.persistableFieldsCount) {
		  formInstance.persistableFieldsCount = newPersistableFieldsCount
		  formInstance.domainClass.source = domainClassService.getSource(formInstance)
		  formInstance.domainClassSourceUpdated = true
	  } else { // formInstance.isDirty('fieldsList') always return false
		  def newPersistentFields = formInstance.fieldsList.findAll { field ->
			  field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1
		    }
		  log.debug "oldPersistentFields*.name:\n${oldPersistentFields*.name}"
		  log.debug "newPersistentFields*.name:\n${newPersistentFields*.name}"
		  if (!newPersistentFields.containsAll(oldPersistentFields)) {
			  log.debug "!newPersistentFields.containsAll(oldPersistentFields)"
			  formInstance.domainClass.source = domainClassService.getSource(formInstance)
			  formInstance.domainClassSourceUpdated = true
		  }
	  }
    }
    def delete = {
        def formInstance = Form.get(params.id)
        if (formInstance) {
            try {
                formInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), params.id])}"
            redirect(action: "list")
        }
    }
}
