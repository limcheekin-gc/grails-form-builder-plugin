<html>
    <head>
        <title><g:layoutTitle default="Grails" /> | Grails Form Builder</title>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery" />
        <jqui:resources theme="smoothness" />
        <jqJson:resources />
        <uf:resources />    
        <jqfb:resources />        
        <g:layoutHead />
        <style type="text/css">
        div.logo {
				  float: left;
				  width: 340px;
				  padding: 0;
				  margin: 0;
				}  
  
        h1 {
				    color: #48802c;
				    font-weight: normal;
				    font-size: 16px;
				}
				
				/* NAVIGATION MENU */
				
				.nav {
				    background: #fff url(../images/skin/shadow.jpg) bottom repeat-x;
				    border: 1px solid #ccc;
				    border-style: solid none solid none;
				    margin-top: 5px;
				    padding: 10px 12px;
				    height: 15px;
				    margin-left: 340px;
				}
				
				.nav .title {
				    float: left;
				    margin: -13px -13px -13px 0;
				}
								
				.menuButton {
				    float: right;
				    font-size: 10px;
				    padding: 0 5px;
				}
				.menuButton a {
				    color: #333;
				    padding: 4px 6px;
				}
				.menuButton a.home {
				    background: url(../images/skin/house.png) center left no-repeat;
				    color: #333;
				    padding-left: 25px;
				}
				.menuButton a.list {
				    background: url(../images/skin/database_table.png) center left no-repeat;
				    color: #333;
				    padding-left: 25px;
				}
				.menuButton a.create {
				    background: url(../images/skin/database_add.png) center left no-repeat;
				    color: #333;
				    padding-left: 25px;
				}				
				
				#container {
	         clear: both;
	         margin-top: 1em;
                }
                
				.buttons {
				    background: #fff url(../images/skin/shadow.jpg) bottom repeat-x;
				    border: 1px solid #ccc;
				    color: #666;
				    font-size: 10px;
				    margin-top: 5px;
				    overflow: hidden;
				    padding: 0;
				}            			    
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
          </div>
        </div>
        <g:layoutBody />
        <div id="footer">&copy;2011 <a href="http://www.google.com/recaptcha/mailhide/d?k=01jTjzuFnxrUdgxzZaynYmkw==&amp;c=jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY=" onclick="window.open('http://www.google.com/recaptcha/mailhide/d?k\07501jTjzuFnxrUdgxzZaynYmkw\75\75\46c\75jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY\075', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300'); return false;" title="Reveal my e-mail address">Lim, Chee Kin</a>  |  <a href="http://code.google.com/p/grails-form-builder-plugin/">Project Home</a>  |  <a href="http://groups.google.com/group/grails-form-builder-plugin">Discussion Group</a></div>
    </body>
</html>