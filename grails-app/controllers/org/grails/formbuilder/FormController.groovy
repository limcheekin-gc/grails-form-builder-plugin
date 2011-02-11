package org.grails.formbuilder

import org.codehaus.groovy.grails.web.pages.FastStringWriter
import freemarker.template.Template

class FormController {
	  def freemarkerConfig
	  def formTemplateService
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
		
		    formInstance.fieldsList.each { field ->
				  println "field.name = ${field.name}, field.sequence = ${field.sequence}"
		        }
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
            formInstance.properties = params
						def fieldsToBeDeleted = formInstance.fieldsList.findAll { !it || it.status == FieldStatus.D }
						if (fieldsToBeDeleted) {
							formInstance.fieldsList.removeAll(fieldsToBeDeleted)
						  }
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
