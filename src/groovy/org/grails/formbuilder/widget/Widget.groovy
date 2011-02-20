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

import org.grails.formbuilder.Field
import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import grails.converters.JSON

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
abstract class Widget {
	
	String getTemplateText(Field field, Integer index, Locale locale, Boolean readOnly = false, FormDesignerView formDesignerView = null, Object settings = null) {
		FastStringWriter out = new FastStringWriter()
		if (!settings) {
		   settings = JSON.parse(field.settings)
		}
		out << '<div class="ctrlHolder ' 
		out << getFieldClasses(settings, locale)
		out << '" rel="'
		out << index
		out << '" style="'
		out << getFieldStyles(settings, locale)
		out << '">'
		String templateText 
		if (readOnly) {
		  templateText = getWidgetReadOnlyTemplateText(field.name, settings, locale, formDesignerView)
		  templateText = templateText?:getWidgetTemplateText(field.name, settings, locale, formDesignerView)
		} else {  
			templateText = getWidgetTemplateText(field.name, settings, locale, formDesignerView)
		}
		out << templateText
		
		if (formDesignerView) {
			if (formDesignerView != FormDesignerView.SHOW) {
				out << '<a class="ui-corner-all closeButton" href="#"><span class="ui-icon ui-icon-close">delete this widget</span></a>'
			}
			out << """\
				 <div class="fieldProperties">
					  [@g.hiddenField name="fields[$index].id" value="${field.id}" /]
					  [@g.hiddenField name="fields[$index].name" value="${field.name}" /]
					  [@g.hiddenField name="fields[$index].type" value="${field.type}" /]
					  [@g.hiddenField name="fields[$index].settings" value="${field.settings.encodeAsJavaScript()}" /]
					  [@g.hiddenField name="fields[$index].sequence" value="${field.sequence}" /]
					  [@g.hiddenField name="fields[$index].status" /]
				 </div>
			  """
		}
		out << '</div>'
		return out.toString()
	}
	
	String getConstraints(Field field, Object settings) {
		FastStringWriter out = new FastStringWriter()
		out << "${field.name} "
		if (settings.required) {
			out << "nullable:false, blank:false"
		} else {
		  out << "nullable:true"
		}
		String fieldConstraints = getFieldConstraints(settings)
		if (fieldConstraints != FormBuilderConstants.EMPTY_STRING) {
			out << ", ${fieldConstraints}"
		}
		return out.toString()
	}
	
	abstract String getWidgetTemplateText(String name, Object settings, 
	                                      Locale locale, FormDesignerView formDesignerView)
	abstract String getWidgetReadOnlyTemplateText(String name, Object settings,
		                                            Locale locale, FormDesignerView formDesignerView)
	String getFieldClasses(Object settings, Locale locale) { return FormBuilderConstants.EMPTY_STRING	}
	String getFieldStyles(Object settings, Locale locale) { return FormBuilderConstants.EMPTY_STRING	}
	String getFieldConstraints(Object settings) { return FormBuilderConstants.EMPTY_STRING	}
	Object getFieldValue(Object settings, Locale locale) { 
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		return settings."${language}".value	
	}
}
