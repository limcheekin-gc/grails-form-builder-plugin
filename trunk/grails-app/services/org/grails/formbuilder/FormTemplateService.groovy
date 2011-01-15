package org.grails.formbuilder

import org.grails.formbuilder.widget.Widget
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.springframework.web.servlet.support.RequestContextUtils as RCU

class FormTemplateService {
	def grailsApplication
	static transactional = false
	static final String WIDGET_PACKAGE = "org.grails.formbuilder.widget"
	static final String LAYOUT = "formbuilder"
	static final String FB_CREATE_EDIT_VIEW = """\
[#ftl/]
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${LAYOUT}" />
        [#assign  entityName=g._message({'code': 'form.label', 'default': 'Form'}) /]
        <title>[@g.message code="default.create.label" args=[entityName] /]</title>
        <script type="text/javascript">
        \$(function() {
        	 \$('#container').formbuilder();
        	 \$('div.buttons').children().button();
        	    });
        </script>            
    </head>
    <body>
        <content tag="nav">
           <div class="title"><h1>[@g.message code="default.create.label" args=[entityName] /]</h1></div>
           <span class="menuButton"><a class="home" href="\${g._createLink({'uri': '/'})}">\${g._message({'code':'default.home.label'})}</a></span>
           <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>      
        </content>
				<div id="container">
				  <div id="header">
		        [#if flash.message?exists]
		        <div class="message">\${flash.message}</div>
		        [/#if]
		        [@g.hasErrors bean=formInstance]
		        <div class="errors">
		            [@g.renderErrors bean=formInstance _as="list" /]
		        </div>     
		        [/@g.hasErrors]				  		  
				  </div>
				  <div id="builderPalette">
				     <div class="floatingPanelIdentifier"></div>
				     <div class="floatingPanel">
							<div id="paletteTabs">
								<ul>
									<li><a href="#addField">Add Field</a></li>
									<li><a href="#fieldSettings">Field Settings</a></li>
									<li><a href="#formSettings">Form Settings</a></li>
								</ul>
								<div id="addField">
								  <strong>Standard Fields</strong>
									<div id="standardFields"></div>
									<br />
								  <strong>Fancy Fields</strong>
								  <div id="fancyFields"></div>
								</div>
								<div id="fieldSettings">
									<fieldset class="language">
									<legend>Language: </legend>
									</fieldset>
									<div class="general">
									</div>				
								</div>
								<div id="formSettings">
									<fieldset class="language">
									<legend>
									<label for="language">Language: </label>
									<select id="language">
									  <option value="zh">Chinese</option>
									  <option value="en" selected>English</option>
									</select>
									</legend>
									</fieldset>	
									<div class="general">
										<div class="clear labelOnTop">
											<label for="text">Name (?)</label><br/>
											<input type="text" id="form.name" />
										</div>	
										<div class="clear labelOnTop">
											<label for="text">Description (?)</label><br/>
											<textarea id="form.description"></textarea>
										</div>												
									</div>								
								</div>
							</div>     
				     </div>
				  </div>
				  [@g.form action="save" name="builderForm" class="uniForm"]
				  <div id="builderPanel">
						  <div id="emptyBuilderPanel">Start adding some fields from the menu on the left.</div>
						  <fieldset>
						  	[@g.hiddenField name="name" /]
		            [@g.hiddenField name="description" /]
		            @FIELDS  
						  </fieldset>
				  </div>
				  <div class="buttons">
						  [@g.submitButton name="create" class="save" value="\${g._message({'code': 'default.button.create.label', 'default': 'Create'})}" /]
					</div>
					[/@g.form] 
				</div>            
    </body>
</html>
"""
	
	String getCreateViewTemplate(def request, Form form) {
		return FB_CREATE_EDIT_VIEW.replace('@FIELDS', 
			              getWidgetsTemplateText(request, form))
	}
	
	private String getWidgetsTemplateText(def request, Form form) {
		Widget widget
		if (form.fieldsList?.size() > 0) {
			Locale locale = RCU.getLocale(request)
			FastStringWriter out = new FastStringWriter()
			form.fieldsList.eachWithIndex { field, i ->
				widget = grailsApplication.classLoader.loadClass("${WIDGET_PACKAGE}.${field.type}").newInstance()
				out << widget.getTemplateText(field, i, locale, false, true)
			}
			return out.toString();
		} else {
			return Widget.EMPTY_STRING
		}
	}
}
