<html>
    <head>
        <title><g:layoutTitle default="Grails" /> | Grails Form Builder | Form Viewer</title>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir:'css',file:'formbuilder.css')}" />  
        <g:javascript library="jquery" plugin="jquery" />
        <jqui:resources theme="smoothness" />
        <uf:resources />     
        <langs:resources />
        <g:layoutHead />
        <style type="text/css">
					#footer {
						clear: both;
						text-align: center;
						margin-top: 5px;
					}
					
					/* Plain Text field */
					.uniForm .ctrlHolder .PlainText { padding-top: 0; padding-bottom: 0 }
					.uniForm .topAlign { padding-top: 0; padding-bottom: 2em; }
					.uniForm .bottomAlign { padding-top: 2em; padding-bottom: 0; }
					div.rightAlign { text-align: right; }
					div.centerAlign { text-align: center; }					
        </style>
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div>
          <div class="logo"><a href="http://grails.org"><img src="${resource(dir:'images',file:'grails_logo.png')}" alt="Grails" border="0" /></a> <div style="color: grey; font-size: 20pt; margin-top: -35px; padding-left:165px; position:relative">Form Builder</div></div>
          <div class="nav">
            <g:pageProperty name="page.nav" />
            <langs:selector langs="en, zh_CN" default="${flash.language}" />
          </div>
        </div>
        <g:layoutBody />
        <div id="footer">&copy;2011 <a href="http://www.google.com/recaptcha/mailhide/d?k=01jTjzuFnxrUdgxzZaynYmkw==&amp;c=jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY=" onclick="window.open('http://www.google.com/recaptcha/mailhide/d?k\07501jTjzuFnxrUdgxzZaynYmkw\75\75\46c\75jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY\075', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300'); return false;" title="Reveal my e-mail address">Lim, Chee Kin</a>  |  <a href="http://code.google.com/p/grails-form-builder-plugin/">Project Home</a>  |  <a href="http://groups.google.com/group/grails-form-builder-plugin">Discussion Group</a></div>
    </body>
</html>