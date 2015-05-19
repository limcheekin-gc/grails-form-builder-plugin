# Introduction #
The Grails Form Builder Plugin consists of the following two main components:
|<strong>Form Designer</strong>|<strong>Form Viewer</strong>|
|:-----------------------------|:---------------------------|
|![http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Form_Designer.png](http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Form_Designer.png)|![http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Form_Viewer.png](http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Form_Viewer.png)|

## Form Designer ##
### Overview ###
It is GUI to allow Form Editor to create the form and the form properties and field properties will be stored in `Form` and `Field` domain class for Form Template and Domain Class generation.

### Designer UI ###
[JQuery Form Builder Plugin](http://code.google.com/p/jquery-form-builder-plugin) chosen to be Designer UI. The plugin is a sister project of this project started on 1 Jan 2011. [JQuery Form Builder](http://blog.sonuku.com/2009/04/11/php-formbuilder/) from Sonuku no longer acts as initial code base of Designer UI component. Please see the [blog post](http://limcheekin.blogspot.com/2011/01/kick-off-jquery-form-builder-plugin.html) to find out more.

### Form Template ###
#### Template Engine ####
`FreeMarker` chosen to be the template engine of plugin due to the following reasons:
  * `FreeMarker` template do not need compilation.
  * `FreeMarker` template doesn't support Scriptlet (better security).
  * Better performance compared with GSP as mentioned in this [discussion topic](http://grails.1312388.n4.nabble.com/Freemarker-In-A-Grails-App-td1378733.html) and the outcome of micro-benchmarking performed by me at blog post titled [FreeMarker vs. GSP for Dynamic Template Rendering](http://limcheekin.blogspot.com/2011/01/freemarker-vs-gsp-for-dynamic-template.html) and it's [Part 2](http://limcheekin.blogspot.com/2011/01/freemarker-vs-gsp-for-dynamic-template_27.html).
  * There is `FreeMarker` Grails Plugin available at http://www.grails.org/plugin/freemarker.
The Form Template will be stored database using the [FreeMarkerTemplate](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/grails-app/domain/org/codehaus/groovy/grails/plugins/freemarker/FreeMarkerTemplate.groovy) domain class and load by `DatabaseTemplateLoader`.

#### Markup Language ####
XHTML is chosen as markup language due to the following reasons:
  * Minimal processing; The markups stored in the Form Template file will be the same content delivers to web browser.
  * XHTML is familiar to most power user and allow them to edit the template source directly.
  * Code editor support; `CodeMirror` at http://codemirror.net/ seems like more appropriate than WYMeditor: web-based XHTML editor at http://www.wymeditor.org/. Also, A `CodeMirror` plugin was just released at http://grails.org/plugin/zk-codemirror.
  * Validation support; Unicorn at http://code.w3.org/unicorn and Grails Markup Sanitizer at http://www.grails.org/plugin/sanitizer. These validation tools may not be usable for the plugin as `FreeMarker` template may be not a validate XHTML document (I not sure, anyone?).

**Note:** Code editing is not the focus of this project, you may consider to create a Grails plugin to implement this functionality.

#### Form Template Itself ####
The Form Template itself is the most important component of the plugin as it shared by all plugin components.

The Form Template is `FreeMarker` template with mixed of `FreeMarker` GSP tags which enabled by [freemarker-tags](http://www.grails.org/plugin/freemarker-tags) plugin.

Below is Form Template sample code, it is expect to be updated and expanded:
```
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
			     @FIELDS // To be replaced by Field templates
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
```
You can find out more about current implementation of Form Template [here](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/FormBuilderConstants.groovy).

#### Form Domain Class ####
Below is initial design of the Form Domain Class, it is expect to be updated and expanded:
```
package org.grails.formbuilder

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils
import org.codehaus.groovy.grails.plugins.freemarker.FreeMarkerTemplate

class Form {
	String name
	String description
	String settings
	DomainClass domainClass
	FreeMarkerTemplate listViewTemplate	
	FreeMarkerTemplate createViewTemplate
	FreeMarkerTemplate editViewTemplate
	FreeMarkerTemplate showViewTemplate
	Integer numberOfColumnInList
	Integer numberOfRowPerPage
	Integer persistableFieldsCount
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		name blank: false, unique: true
		description nullable:true, blank: true
		settings nullable:false, blank: false, maxSize:2000
		domainClass nullable:false, unique: true
		listViewTemplate nullable:false, unique: true
		createViewTemplate nullable:false, unique: true
		editViewTemplate nullable:false, unique: true
		showViewTemplate nullable:false, unique: true
		numberOfColumnInList nullable:true, blank: true
		numberOfRowPerPage nullable:true, blank: true
		persistableFieldsCount nullable:false, min: 1
		fieldsList nullable:false, minSize: 1
	}
	
	List fieldsList = new ArrayList()
	static hasMany = [ fieldsList:Field ]
	
	static mapping = { 
		fieldsList cascade:"all-delete-orphan", sort: "sequence", lazy: false 
		domainClass cascade:"all"
	}
	
	def getFields() {
		return LazyList.decorate(fieldsList,
		FactoryUtils.instantiateFactory(Field.class))
	}
}
```

You can find out the latest implementation of domain classes at [here](http://code.google.com/p/grails-form-builder-plugin/source/browse/#svn%2Ftrunk%2Fgrails-app%2Fdomain%2Forg%2Fgrails%2Fformbuilder).

Value of `domainClass`, `listViewTemplate`,	`createViewTemplate`, `editViewTemplate` and `showViewTemplate` was generated based on `Form` and `Field`s properties.

## Form Viewer ##
### Overview ###
Form Viewer display the form to Form User to perform data entry operations.

### Viewer UI ###
Viewer UI will be rendered Form Template and JQuery UI widgets to the web browser.

#### Client Side Validation ####
Using the sub-project of this project, [JQuery Validation UI Plugin](http://code.google.com/p/jquery-validation-ui-plugin).

### Dynamic Views ###
The body of the views will be rendered by `FreeMarker` Template Engine for given `FreeMarker` Form Template text.

### Dynamic Controller ###
The Form Viewer Controller is Dynamic Controller that works dynamically based on the loaded `Form`, `Field` and Dynamic Domain Classes.

You can refer to latest implementation of Form Viewer Controller at [here](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/grails-app/controllers/org/grails/formbuilder/FormViewerController.groovy).

### Dynamic Domain Class ###
When the application startup, it will load and instantiate all Dynamic Domain Classes from  `Form.domainClass` property. This feature enabled by [Dynamic Domain Class plugin](http://code.google.com/p/grails-dynamic-domain-class-plugin/).

# Widget's Class Structure #

![http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Class_Structure.png](http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/architecture/Class_Structure.png)

The design of the Form Builder widgets is very simple, each box in the diagram above is a Groovy class by itself, the structure is very similar to [JQuery Form Builder plugin's structural design](http://code.google.com/p/jquery-form-builder-plugin/wiki/Design#Structural_Design). All widgets developed for the Form Builder such as `PlainText`, `SingleLineText`, etc. must extend/inherit from `Widget` class.
You can find out more about the `Widget` class by look into the [Widget API documentation](http://code.google.com/p/grails-form-builder-plugin/wiki/WidgetAPI).

# Internationalization Support #
Store the message bundles to database by using [localizations](http://grails.org/plugin/localizations) plugin.

# Final Note #
Your feedback to the Grails Form Builder Plugin Architecture is very important to us and very much appreciated. Please join the [discussion topic](http://groups.google.com/group/grails-form-builder-plugin/browse_thread/thread/219d0191979544d1) and tell us what is in your mind.

# References #
## FreeMarker ##
`FreeMarker` Home Page
  * http://freemarker.sourceforge.net/

## XHTML ##
Transitional vs. Strict Markup
  * http://24ways.org/2005/transitional-vs-strict-markup

XHTML custom attributes support
  * http://www.alistapart.com/articles/scripttriggers/
  * http://www.alistapart.com/articles/customdtd/
  * http://www.bennadel.com/blog/1453-Using-jQuery-With-Custom-XHTML-Attributes-And-Namespaces-To-Store-Data.htm

## JQuery UI ##
JQuery UI Home Page
  * http://jqueryui.com/