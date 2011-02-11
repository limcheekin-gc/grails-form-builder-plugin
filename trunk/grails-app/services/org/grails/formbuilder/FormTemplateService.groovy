package org.grails.formbuilder

import org.grails.formbuilder.widget.Widget
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import grails.converters.JSON

class FormTemplateService {
	def grailsApplication
	static transactional = false
	String getCreateViewTemplate(def request, def flash, Form form) {
		def settings
		Locale locale = RCU.getLocale(request)
		if (form.settings) {
			settings = JSON.parse(form.settings)
		 }
		setBuilderPanelStyles(flash, settings, locale)
		setFormHeading(flash, settings?."${locale.language}")
		return FormBuilderConstants.FB_CREATE_VIEW.replace('@FIELDS',
						  getWidgetsTemplateText(form, locale, FormDesignerView.CREATE))
	}
	
	private setBuilderPanelStyles(def flash, def settings, Locale locale) {
		if (settings) {
			flash.builderPanelStyles = "font-family: ${settings."${locale.language}".styles.fontFamily}; " + 
			                           "font-size: ${settings."${locale.language}".styles.fontSize}px; " +
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
		Locale locale = RCU.getLocale(request)
		def settings = JSON.parse(form.settings)
    setBuilderPanelStyles(flash, settings, locale)
    setFormHeading(flash, settings."${locale.language}")
		return FormBuilderConstants.FB_SHOW_VIEW.replace('@FIELDS',
						  getWidgetsTemplateText(form, locale, FormDesignerView.SHOW))
	}
	
	String getEditViewTemplate(def request, def flash, Form form) {
		Locale locale = RCU.getLocale(request)
		def settings = JSON.parse(form.settings)
		setBuilderPanelStyles(flash, settings, locale)
		setFormHeading(flash, settings."${locale.language}")
		return FormBuilderConstants.FB_EDIT_VIEW.replace('@FIELDS',
						  getWidgetsTemplateText(form, locale, FormDesignerView.EDIT))
	}
	
	private String getWidgetsTemplateText(Form form, Locale locale, FormDesignerView formDesignerView = null) {
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
