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
import grails.converters.JSON
/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class DomainClassService {
	def grailsApplication
	def dynamicDomainService
  static transactional = false
  
	String getName(Form form) {
		return "${form.name}.${GrailsNameUtils.getClassNameRepresentation(form.name)}"
    }
  
	String getSource(Form form) {
		def out = new FastStringWriter()
		def widget, defaultConstraints
		Object settings
		def constraintsStrings = new FastStringWriter()
		
		out << "package ${form.name}\n"
		out << "class ${GrailsNameUtils.getClassNameRepresentation(form.name)} {\n"
		form.fieldsList.each { field ->
			settings = JSON.parse(field.settings)
			if (settings._persistable) {
				out << """${grailsApplication.config.formBuilder."${field.type}".type} ${field.name}\n"""
				widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
				constraintsStrings << widget.getConstraints(field, settings)
				defaultConstraints = grailsApplication.config.formBuilder."${field.type}".defaultConstraints
				defaultConstraints?.each { k, v ->
					constraintsStrings << ", ${k}:${v}"
				}
				constraintsStrings << FormBuilderConstants.NEW_LINE
			}
		}
	  out << "Date dateCreated\nDate lastUpdated\nstatic constraints = {\n"
	  out << constraintsStrings.toString()
	  out << "}\n" // end constraints
		out << "}" // end class 
    }
	
	DomainClass getDomainClass(Form form) {
		return new DomainClass(
			         name: getName(form),
			         source: getSource(form)
			          )
	}
	
	def registerDomainClass(String source) {
		println "domain class source: \n${source}"
	  source.trim().split("package").each {
			if (it) {
				 dynamicDomainService.registerDomainClass "package$it"
			}
		 }
	  dynamicDomainService.updateSessionFactory grailsApplication.mainContext
	 }
	
	def reloadUpdatedDomainClasses(Date lastExecuted) {
		log.debug "reloadDomainClasses() executing..."
		if (DomainClass.countByUpdatedAndLastUpdatedGreaterThan(true, lastExecuted)) {
			def updatedDomainClasses = DomainClass.findAllByUpdatedAndLastUpdatedGreaterThan(true, lastExecuted)
			// From: http://www.intelligrape.com/blog/2010/09/29/gorm-batch-deletes-made-easy-with-load-method/
			// updatedDomainClasses*.discard() // detach all objects from session
			updatedDomainClasses.each { domainClass ->
				registerDomainClass domainClass.source
			}
		}
	  log.debug "reloadDomainClasses() executed"
	 }
	
	def setDefaultValue(def domainInstance, def constrainedProperty) {
		def constraintNames = getConstraintNames(constrainedProperty)
		def value
		constraintNames.eachWithIndex { constraintName, i ->
			println "$i) $constraintName"
			switch (constraintName) {
				case "nullable":
				case "blank":
				  // required
				  if (!constrainedProperty.isNullable() && !constrainedProperty.isBlank()) {
					  if (constrainedProperty.propertyType == String) {
						  value = FormBuilderConstants.DEFAULT_STRING
					  } else if (propertyType == Date) {
						  value = FormBuilderConstants.DEFAULT_DATE
					  } else if (propertyType.superclass == Number) {
						  value = FormBuilderConstants.DEFAULT_NUMBER
					  }
				 } else { // nullable:true
				   return
				  }
				break
				case "creditCard":
				if (constrainedProperty.isCreditCard()) {
					value = FormBuilderConstants.DEFAULT_CREDIT_CARD_NO
				}
				break
				case "email":
				if (constrainedProperty.isEmail()) {
					value = FormBuilderConstants.DEFAULT_EMAIL
				}
				break
				case "url":
				if (constrainedProperty.isUrl()) {
					value = FormBuilderConstants.DEFAULT_URL
				}
				break
				case "inList":
				  value = constrainedProperty.inList[0]
				break
				case "matches":
				break
				case "max":
				break
				case "maxSize":
				break
				case "min":
				  value = constrainedProperty.min
				break
				case "minSize":
				if (constrainedProperty.propertyType == String) {
					value = FormBuilderConstants.DEFAULT_STRING * constrainedProperty.minSize
				}
				break
				case "notEqual":
				break
				case "range":
				  value = constrainedProperty.range.from
				break
				case "size":
				if (constrainedProperty.propertyType == String) {
				   value = FormBuilderConstants.DEFAULT_STRING * constrainedProperty.size.from
				}
				break
				case "unique":
				if (constrainedProperty.propertyType == String) {
					value = UUID.randomUUID()
				 }
				break
				case "validator":
				break
				default:
				println "custom constraint: ${constraintName}"
			}
		}
		domainInstance."${constrainedProperty.propertyName}" = value
		println "DomainClassService.setDefaultValue: ${constrainedProperty.propertyName} = ${value}"
	 }

	private List getConstraintNames(def constrainedProperty) {
		def constraintNames = constrainedProperty.appliedConstraints.collect { return it.name }
		if (constraintNames.contains("blank") && constraintNames.contains("nullable")) {
			constraintNames.remove("nullable") // blank constraint take precedence
		}
		return constraintNames
	}
}
