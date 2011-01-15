[#ftl/]
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
         [#assign entityName=g._message({'code': 'form.label', 'default': 'Form'}) /]
        <title>[@g.message code="default.show.label" args=[entityName] /]</title>
        <script type="text/javascript">
        $(function() {
        	 $('#container').formbuilder({tabSelected: 1});
        	 $("input").attr("disabled", true);
        	 $("select").attr("disabled", true);
        	    });
        </script>    
    </head>
    <body>
        <content tag="nav">
           <div class="title"><h1>[@g.message code="default.show.label" args=[entityName] /]</h1></div>
           <span class="menuButton"><a class="home" href="${g._createLink({'uri': '/'})}">${g._message({'code':'default.home.label'})}</a></span>
           <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>  
           <span class="menuButton">[@g.link class="create" action="create"][@g.message code="default.new.label" args=[entityName] /][/@g.link]</span>  
        </content>
				<div id="container">
				  <div id="header">
		        [#if flash.message?exists]
		        <div class="message">${flash.message}</div>
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
									<li><a href="#fieldSettings">Field Settings</a></li>
									<li><a href="#formSettings">Form Settings</a></li>
								</ul>
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
				  <g:form action="save" name="builderForm" class="uniForm">
				  <div id="builderPanel">
						  <div id="emptyBuilderPanel">Start adding some fields from the menu on the left.</div>
						  <fieldset>
						    <g:hiddenField name="id" value="${formInstance?.id}" />
						  	<g:hiddenField name="name" value="${formInstance?.name}" />
		            <g:hiddenField name="description" value="${formInstance?.description}" />		  
						  </fieldset>
				  </div>
				  <div class="buttons">
                <g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</div>
					</g:form>	  
				</div>                    
    </body>
</html>
