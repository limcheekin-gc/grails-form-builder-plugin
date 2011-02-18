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

import org.codehaus.groovy.grails.web.binding.DataBindingUtils
import grails.converters.JSON
import freemarker.template.Template
import org.codehaus.groovy.grails.web.pages.FastStringWriter

  /**
 * Form Viewer Controller for Dynamic Domain Class.
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class FormViewerController {
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def freemarkerConfig
	def formViewerTemplateService
	def domainClassService
	
	def index = {
		redirect(action: "list", params: params)
	}
	
	def list = {
    Form formInstance = Form.read(params.formId)
		if (formInstance.domainClass.updated) {
		  def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		  formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
		}
	  renderView("list", [formInstance: formInstance, flash: flash], 
		  formViewerTemplateService.getListViewTemplate(request, flash, formInstance))
	}
	
	private renderView(name, model, templateText) {
		// println "$name:\n$templateText"
		FastStringWriter out = new FastStringWriter()
		new Template(name, new StringReader(templateText),
			 freemarkerConfig.configuration).process(model, out)
			  render out.toString()
	}

	def listData = {
		Form form = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(form.domainClass.name)
		def domainInstance = domainClass.newInstance()
		formViewerTemplateService.handleDomainClassSourceUpdated(form, domainClass)
		def filters = []
		def fieldsToRender = form.fieldsList?.findAll { field ->
			field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1
		}
		fieldsToRender.findAll { field ->
		  grailsApplication.config.formBuilder."${field.type}".type == 'String'
		}.each { field ->
		  filters << "d.${field.name} like :filter"
		}
		fieldsToRender = fieldsToRender.collect { it.name }
		fieldsToRender.add(0, 'id')
		def filter = filters.join(" OR ")

		def dataToRender = [:]
		dataToRender.sEcho = params.sEcho
		dataToRender.aaData=[]                // Array of domains.

		dataToRender.iTotalRecords = domainClass.clazz.count()
		dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords

		def queryWithOrder = new StringBuilder("from ${domainClass.name} d")
		if ( params.sSearch ) {
		   queryWithOrder.append(" where (${filter})")
		}
		String query = queryWithOrder.toString()
		queryWithOrder.append(" order by d.${fieldsToRender[params.iSortCol_0 as int]} ${params.sSortDir_0}")
		def dataRows
		String selectQuery = "select ${fieldsToRender.collect { "d.${it}" }.join(',')} ${queryWithOrder.toString()}"
		// println "selectQuery = $selectQuery"
		if ( params.sSearch ) {
		  dataToRender.iTotalDisplayRecords = domainClass.clazz.executeQuery("select count(d.id) ${query}", [filter: "%${params.sSearch}%"])[0]
		  // findAll throwing SQLException after domain class reloaded, so using executeQuery
		  dataRows = domainClass.clazz.executeQuery(selectQuery,
			  [filter: "%${params.sSearch}%"],
			  [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
		} else {
		  dataRows = domainClass.clazz.executeQuery(selectQuery,
			  [max: params.iDisplayLength as int, offset: params.iDisplayStart as int])
		}
		
		dataRows?.each { dataRow ->
			// println "dataRow = ${dataRow}"
		  dataToRender.aaData << dataRow
		}
		render dataToRender as JSON
   }
	
	def create = {
		Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.newInstance()
		formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
		renderView('create', [flash: flash, formInstance: formInstance, 
			domainInstance: domainInstance, domainClass: domainClass, multiPart: false], // multiPart:true if form have upload component 
			formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance)) 
	}
	
	def save = {
		Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.newInstance()   
		// setProperties(domainClass, domainInstance, params)
		DataBindingUtils.bindObjectToDomainInstance(domainClass, domainInstance, params)
		formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
		if (domainInstance.validate()) {
			domainInstance.save(flush: true)
			flash.message = "${message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), domainInstance.id])}"
			redirect(action: "show", id: domainInstance.id, params: [formId: formInstance.id])
		}
		else {
			renderView('create', [flash: flash, formInstance: formInstance,
				domainInstance: domainInstance, domainClass: domainClass, multiPart: false], // multiPart:true if form have upload component
				formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance))
		}
	}
	
	def show = {
    Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.get(params.id)		
		if (!domainInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
			redirect(action: "list", params:[formId:params.formId])
		}
		else {
			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
			renderView('show', [flash: flash, formInstance: formInstance,
				domainInstance: domainInstance, domainClass: domainClass], 
				formViewerTemplateService.getShowViewTemplate(request, flash, formInstance))
		}
	}
	
	def edit = {
		Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.get(params.id)
		if (!domainInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
			redirect(action: "list", params:[formId:params.formId])
		}
		else {
			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
			renderView('edit', [flash: flash, formInstance: formInstance,
				domainInstance: domainInstance, domainClass: domainClass], 
				formViewerTemplateService.getEditViewTemplate(request, flash, formInstance))
		}
	}
	
	def update = {
		Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.get(params.id)
		if (domainInstance) { 
			if (params.version) {
				def version = params.version.toLong()
				if (domainInstance.version > version) {
					domainInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: '${domainClass.propertyName}.label', default: domainClass.name)] as Object[], "Another user has updated this ${domainClass.propertyName} while you were editing")
					renderView('edit', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance, domainClass: domainClass],
						formViewerTemplateService.getEditViewTemplate(request, flash, formInstance))
					return
				}
			}
			DataBindingUtils.bindObjectToDomainInstance(domainClass, domainInstance, params)
			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
			if (!domainInstance.hasErrors() && domainInstance.validate()) {
				domainInstance.save(flush: true)
				flash.message = "${message(code: 'default.updated.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), domainInstance.id])}"
				redirect(action: "show", id: domainInstance.id, params:[formId:params.formId])
			}
			else {
				renderView('edit', [flash: flash, formInstance: formInstance,
					domainInstance: domainInstance, domainClass: domainClass],
					formViewerTemplateService.getEditViewTemplate(request, flash, formInstance))
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
			redirect(action: "list", params:[formId:params.formId])
		}
	}
	
	def delete = {
		Form formInstance = Form.read(params.formId)
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.get(params.id)
		if (domainInstance) {
			try {
				domainInstance.delete(flush: true)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
				redirect(action: "list", params:[formId:params.formId])
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
				redirect(action: "show", id: params.id, params:[formId:params.formId])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), params.id])}"
			redirect(action: "list", params:[formId:params.formId])
		}
	}
}
