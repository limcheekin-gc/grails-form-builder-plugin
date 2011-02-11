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

import grails.util.GrailsNameUtils
import org.codehaus.groovy.grails.web.pages.FastStringWriter

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class DomainClassGenerationService {
	def grailsApplication
  static transactional = false
  
	String getName(Form form) {
		return "${form.name}.${GrailsNameUtils.getClassNameRepresentation(form.name)}"
    }
  
	String getSource(Form form) {
		def out = new FastStringWriter()
		def persistableFields = grailsApplication.config.formBuilder.persistableFields
		def widget, defaultConstraints
		
		out << "package ${form.name}\n"
		out << "class ${GrailsNameUtils.getClassNameRepresentation(form.name)} {\n"
		form.fieldsList.each { field ->
			if (persistableFields.contains(field.type)) {
				out << """${grailsApplication.config.formBuilder."${field.type}".type} ${field.name}\n"""
			}
		}
	  out << "Date dateCreated\nDate lastUpdated\nstatic constraints = {\n"
	  form.fieldsList.each { field ->
		 if (persistableFields.contains(field.type)) {
				widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
				out << widget.getConstraints(field)
				defaultConstraints = grailsApplication.config.formBuilder."${field.type}".defaultConstraints
				if (defaultConstraints) {
					out << ", ${defaultConstraints}"
				}	
				out << "\n"
		   }
	   }
	  out << "dateCreated blank:false\nlastUpdated nullable:true\n}\n" // end constraints
		out << "}" // end class 
    }
}
