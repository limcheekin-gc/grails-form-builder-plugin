package org.grails.formbuilder

class FormDesignerController {
	  static allowedMethods = [preview: "POST"]
    def create = { }
	
	  def preview = { 

		     println "preview is executing..."
			   params.each { k, v ->
			     println "$k = $v"
		          }

		}
	  
	  def save = {
		     println "preview is executing..."
			   params.each { k, v ->
			     println "$k = $v"
		          }
		  def elements = params.findAll {!it.key.startsWith('properties')&&
			 							  it.key != 'controller' && it.key != 'action'}
		  String templateSource = g.render(template: "templateSource", model : [elements:elements])?.toString()
			render templateSource
		}
	  

}
