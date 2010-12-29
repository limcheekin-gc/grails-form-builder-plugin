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
		<script src="${resource(dir:'js',file:'jquery.formDesigner.js')}" type="text/javascript" charset="utf-8"></script>
		<script src="${resource(dir:'js',file:'tiny_mce/tiny_mce.js')}" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<h1>Form Designer</h1>
		
		<div class="info">
			Any text you put into your form elements, will be set as the "default" text.
		</div>
		
		<div id="form_builder_nav">
			<g:render template="toolbox" />
			<g:render template="properties" />
		</div>
		
		<div id="form_builder_panel">
			<form method="post" action="save" class="fancy">
				<fieldset class='sml'>
					<legend>Built Form</legend>
					<ol>
						<li></li>
					</ol>
				</fieldset>
				<input type="submit" value="Submit"/>
				<input type="button" name="preview" value="Preview" onclick="formDesigner.preview();"/>
			</form>
		</div>
		
		<div class="dialog {w: 850, h:500}" id="form_builder_preview" title="Preview"></div>
		
	</body>
</html>