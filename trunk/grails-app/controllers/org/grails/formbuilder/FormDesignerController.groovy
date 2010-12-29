package org.grails.formbuilder

import java.util.Map;
import org.codehaus.groovy.grails.web.pages.FastStringWriter

class FormDesignerController {
	  static allowedMethods = [preview: "POST"]
	  def formTemplateService
	  def groovyPagesTemplateEngine
	  
    def create = { }
	
	  def preview = { 
		    def elements = params.findAll {!it.key.startsWith('properties') &&
			  it.key != 'controller' && it.key != 'action'}
		     println "preview is executing..."
			   params.each { k, v ->
			     println "$k = $v"
		          }
			  String templateSource = formTemplateService.getTemplateSource(elements, params)
			  println "templateSource = \n $templateSource"
			  def output = new FastStringWriter()
			  groovyPagesTemplateEngine.createTemplate(templateSource, 'templateSource').make([:]).writeTo(output)
			  render output.toString()
		}
	  
	  def save = {
		     println "save is executing..."
			   params.each { k, v ->
			     println "$k = $v"
		          }
		  def elements = params.findAll {!it.key.startsWith('properties') &&
			 							  it.key != 'controller' && it.key != 'action'}
		  String templateSource = g.render(template: "templateSource", model : [elements:elements])?.toString()
			render templateSource
		}
	  

}
