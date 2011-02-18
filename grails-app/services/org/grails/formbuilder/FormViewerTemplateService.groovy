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

import org.grails.formbuilder.widget.Widget
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import grails.converters.JSON

/**
* Form Viewer Controller for Dynamic Domain Class.
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class FormViewerTemplateService {
	def grailsApplication
	def domainClassService
	static transactional = false
	
	String getCreateViewTemplate(def request, def flash, Form form, Object domainInstance = null) {
		return getViewTemplateSource(FormBuilderConstants.FV_CREATE_VIEW, request, flash, form, false, domainInstance)
	}
	
	String getEditViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FV_EDIT_VIEW, request, flash, form)
	}
	
	private String getViewTemplateSource(String viewTemplate, def request, def flash, Form form, Boolean readOnly = false, Object domainInstance = null) {
		Locale locale = RCU.getLocale(request)
		flash.language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		def settings = JSON.parse(form.settings)
	  setBodyStyles(flash, settings, locale)
	  setFormHeading(flash, settings."${flash.language}")
		return viewTemplate.replace('@FIELDS', 
			getWidgetsTemplateText(form, locale, readOnly, domainInstance))
	}	
	
	private setBodyStyles(def flash, def settings, Locale locale) {
		if (settings) {
			flash.bodyStyles = "font-family: ${settings."${flash.language}".styles.fontFamily}; " + 
			                   "font-size: ${settings."${flash.language}".styles.fontSize}px; " +
									       "color: #${settings.styles.color}; " +
												 "background-color: #${settings.styles.backgroundColor}"
		} else {
		  flash.bodyStyles = FormBuilderConstants.EMPTY_STRING
		} 
	}
	
	private setFormHeading(def flash, def settings) {
		if (settings) {
			String style = "font-weight: ${settings.styles.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			               "font-style: ${settings.styles.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
						         "text-decoration: ${settings.styles.fontStyles[2] == 1 ? 'underline' : 'none' };" 					   
			
			flash.formHeading = """<${settings.heading} class="heading" style="${style}">""" + 
				                  "${settings.name}</${settings.heading}>"
			flash.formName = settings.name
		  flash.formHeadingHorizontalAlign = " ${settings.classes[0]}"
		} else {
		  flash.formHeading = FormBuilderConstants.EMPTY_STRING
		  flash.formHeadingHorizontalAlign = FormBuilderConstants.EMPTY_STRING
		}
	}
	
	String getShowViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FV_SHOW_VIEW, request, flash, form, true)
	}
	
	String getListViewTemplate(def request, def flash, Form form) {
		Locale locale = RCU.getLocale(request)
		def settings
		flash.language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		flash.formName = JSON.parse(form.settings)."${flash.language}".name
		FastStringWriter out = new FastStringWriter()
		out << "<th>Id.</th>"
		form.fieldsList?.each { field ->
			settings = JSON.parse(field.settings)
			if (settings._persistable) {
			  out << "<th>"
			  out << settings."${flash.language}".label
			  out << "</th>"
			}
		}
		return FormBuilderConstants.FV_LIST_VIEW.replaceAll('@FIELDS_HEADER', out.toString())
	}
	
	private String getWidgetsTemplateText(Form form, Locale locale, Boolean readOnly = false, Object domainInstance = null) {
		Widget widget
		Object settings
		FastStringWriter out = new FastStringWriter()
		
		form.fieldsList?.eachWithIndex { field, i ->
			widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
			settings = JSON.parse(field.settings)
			out << widget.getTemplateText(field, i, locale, readOnly, null, settings)
			if (domainInstance && settings._persistable) {
				// initialize default value for create view
			  domainInstance."${field.name}" = widget.getFieldValue(settings, locale)
			}
		}
		return out.toString();
	}
	
	def handleDomainClassSourceUpdated(Form form, def domainClass, def domainInstance = null) {
		if (form.domainClassSourceUpdated) {
			def propertyNames = domainClass.persistentProperties*.name.findAll { !FormBuilderConstants.DOMAIN_CLASS_SYSTEM_FIELDS.contains(it) }
			if (domainInstance && form.persistableFieldsCount < propertyNames.size()) {
				def fieldsListNames = form.fieldsList*.name
				propertyNames.findAll { !fieldsListNames.contains(it) }.each { propertyName ->
					 domainClassService.setDefaultValue(domainInstance, domainClass.constraints[propertyName])
				  }
			} else {
				form.fieldsList = form.fieldsList?.findAll { field ->
					propertyNames.contains(field.name) ||
					field.settings.indexOf(FormBuilderConstants.PERSISTABLE) == -1
				}
			}
		}
	}
}
