package org.grails.formbuilder

import org.grails.formbuilder.widget.Widget
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import grails.converters.JSON

class FormTemplateService {
	def grailsApplication
	static transactional = false
	static final String WIDGET_PACKAGE = "org.grails.formbuilder.widget"
	static final String LAYOUT = "formDesigner"
	static final String BUILDER_PANEL = """\
  <div id="builderPanel" style="\${flash.builderPanelStyles}">
    <div class="formHeading">\${flash.formHeading}</div>
	  <fieldset>
	    <div id="emptyBuilderPanel">Start adding some fields from the menu on the left.</div>
      @FIELDS
    </fieldset>
  </div>		
	"""
	static final String BUILDER_PALETTE = """\
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
					<legend></legend>
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
					<div class="general"></div>								
				</div>
			</div>     
     </div>
  </div>
	  """
	static final String FB_CREATE_VIEW = """\
[#ftl/]
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${LAYOUT}" />
        [#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
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
           <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
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
					${BUILDER_PALETTE}
				  [@g.form action="save" name="builderForm" class="uniForm"]
					  [@g.hiddenField name="name" value="\${formInstance.name}" /]
						[@g.hiddenField name="settings" value="\${formInstance.settings}" /]
          ${BUILDER_PANEL}				  
				  <div class="buttons">
						  [@g.submitButton name="create" class="save" value="\${g.message({'code': 'default.button.create.label', 'default': 'Create'})}" /]
					</div>
					[/@g.form] 
				</div>            
    </body>
</html>
"""
	static final String FB_SHOW_VIEW = """\
[#ftl/]
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${LAYOUT}" />
         [#assign entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
        <title>[@g.message code="default.show.label" args=[entityName] /]</title>
        <script type="text/javascript">
        \$(function() {
        	 \$('#container').formbuilder({tabDisabled: [0], tabSelected: 2, readOnly: true});
           \$('div.buttons').children().button();
           \$('#id').removeAttr('disabled');
        	    });
        </script>    
    </head>
    <body>
        <content tag="nav">
           <div class="title"><h1>[@g.message code="default.show.label" args=[entityName] /]</h1></div>
           <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
           <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>  
           <span class="menuButton">[@g.link class="create" action="create"][@g.message code="default.new.label" args=[entityName] /][/@g.link]</span>  
        </content>
				<div id="container">
				  <div id="header">
		        [#if flash.message?exists]
		        <div class="message">\${flash.message}</div>
		        [/#if]			  
				  </div>
					${BUILDER_PALETTE}
				  [@g.form name="builderForm" class="uniForm"]
						[@g.hiddenField name="id" value="\${formInstance.id}" /]
						[@g.hiddenField name="name" value="\${formInstance.name}" /]
					  [@g.hiddenField name="settings" value="\${formInstance.settings}" /]						
					${BUILDER_PANEL}				  
				  <div class="buttons">
				  [@g.actionSubmit class="edit" value="\${g.message({'code': 'default.button.edit.label', 'default': 'Edit'})}" /]
				  [@g.actionSubmit class="delete" value="\${g.message({'code': 'default.button.delete.label', 'default': 'Delete'})}" onclick="return confirm('\${g.message({'code': 'default.button.delete.confirm.message', 'default': 'Are you sure?'})}');" /]
					</div>
					[/@g.form]	  
				</div>                    
    </body>
</html>	
		"""
	static final String FB_EDIT_VIEW = """\
		[#ftl/]
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<meta name="layout" content="${LAYOUT}" />
				[#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
				<title>[@g.message code="default.edit.label" args=[entityName] /]</title>
				<script type="text/javascript">
				\$(function() {
					 \$('#container').formbuilder();
					 \$('div.buttons').children().button();
						});
				</script>
			</head>
			<body>
				<content tag="nav">
				   <div class="title"><h1>[@g.message code="default.edit.label" args=[entityName] /]</h1></div>
				   <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
				   <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>
           <span class="menuButton">[@g.link class="create" action="create"][@g.message code="default.new.label" args=[entityName] /][/@g.link]</span>  
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
						  ${BUILDER_PALETTE}
						  [@g.form method="post" name="builderForm" class="uniForm"]
							  [@g.hiddenField name="name" value="\${formInstance.name}" /]
							  [@g.hiddenField name="settings" value="\${formInstance.settings}" /]
							  [@g.hiddenField name="id" value="\${formInstance.id}" /]
							  [@g.hiddenField name="version" value="\${formInstance.version}" /]	
					    ${BUILDER_PANEL}
						  <div class="buttons">
							  [@g.actionSubmit class="save" value="\${g.message({'code': 'default.button.update.label', 'default': 'Update'})}" /]
							  [@g.actionSubmit class="delete" value="\${g.message({'code': 'default.button.delete.label', 'default': 'Delete'})}" onclick="return confirm('\${g.message({'code': 'default.button.delete.confirm.message', 'default': 'Are you sure?'})}');" /]
							</div>
							[/@g.form]
						</div>
			</body>
		</html>
		"""
	String getCreateViewTemplate(def request, def flash, Form form) {
		def settings
		Locale locale = RCU.getLocale(request)
		if (form.settings) {
			settings = JSON.parse(form.settings)
		 }
		setBuilderPanelStyles(flash, settings)
		setFormHeading(flash, settings."${locale.language}")
		return FB_CREATE_VIEW.replace('@FIELDS', 
			              getWidgetsTemplateText(request, form, locale, false, true))
	}
	
	private setBuilderPanelStyles(def flash, def settings) {
		if (settings) {
			flash.builderPanelStyles = "font-family: ${settings.styles.fontFamily}; " + 
			                           "font-size: ${settings.styles.fontSize}px; " +
									               "color: #${settings.styles.color}; " +
												         "background-color: #${settings.styles.backgroundColor}"
		} else {
		  flash.builderPanelStyles = ''
		} 
	}
	
	private setFormHeading(def flash, def settings) {
		if (settings) {
			String style = "font-weight: ${settings.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			               "font-style: ${settings.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
						         "text-decoration: ${settings.fontStyles[2] == 1 ? 'underline' : 'none' };" 					   
			
			flash.formHeading = """<${settings.heading} class="heading ${settings.classes[0]}" style="${style}">""" + 
				                  "${settings.name}</${settings.heading}>"
		} else {
		  flash.formHeading = ''
		}
	}
	String getShowViewTemplate(def request, Form form) {
		Locale locale = RCU.getLocale(request)
		return FB_SHOW_VIEW.replace('@FIELDS',
						  getWidgetsTemplateText(request, form, locale, true, true))
	}
	
	String getEditViewTemplate(def request, Form form) {
		Locale locale = RCU.getLocale(request)
		return FB_EDIT_VIEW.replace('@FIELDS',
						  getWidgetsTemplateText(request, form, locale, false, true))
	}
	
	private String getWidgetsTemplateText(def request, Form form, Locale locale, Boolean readOnly = false, Boolean forBuilder = false) {
		Widget widget
		if (form.fieldsList?.size() > 0) {
			FastStringWriter out = new FastStringWriter()
			form.fieldsList.eachWithIndex { field, i ->
				widget = grailsApplication.classLoader.loadClass("${WIDGET_PACKAGE}.${field.type}").newInstance()
				out << widget.getTemplateText(field, i, locale, readOnly, forBuilder)
			}
			return out.toString();
		} else {
			return Widget.EMPTY_STRING
		}
	}
}
