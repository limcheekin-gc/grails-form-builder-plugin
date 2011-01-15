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
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import grails.converters.JSON

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
abstract class Widget {
	static final String EMPTY_STRING = ""
	
	String getTemplateText(Field field, Integer index, Locale locale, Boolean readOnly = false, Boolean forBuilder = false) {
		FastStringWriter out = new FastStringWriter()
		Object fieldSettings = JSON.parse(field.settings)
		out << '<div class="ctrlHolder ' 
		out << getDivClasses(fieldSettings, locale)
		out << '" rel="'
		out << index
		out << '">'
		String templateText 
		if (readOnly) {
		  templateText = getWidgetReadOnlyTemplateText(field.name, fieldSettings, locale, forBuilder)
		  templateText = templateText?:getWidgetTemplateText(field.name, fieldSettings, locale, forBuilder)
		} else {  
			templateText = getWidgetTemplateText(field.name, fieldSettings, locale, forBuilder)
		}
		out << templateText
		if (!readOnly) {
			out << '<a class="ui-corner-all closeButton" href="#"><span class="ui-icon ui-icon-close">delete this widget</span></a>'
		}
		if (forBuilder) {
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
	
	abstract String getWidgetTemplateText(String fieldName, Object fieldSettings, 
	                                      Locale locale, Boolean forBuilder)
	abstract String getWidgetReadOnlyTemplateText(String fieldName, Object fieldSettings,
		                                            Locale locale, Boolean forBuilder)
	String getDivClasses(Object fieldSettings, Locale locale) { return EMPTY_STRING	}
}
