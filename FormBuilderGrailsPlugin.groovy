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
import org.grails.formbuilder.DomainClass
/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class FormBuilderGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [
		  jquery:'1.4.2.7',
		  jqueryUi:'1.8.6',
      dynamicDomainClass: '0.2.1 > *',
			jqueryJson:'2.2 > *',
			freemarkerTags:'0.5.8 > *',
			uniForm:'1.5 > *',
			jqueryDatatables:'1.7.5 > *',
			jqueryValidationUi:'1.2 > *',
			langSelector:'0.3 > *',
			quartz:'0.4.2 > *',
			jqueryFormBuilder:'0.1'
			]
	
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
    def title = "Grails Form Builder Plugin - Create Online Forms without Coding"
    def description = '''\
 The Grails Form Builder Plugin supports user to create online forms in web browser 
 without any programming knowledge.
 
 * Project Site and Documentation: http://code.google.com/p/grails-form-builder-plugin/
 * Support: http://code.google.com/p/grails-form-builder-plugin/issues/list
 * Discussion Forum: http://groups.google.com/group/grails-form-builder-plugin
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/form-builder"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
      if (DomainClass.count()) {
		    DomainClass.executeUpdate('update DomainClass d set d.updated=false')
				DomainClass.list().each { domainClass ->
					applicationContext.domainClassService.registerDomainClass domainClass.source
				}
		}
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
