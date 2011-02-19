<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<g:set var="locale" value="${RequestContextUtils.getLocale(request)}" />
<% String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}" %>

<html>
    <head>
        <title><g:layoutTitle default="Grails" /> | Grails Form Builder | Form Designer</title>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery" />
        <jqui:resources theme="smoothness" />
        <jqJson:resources />
        <uf:resources type="css" />
        <jqfb:resources />      
        <langs:resources />
        <script type="text/javascript" src="${resource(dir:'js',file:'jquery.qtip.min.js')}"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir:'css',file:'jquery.qtip.css')}" />  
        <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir:'css',file:'formbuilder.css')}" />  
        <style type="text/css">

        </style>
        <g:layoutHead />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div>
          <div class="logo"><a href="http://grails.org"><img src="${resource(dir:'images',file:'grails_logo.png')}" alt="Grails" border="0" /></a> <div style="color: grey; font-size: 20pt; margin-top: -35px; padding-left:165px; position:relative">Form Builder</div></div>
          <div class="nav">
            <g:pageProperty name="page.nav" />
            <langs:selector langs="en, zh_CN" default="${language}" />
          </div>
        </div>
        <g:layoutBody />
        <div id="footer">&copy;2011 <a href="http://www.google.com/recaptcha/mailhide/d?k=01jTjzuFnxrUdgxzZaynYmkw==&amp;c=jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY=" onclick="window.open('http://www.google.com/recaptcha/mailhide/d?k\07501jTjzuFnxrUdgxzZaynYmkw\75\75\46c\75jsDnH9NCPyt5b09KmqvO1TZu2hgh2LxZya9yYaPRSPY\075', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300'); return false;" title="Reveal my e-mail address">Lim, Chee Kin</a>  |  <a href="http://code.google.com/p/grails-form-builder-plugin/">Project Home</a>  |  <a href="http://groups.google.com/group/grails-form-builder-plugin">Discussion Group</a></div>
    </body>
</html>