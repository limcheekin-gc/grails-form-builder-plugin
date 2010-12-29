package org.grails.formbuilder

class FormTemplateService {

    static transactional = false

    def getTemplateSource(Map elements, Map params) {
		  StringBuffer fieldsHtml = new StringBuffer(1000)
		  elements.reverseEach { name, value -> 
			  fieldsHtml.append getFieldHtml(params["properties[$name][type]"], name, 
				                               params["properties[$name][label]"], value, 
											                 params["properties[$name][description]"])
		    }

		  return """
		<html>
		    <head>
		        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		        <meta name="layout" content="main" />
		        <g:set var="entityName" value="\${message(code: 'person.label', default: 'Person')}" />
		        <title><g:message code="default.create.label" args="[entityName]" /></title>
		        <g:javascript library="jquery" plugin="jquery"/>
		        <jqval:resources />
		        <jqvalui:resources />
		        <%--jqvalui:renderValidationScript for="org.grails.jquery.validation.ui.Person" also="homeAddress, workAddress" /--%>	
		        <uf:resources />                	                
		    </head>
		    <body>
		        <div class="nav">
		            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
		            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
		        </div>
		        <div class="body">
		            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
		            <g:if test="\${flash.message}">
		            <div class="message">\${flash.message}</div>
		            </g:if>
		            <g:hasErrors bean="\${domainInstance}">
		            <jqvalui:renderErrors style="margin-bottom:10px">
		                <g:renderErrors bean="\${domainInstance}" as="list" />
		            </jqvalui:renderErrors>
		            </g:hasErrors>
		            <jqvalui:renderErrors style="margin-bottom:10px"/>
		            <g:form action="save" name="form" class="uniForm">
		                <div class="formControls">
		                   ${fieldsHtml}
										</div>
		                <div class="buttons">
		                    <span class="button"><g:submitButton name="create" class="save" value="\${message(code: 'default.button.create.label', default: 'Create')}" /></span>
		                </div>
		            </g:form>
		        </div>
		    </body>
		</html>
		"""		
        }
	
	private String getFieldHtml(String type, String name, String label, String value, String description) {
		String fieldElement

		switch(type)
		{
			case 'text':
				fieldElement = """<g:textArea class="wysiwyg" name="${name}" value="${value}" rows="5" cols="50" />"""
				break
			case 'textarea':
				fieldElement = """<g:textArea name="${name}" value="${value}" rows="5" cols="50" />"""
				break
			case 'textbox':
				fieldElement = """<g:textField name="${name}" maxlength="255" value="${value}" class="textInput" />"""
				break
			case 'dropdown':
				fieldElement = """<g:select name="${name}" from="" value="${value}" noSelection="['':'No Option']"/>"""
				break
			case 'checkbox':
				fieldElement = """<span class="values ${name}"><g:checkBox name="${name}" value="1" /></span>"""
				break
			case 'radio':
				fieldElement = """<span class="values ${name}"><g:radio name="${name}" value="1" /></span>"""
				break
			case 'datetime':
				fieldElement = """<g:textField name="${name}" value="${value}" class="textInput datepicker" />"""
				break
			case 'fileupload':
				fieldElement = """<input type="file" id="${name}" name="${name}" />"""
				break
		}
		return """
	  <div class="ctrlHolder">
	    <label for="${name}"><g:message code="" default="${label}" /></label>
	    ${fieldElement}
	    ${description?"<p class=\"formHint\">$description</p>":""}
	    <g:if test="\${hasErrors(bean: domainInstance, field: '${name}', 'errors')}">
	       <jqvalui:renderError for="${name}" style="left: 380px; margin-top: -15px">
	     	   <g:eachError bean="\${domainInstance}" field="${name}"><g:message error="\${it}" /></g:eachError>
	       </jqvalui:renderError>
	    </g:if>      
	  </div>	
		"""
		}
}
