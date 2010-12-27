<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Form Designer</title>
		
		<link rel="stylesheet" href="${resource(dir:'css',file:'admin.css')}" type="text/css" media="screen" charset="utf-8"/>
		<link rel="stylesheet" href="${resource(dir:'css',file:'site.forms.css')}" type="text/css" media="screen" charset="utf-8"/>
		
		<g:javascript library="jquery" plugin="jquery" />
		<jqui:resources theme="smoothness" />
		<!-- helpers -->
		<script src="${resource(dir:'js',file:'jquery.tooltip.js')}" type="text/javascript" charset="utf-8"></script>
		<script src="${resource(dir:'js',file:'jquery.metadata.js')}" type="text/javascript" charset="utf-8"></script>
		<!-- FB fase -->
		<script src="${resource(dir:'js',file:'admin.formbuilder.js')}" type="text/javascript" charset="utf-8"></script>
		<script src="${resource(dir:'js',file:'tiny_mce/tiny_mce.js')}" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<h1>Form Designer</h1>
		
		<div class="info">
			Any text you put into your form elements, will be set as the "default" text.
		</div>
		
		<div id="form_builder_nav">
			
			<ul id="form_builder_toolbox">
				<li id='text' class='toolbox'>Rich Text Area</li>
				<li id='textarea' class='toolbox'>Text Area</li>
				<li id='textbox' class='toolbox'>Text Box</li>
				<li id='dropdown' class='toolbox'>Drop Down</li>
				<li id='checkbox' class='toolbox'>Check Box</li>
				<li id='radio' class='toolbox'>Radio Button</li>
				<li id='datetime' class='toolbox'>Date Picker</li>
				<li id='fileupload' class='toolbox'>File Upload</li>
				<li id='button' class='toolbox'>Button</li>
			</ul>
			<ul id="form_builder_properties">
				<li>Select an element to display it's properties</li>
			</ul>
			
		</div>
		
		<div id="form_builder_panel">
			<form method="post" action="preview.gsp" class="fancy">
				<fieldset class='sml'>
					<legend>Built Form</legend>
					<ol>
						<li></li>
					</ol>
				</fieldset>
				<input type="submit" value="Submit"/>
				<input type="button" name="preview" value="Preview" onclick="Admin.formbuilder.preview();"/>
			</form>
		</div>
		
		<div class="dialog {w: 850, h:500}" id="form_builder_preview" title="Preview"></div>
		
	</body>
</html>