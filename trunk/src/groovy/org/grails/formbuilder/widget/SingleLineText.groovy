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
package org.grails.formbuilder.widget

import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class SingleLineText extends Widget {
 String getFieldStyles(Object settings, Locale locale) {
	 String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
	 String fontFamily = settings."${language}".styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${settings."${language}".styles.fontFamily}; "
	 String labelColor = settings.styles.label.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.label.color}; "
	 String labelBackgroundColor = settings.styles.label.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.label.backgroundColor}; "
	 return "${fontFamily}${labelColor}${labelBackgroundColor}"
  }
 
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView) {
    return widgetTemplateText(name, settings, locale, formDesignerView, false)
   }
								
 private String widgetTemplateText(String name, Object settings, 
	                            Locale locale, FormDesignerView formDesignerView, boolean readOnly) {
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		def styles = settings."${language}".styles
		String fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal'
    String fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal'
    String textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none'
	  String fontFamily = styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${styles.fontFamily}; "
	  String fontSize = styles.fontSize == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-size: ${styles.fontSize}px; "  
	  String valueColor = settings.styles.value.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.value.color}; "
	  String valueBackgroundColor = settings.styles.value.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.value.backgroundColor}; "
	  String descriptionColor = settings.styles.description.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.description.color}; "
	  String descriptionBackgroundColor = settings.styles.description.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.description.backgroundColor}; "
    String textField
	  String value = formDesignerView ? settings."${language}".value : "\${domainInstance.${name}}" 
	  if (!readOnly) {
		  textField = """\
		  [@g.textField name="${name}" class="textInput" value="${value}" 
		    style="${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		    text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}" /]		  
		  """
	  } else {
		  textField = """\
		  <span class="textOutput" style="${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  \${domainInstance.${name}}
		  </span>"""
	  }
	  return """<div><label for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}">
		  <em>${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em>
		  <span style="text-decoration: ${textDecoration};">${settings."${language}".label}</span></label>
      ${textField}
	    <p class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description}</p></div>"""
  }
								
 String getFieldConstraints(Object settings) {
	 if (settings.restriction == 'no') {
		 return FormBuilderConstants.EMPTY_STRING
	 } else {
	   return "${settings.restriction}:true"
	 }
 }
 								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView) {														
    return widgetTemplateText(name, settings, locale, formDesignerView, true)
  }
}
