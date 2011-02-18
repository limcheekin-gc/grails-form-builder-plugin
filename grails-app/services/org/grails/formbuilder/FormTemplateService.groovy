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
class FormTemplateService {
	def grailsApplication
	static transactional = false
	
	String getCreateViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FB_CREATE_VIEW, 
			request, flash, form, FormDesignerView.CREATE)
	}
	
	String getViewTemplateSource(String viewTemplate, def request, def flash, Form form, FormDesignerView formDesignerView) {
		def settings
		Locale locale = RCU.getLocale(request)
		if (form.settings) {
			settings = JSON.parse(form.settings)
		 }
		flash.language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		setBuilderPanelStyles(flash, settings, locale)
		setFormHeading(flash, settings?."${flash.language}")
		return viewTemplate.replace('@FIELDS', 
			getWidgetsTemplateText(form, locale, formDesignerView))
	}
	
	private setBuilderPanelStyles(def flash, def settings, Locale locale) {
		if (settings) {
			flash.builderPanelStyles = "font-family: ${settings."${flash.language}".styles.fontFamily}; " + 
			                           "font-size: ${settings."${flash.language}".styles.fontSize}px; " +
									               "color: #${settings.styles.color}; " +
												         "background-color: #${settings.styles.backgroundColor}"
		} else {
		  flash.builderPanelStyles = FormBuilderConstants.EMPTY_STRING
		} 
	}
	
	private setFormHeading(def flash, def settings) {
		if (settings) {
			String style = "font-weight: ${settings.styles.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			               "font-style: ${settings.styles.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
						         "text-decoration: ${settings.styles.fontStyles[2] == 1 ? 'underline' : 'none' };" 					   
			
			flash.formHeading = """<${settings.heading} class="heading" style="${style}">""" + 
				                  "${settings.name}</${settings.heading}>"
		  flash.formHeadingHorizontalAlign = " ${settings.classes[0]}"
		} else {
		  flash.formHeading = FormBuilderConstants.EMPTY_STRING
		  flash.formHeadingHorizontalAlign = FormBuilderConstants.EMPTY_STRING
		}
	}
	
	String getShowViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FB_SHOW_VIEW,
			request, flash, form, FormDesignerView.SHOW)
	}
	
	String getEditViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FB_EDIT_VIEW,
			request, flash, form, FormDesignerView.EDIT)
	}
	
	def getPersistableFieldsCount(List fields) {
		Integer persistableFieldsCount = 0
		fields?.each { field ->
	    if (field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1) {
			  persistableFieldsCount++
			 }
		}
		return persistableFieldsCount
	}
	
	private String getWidgetsTemplateText(Form form, Locale locale, FormDesignerView formDesignerView) {
		Widget widget
		if (form.fieldsList?.size() > 0) {
			FastStringWriter out = new FastStringWriter()
			form.fieldsList.eachWithIndex { field, i ->
				widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
				out << widget.getTemplateText(field, i, locale, false, formDesignerView)
			}
			return out.toString();
		} else {
			return FormBuilderConstants.EMPTY_STRING
		}
	}
}
