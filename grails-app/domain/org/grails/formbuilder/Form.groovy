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

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils
/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class Form {
	String name
	String description
	String settings
	String templateSource
	String domainClassCode
	String domainClassFullName
	Integer numberOfColumnInList
	Integer numberOfRowPerPage
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		name blank: false, unique: true
		description nullable:true, blank: true
		settings nullable:false, blank: false
		templateSource nullable:true, blank: true
		domainClassCode nullable:true, blank: true
		domainClassFullName nullable:true, blank: true
		numberOfColumnInList nullable:true, blank: true 
		numberOfRowPerPage nullable:true, blank: true
		fieldsList nullable:false, minSize: 1
		dateCreated blank:false
		lastUpdated nullable:true
	}
	
	List fieldsList = new ArrayList()
	static hasMany = [ fieldsList:Field ]
	
	static mapping = { 
		fieldsList cascade:"all-delete-orphan", sort: "sequence", lazy: false 
	}
	
	// From: http://omarello.com/2010/08/grails-one-to-many-dynamic-forms/
	def getFields() {
		return LazyList.decorate(fieldsList,
		FactoryUtils.instantiateFactory(Field.class))
	}
}
