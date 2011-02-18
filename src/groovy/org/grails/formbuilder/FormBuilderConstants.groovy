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

import java.util.List;

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class FormBuilderConstants {
	static final String EMPTY_STRING = ""
	static final String DEFAULT_STRING = "A"
	static final Date DEFAULT_DATE = new Date()
	static final Number DEFAULT_NUMBER = 0
	static final String DEFAULT_CREDIT_CARD_NO = "11111111111"
	static final String DEFAULT_EMAIL = "formbuilder@grails.org"
	static final String DEFAULT_URL = "http://www.grails.org"
	static final String NEW_LINE = "\n"
	static final String WIDGET_PACKAGE = "org.grails.formbuilder.widget"
	static final String DESIGNER_LAYOUT = "formDesigner"
	static final String VIEWER_LAYOUT = "formViewer"
	static final String FORM_VIEWER_CONTROLLER = "formViewer"
	static final String PERSISTABLE = "_persistable"
	static final List DOMAIN_CLASS_SYSTEM_FIELDS = ["id", "version", "dateCreated", "lastUpdated"]
	static final String BUILDER_PANEL = """\
  <div id="builderPanel" style="\${flash.builderPanelStyles}">
	<div class="formHeading\${flash.formHeadingHorizontalAlign}">\${flash.formHeading}</div>
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
					  <option value="en">English</option>
					  <option value="zh_CN">简体中文</option>
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
		<meta name="layout" content="${DESIGNER_LAYOUT}" />
		[#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
		<title>[@g.message code="default.create.label" args=[entityName] /]</title>
		<script type="text/javascript">
		\$(function() {
			 \$('#container').formbuilder({formCounter: \${flash.formCounter}, language: '\${flash.language}'});
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
		<meta name="layout" content="${DESIGNER_LAYOUT}" />
		 [#assign entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
		<title>[@g.message code="default.show.label" args=[entityName] /]</title>
		<script type="text/javascript">
		\$(function() {
			 \$('#container').formbuilder({tabDisabled: [0], tabSelected: 2, readOnly: true, language: '\${flash.language}'});
		   \$('div.buttons').children().button();
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
				<meta name="layout" content="${DESIGNER_LAYOUT}" />
				[#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
				<title>[@g.message code="default.edit.label" args=[entityName] /]</title>
				<script type="text/javascript">
				\$(function() {
					 \$('#container').formbuilder({language: '\${flash.language}', disabledNameChange: true});
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
	
  static final String FV_CREATE_VIEW = """\
	[#ftl/]
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<meta name="layout" content="${VIEWER_LAYOUT}" />
			<title>[@g.message code="default.create.label" args=[flash.formName] /]</title>
			<script type="text/javascript">
			\$(function() {
				 \$('div.buttons').children().button();
				});
			</script>
		</head>
		<body>
			<content tag="nav">
			   <div class="title"><h1>[@g.message code="default.create.label" args=[flash.formName] /]</h1></div>
			   <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
			   <span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
			</content>			
			<div class="body" style="\${flash.bodyStyles}">
			  <div class="formHeading\${flash.formHeadingHorizontalAlign}">\${flash.formHeading}</div>
				[#if flash.message?exists]
				  <div class="message">\${flash.message}</div>
				[/#if]
				[@g.hasErrors bean=domainInstance]
				<div class="errors">
					[@g.renderErrors bean=domainInstance _as="list" /]
				</div>
				[/@g.hasErrors]
			  [@g.form action="save" class="uniForm"]
        <div class="formControls">
         @FIELDS
        </div>
			  <div class="buttons">
			    [@g.hiddenField name="formId" value="\${formInstance.id}" /]		
					[@g.submitButton name="create" class="save" value="\${g.message({'code': 'default.button.create.label', 'default': 'Create'})}" /]
				</div>
				[/@g.form]
			</div>
		</body>
	</html>
	"""
  
	static final String FV_EDIT_VIEW = """\
		[#ftl/]
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<meta name="layout" content="${VIEWER_LAYOUT}" />
				<title>[@g.message code="default.edit.label" args=[flash.formName] /]</title>
				<script type="text/javascript">
				\$(function() {
					 \$('div.buttons').children().button();
					});
				</script>
			</head>
			<body>
				<content tag="nav">
				   <div class="title"><h1>[@g.message code="default.edit.label" args=[flash.formName] /]</h1></div>
				   <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
				   <span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
				   <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
				</content>
				<div class="body" style="\${flash.bodyStyles}">
				  <div class="formHeading\${flash.formHeadingHorizontalAlign}">\${flash.formHeading}</div>
					[#if flash.message?exists]
					  <div class="message">\${flash.message}</div>
					[/#if]
					[@g.hasErrors bean=domainInstance]
					<div class="errors">
						[@g.renderErrors bean=domainInstance _as="list" /]
					</div>
					[/@g.hasErrors]
				  [@g.form method="post" class="uniForm"]
			    <div class="formControls">
			     @FIELDS
			    </div>
				  <div class="buttons">
					  [@g.hiddenField name="formId" value="\${formInstance.id}" /]
				    [@g.hiddenField name="id" value="\${domainInstance.id}" /]
				    [@g.hiddenField name="version" value="\${domainInstance.version}" /]				    				  
						[@g.actionSubmit class="save" value="\${g.message({'code': 'default.button.update.label', 'default': 'Update'})}" /]
						[@g.actionSubmit class="delete" value="\${g.message({'code': 'default.button.delete.label', 'default': 'Delete'})}" onclick="return confirm('\${g.message({'code': 'default.button.delete.confirm.message', 'default': 'Are you sure?'})}');" /]
					</div>
					[/@g.form]
				</div>
			</body>
		</html>
		"""
	
  static final String FV_LIST_VIEW = """\
	[#ftl/]  
  <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${VIEWER_LAYOUT}" />
        <title>[@g.message code="default.list.label" args=[flash.formName] /]</title>
        [@jqDT.resources jqueryUi="true" /]
        <script type="text/javascript">
				\$(function() {
					  \$('.list table').dataTable({
						  sScrollY: '50%',
						  bProcessing: true,
						  bServerSide: true,
						  sAjaxSource: './listData?formId=\${formInstance.id}',
						  bJQueryUI: true,
						  "sPaginationType": "full_numbers",
						  "bScrollCollapse": true			  
					   });
					   \$('.list tbody').click(function(event) {
					     var id = \$('td:first', event.target.parentNode).text();
					     window.location = "./show/" + id + '?formId=\${formInstance.id}';
					   });
				   });        
        </script>
    </head>
    <body>
			<content tag="nav">
			   <div class="title"><h1>[@g.message code="default.list.label" args=[flash.formName] /]</h1></div>
			   <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
			   <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
			</content>
      <div class="body">
			[#if flash.message?exists]
			  <div class="message">\${flash.message}</div>
			[/#if]
        <div class="list">
           <table>
						  <thead>
							   <tr>
								  @FIELDS_HEADER
							   </tr>
							</thead>
							<tbody></tbody>
							<tfoot>
							   <tr>
								  @FIELDS_HEADER
							   </tr>
							</tfoot>
           </table>
        </div>
    </div>
    </body>
</html>  
  """
  static final String FV_SHOW_VIEW = """\
	  [#ftl/]
	  <html>
		  <head>
			  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			  <meta name="layout" content="${VIEWER_LAYOUT}" />
			  <title>[@g.message code="default.show.label" args=[flash.formName] /]</title>
			  <script type="text/javascript">
			  \$(function() {
				 \$('div.buttons').children().button();
					});
			  </script>
		  </head>
		  <body>
			  <content tag="nav">
				 <div class="title"><h1>[@g.message code="default.show.label" args=[flash.formName] /]</h1></div>
				 <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
				 <span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
				 <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
			  </content>
	      <div class="body" style="\${flash.bodyStyles}">
	        <div class="formHeading\${flash.formHeadingHorizontalAlign}">\${flash.formHeading}</div>
				[#if flash.message?exists]
				  <div class="message">\${flash.message}</div>
				[/#if]
				[@g.form class="uniForm"]
	        <div class="formControls">
	         @FIELDS
	        </div>
					<div class="buttons">
				    [@g.hiddenField name="id" value="\${domainInstance.id}" /]		
				    [@g.hiddenField name="formId" value="\${formInstance.id}" /]				
						[@g.actionSubmit class="edit" value="\${g.message({'code': 'default.button.edit.label', 'default': 'Edit'})}" /]
						[@g.actionSubmit class="delete" value="\${g.message({'code': 'default.button.delete.label', 'default': 'Delete'})}" onclick="return confirm('\${g.message({'code': 'default.button.delete.confirm.message', 'default': 'Are you sure?'})}');" /]
					</div>
				[/@g.form]
				</div>
		  </body>
	  </html>
	 """
}
