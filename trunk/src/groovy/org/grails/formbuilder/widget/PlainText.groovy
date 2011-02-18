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
	  String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		return settings."${language}".classes[1]
  }
 
 String getFieldStyles(Object settings, Locale locale) { 
	 String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
	 return "font-weight: ${settings."${language}".styles.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			    "font-style: ${settings."${language}".styles.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
			    "text-decoration: ${settings."${language}".styles.fontStyles[2] == 1 ? 'underline' : 'none' };" +
				  "font-family: ${settings."${language}".styles.fontFamily}; " +
				  "font-size: ${settings."${language}".styles.fontSize}px; " +
					"color: #${settings.styles.color}; " +
					"background-color: #${settings.styles.backgroundColor}"
  }
 
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView) {
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		String text = settings."${language}".text
		String styleClass = settings."${language}".classes[0]
		return """<div class="PlainText ${styleClass}">${text}</div>"""
   }
								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView) {
														
    return null
  }
}
