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

import grails.test.*

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class DomainClassServiceTests extends GrailsUnitTestCase {
	def domainClassService
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

  void testCreateSimpleDomainClass() {
		Form form = new Form(name: 'bookForm')
		form.fieldsList << new Field(
			name: 'name',
			type: 'SingleLineText',
			settings: '{"_persistable":true,"required":true,"restriction":"alphanumeric"}'
		)
		assertEquals('name', 'bookForm.BookForm', domainClassService.getName(form))
		String source = """\
package bookForm
class BookForm {
String name
Date dateCreated
Date lastUpdated
static constraints = {
name nullable:false, blank:false, alphanumeric:true, maxSize:255
}
}"""
		assertEquals('source', source, domainClassService.getSource(form))
    }
}
