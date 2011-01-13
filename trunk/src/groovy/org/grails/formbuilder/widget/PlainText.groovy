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

import java.util.Locale;

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class PlainText extends Widget {
 String getDivClasses(Object fieldSettings, Locale locale) {
		return fieldSettings."${locale.language}".classes[1]
  }
 
 String getWidgetTemplateText(String fieldName, Object fieldSettings,  
	                            Locale locale, Boolean forBuilder) {
		String text = fieldSettings."${locale.language}".text
		String styleClass = fieldSettings."${locale.language}".classes[0]
		return """<div class="plainText ${styleClass}">${text}</div>"""
   }
								
 String getWidgetReadOnlyTemplateText(String fieldName, Object fieldSettings,
									                    Locale locale, Boolean forBuilder) {
														
    return null
  }
}
