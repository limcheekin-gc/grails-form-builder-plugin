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

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class PlainText extends Widget {
 String getFieldClasses(Object settings, Locale locale) {
		return settings."${locale.language}".classes[1]
  }
 
 String getFieldStyles(Object settings, Locale locale) { 
	 return "font-weight: ${settings."${locale.language}".styles.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			    "font-style: ${settings."${locale.language}".styles.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
			    "text-decoration: ${settings."${locale.language}".styles.fontStyles[2] == 1 ? 'underline' : 'none' };" +
				  "font-family: ${settings."${locale.language}".styles.fontFamily}; " +
				  "font-size: ${settings."${locale.language}".styles.fontSize}px; " +
					"color: #${settings.styles.color}; " +
					"background-color: #${settings.styles.backgroundColor}"
  }
 
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView) {
		String text = settings."${locale.language}".text
		String styleClass = settings."${locale.language}".classes[0]
		return """<div class="PlainText ${styleClass}">${text}</div>"""
   }
								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView) {
														
    return null
  }
}
