<g:set var="elements" value="${params.findAll { !it.key.startsWith('properties')&& it.key != 'controller' && it.key != 'action'}}" /> 
<form class="fancy">
	<fieldset>
		<legend>Form Preview</legend>
		<ol>
       <% elements.reverseEach { k, v -> %>
	        <g:set var="name" value="${k}"/>
	        <% type = params["properties[$name][type]"] %>
					<g:if test="${type == 'text'}">
					    <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <textarea class="wysiwyg" id="${name}" name="${name}" rows="5" cols="50">${v}</textarea>
					    </li>
					</g:if>
					
					<g:elseif test="${type == 'textarea'}">
					    <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <textarea id="${name}" name="${name}" rows="5" cols="50">${v}</textarea>
					    </li>
					</g:elseif>
					        
					<g:elseif test="${type == 'textbox'}">
					    <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <input type="text" id="${name}" name="${name}" value="${v}" />"
					    </li> 
					</g:elseif>     
					
					<g:elseif test="${type == 'dropdown'}">
					    <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <select id="${name}" name="${name}"><option value="null">No Option</option></select>
					    </li> 
					</g:elseif> 					   
				
					<g:elseif test="${type == 'checkbox'}">
					   <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <span class="values ${name}"><input name="${name}" type="checkbox" /></span>
					   </li> 
					</g:elseif> 	

					<g:elseif test="${type == 'radio'}">
					    <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <span class="values ${name}"><input name="${name}" type="radio" /></span>
					    </li>
					</g:elseif> 	

					<g:elseif test="${type == 'datetime'}">
					   <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <input type="text" id="${name}" name="${name}" class="datepicker" />
					    </li>
					</g:elseif> 	

					<g:elseif test="${type == 'fileupload'}">
					   <li>
					    <label for="${name}">${params["properties[$name][label]"]}</label>
					    <div class="block" />
					    <input type="file" id="${name}" name="${name}" />
					  </li>
					</g:elseif> 	

					<g:elseif test="${type == 'button'}">
              <input type="button" id="${name}" name="${name}" value="${v}" />
					</g:elseif> 			
                   <% } %>
		</ol>
	</fieldset>
</form>

