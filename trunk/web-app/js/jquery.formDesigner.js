/* Copyright 2010 the original author or authors.
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

/**
 * Initial code base is based on the works of Sonuku at http://blog.sonuku.com/2009/04/11/php-formbuilder/.
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */

  var tinyMCE = false; //Placeholder until tinyMCE is loaded at end of DOM.
	
	$(document).ready(function(){
		formDesigner.init();
	});
	
	formDesigner = {
		PREVIEWURL: '/form-builder/formDesigner/preview',
		init: function()
		{
			formDesigner.layout('body');
			// formDesigner.tinymce();
		},
		layout: function(e)
		{
			var $active_layout = $(e);
			
			$active_layout.find('form[id=""]').each(function(){
				$(this).attr('id','f'+randomString(50)); //an ID for every form.
			});
			
			$active_layout.find('.last-child').removeClass('last-child'); //meh, safety dance
			
			$active_layout.find('ul,ol').each(function(){
				$(this).children('li:last').addClass('last-child');
			});
			
			$active_layout.children('li:last').addClass('last-child'); //incase the element itself is a ul or ol
			
			$active_layout.find('.tooltip').tooltip({track: true, delay: 0, showURL: false, fade: 0, showBody: " - "});
			
			$active_layout.find('.datepicker').datepicker({dateFormat: 'dd MM yy', duration: ''});
			
			$active_layout.find("#form_builder_toolbox li").click(function(){
					var into = $("#form_builder_panel ol");
					var type = $(this).attr('id');
					var e = this;
					$(this).addClass('loading');
					var result = formDesigner.renderElement(type);
					$(e).removeClass('loading');
					$(into).append(result);
					var $newrow = $(into).find('li:last');
					//style
					formDesigner.editors();
					formDesigner.properties($newrow);
					formDesigner.layout($newrow);
						//show
					$newrow.hide().slideDown('slow');
					$(into).sortable("refresh");
			});
			
			$active_layout.find("#form_builder_panel ol").sortable({
				cursor: 'ns-resize',
				axis: 'y',
				handle: '.handle',
				start: function(e,ui) {
					$('.wysiwyg').each(function(){
						var name = $(this).attr('name');
						if (name) {
							if (tinyMCE.get(name)) {
								tinyMCE.execCommand('mceRemoveControl', false, name);
							}
						}
					});
				},
				stop: function(e,ui) {
					formDesigner.editors();
				}
			});
			
			$active_layout.find('div.dialog').each(function(){
				
				$.metadata.setType("class");
				var w = $(this).metadata().w;
				var h = $(this).metadata().h;
				
				$(this).dialog({
					modal: true,
					zIndex: 400000, /* TinyMCE grief. Their default is literally 300000... Fail*/
					autoOpen: false,
					shadow: false,
					width: (w?parseInt(w, 10):400),
					height: (h?parseInt(h, 10):'auto'),
					title: $(this).attr('title'),
					dragStart: function(event, ui) {
						$(this).find('iframe').hide();
					},
					dragStop: function(event, ui) {
						$(this).find('iframe').show();
					},
					resizeStart: function(event, ui) {
						$(this).find('iframe').hide();
					},
					resizeStop: function(event, ui) {
						$(this).find('iframe').show();
					}
				});
			});
			
			$active_layout = null; //destroy
		},
		tinymce: function(e)
		{
			if (!tinyMCE)
			{			
				tinyMCE_GZ.init({
					plugins : 'pagebreak,style,table,advlink,inlinepopups,media,contextmenu,paste,xhtmlxtras',
					themes : 'simple,advanced',
					disk_cache : true,
					languages : 'en',
					debug : false
				},function(){
					tinyMCE.init({
						mode : "textareas",
						theme : "simple",
						editor_selector : "fancysml"
					});
				});
			} else {
				$(e).find('textarea.fancy, textarea.fancysml').each(function(){
					
					var name = $(this).attr('name');
					if (!tinyMCE.get(name)) tinyMCE.execCommand('mceAddControl', false, name);
				});
			}
		},
		renderElement: function (type) // Element is generated and spat onscreen
		{
			var numOfElements = $("#form_builder_panel ol li").size();
			var id = type + '_' + numOfElements;
			var element = null;
			switch(type)
			{
				case 'text': 				
					element = '<textarea class="wysiwyg" id="' + id + '" name="' + id + '" rows="5" cols="50"></textarea>';
					break;
				case 'textarea':
					element = '<textarea id="' + id + '" name="' + id + '" rows="5" cols="50"></textarea>'; 
					break;
				case 'textbox': 
					element = '<input type="text" id="' + id + '" name="' + id + '" />'; 
					break;
				case 'dropdown': 
					element = '<select id="' + id + '" name="' + id + '"><option value="null">No Option</option></select>'; 
					break;
				case 'checkbox': 
					element = '<span class="values ' + id + '"><input name="' + id + '" type="checkbox" /></span>'; 
					break;
				case 'radio': 
					element = '<span class="values ' + id + '"><input name="' + id + '" type="radio" /></span>'; 
					break;
				case 'datetime':
					element = '<input type="text" id="' + id + '" name="' + id + '" class="datepicker" />';  
					break;
				case 'fileupload': 
					element = '<input type="file" id="' + id + '" name="' + id + '" />';  
					break;
				case 'button': 
					element = '<input type="button" id="' + id + '" name="' + id + '" value="Submit" />';   
				  break;
			}
			
			//give the text box a different label
			var label = (type != 'button') ? 'Static Text' : 'No Label';
			
			//basic output list element.
			var output = '<li> \
				<label for="' + id + '"><a href="#" rel="' + type + '" class="properties tooltip" title="Edit">' + label + '</a></label> \
					<div class="block"> \
						<div class="handle"><span class="icon move">Move</span></div> \
						' + element + ' \
						<span class="note ' + id + '"></span> \
					</div> \
					<div class="clear"></div> \
					<div class="attrs clear ' + id + '"> \
						<input type="hidden" name="properties[' + id + '][type]" value="' + type + '" /> \
					</div> \
				</li>';
		
			
			if (element != null) {
				return output;
			}
		},
		remove: function(e)
		{
			formDesigner.confirm("Really remove this element?",function(options){
				$('label[for='+options.rel+']').parents('li').slideUp('slow',function(){
					$(this).remove();
				});
			},{rel: $(e).attr('rel')});
		},
		editors: function()
		{
			$('.wysiwyg').each(function(){
				var name = $(this).attr('name');
				if (name) {
					if (!tinyMCE.get(name)) tinyMCE.execCommand('mceAddControl', false, name);
				}
			});
		},
		properties: function(e)
		{
			$(e).find('a.properties').click(function(){
				$('#form_builder_properties').html('<span class="icon loading">Loading...</span>');
				var id = $(this).parents('label:first').attr('for');
				var type = $(this).attr('rel');
				$('#form_builder_panel li.on').removeClass('on');
				$(this).parents('li:first').addClass('on');
				var result = formDesigner.renderProperties(type, id); 
				$('#form_builder_properties').html(result);
        formDesigner.attr.get(id);
				formDesigner.layout('#form_builder_properties');
				$('#form_builder_properties li *:input').keyup(function(){
					formDesigner.attr.update(this);
				});
									
				return false;
			});
		}, 
		renderProperties: function(componentType, id) // Builds a list of properties for the builder to display.
		{
			var output = null;
			
			//basic options
			var label = new Object();
			label.name = 'Label';
			label.component = '<input type="text" name="label" rel="label[for=' + id + '] a"/>'; 
			var isRequired = new Object();
			isRequired.name = 'Yes';
			isRequired.component = '<input type="checkbox" name="required" checked />';
			var type = new Object();
			type.name = 'Type';
			type.component = '<select name="required_vars"> \
				<option value="">Text</option> \
				<option value="email">Email</option> \
				<option value="number">Number</option> \
			</select>';
			var required = new Object();
			required.name = 'Required';
			required.component = new Array(isRequired, type);
			var description = new Object();
			description.name = 'Description';
			description.component = '<input type="text" name="description" rel=".note[class~=' + id + ']"/>'; 
			var options = new Array(label, required, description);
			
			var seperate_help = '<span class="icon tooltip" title="Seperate multiple values with a semicolon;<br/>Eg: test;something;here">Help</span>';
			
			//specific options
			switch(componentType)
			{
				case 'dropdown':
					options.push(propertyOption(type, 'select[name=' + id + ']', seperate_help)); 
					break;
				case 'radio':
					options.push(propertyOption(type, 'span.values[class~=' + id + ']', seperate_help)); 
					break;
				case 'checkbox':
					options.push(propertyOption(type, 'span.values[class~=' + id + ']', seperate_help)); 
					break;
				case 'button':
					var option = new Object();
					option.name = 'Value';
					option.component = '<input type="text" name="value" class="' + type + '" rel="input[name=' + id + ']" />'; 				
					options.push(option);
					unset(options, 'Required'); //useless			
					break;
				case 'text':
					unset(options, 'Label'); //useless
					unset(options, 'Description'); //useless
					break;
				default: break;
			}
			
			//throw a delete on the bottom for good measure!
			// $options['Delete'] = form_input(array('rel'=>$id,'name'=>'remove','value'=>'Delete Element','type'=>'button','onclick'=>'formDesigner.remove(this);'));
			
			var output = '';
			var option = null;
			var subOption = null;
			var i, j;
			//spit out the options
			for (i in options) {
				option = options[i];
				output += '<li class="' + id + '"> \
				           <b>' + option.name + '</b>: \
				           <ul>';
						if ($.isArray(option.component)) {
							for (j in option.component) {
								subOption = option.component[j];
								output += '<li class="sub"><b>' + subOption.name + '</b>: ' + subOption.component + '</li>'; 
							}
						} else {
							output += '<li class="sub">' + option.component + '</li>';
						}
				output += '</ul>';
				output += '</li>';
			}
			
			return output;
			
			function unset(options, optionName) {
				var i;
				for (i in options) {
				  if (options[i].name == optionName) {
					  options.splice(i, 1);
					  return;
				    } 	
				}
			}		
			
			function propertyOption(type, rel, element) {
				var option = new Object();
				option.name = 'Options';
				option.component = '<input type="text" name="values" class="' + type + '" rel="' + rel + '" />' + element; 
				return option; 	
			}		
		},
		attr: {
			get: function(id)
			{
				$('.attrs.'+id+' input').each(function(){
					var val = $(this).val();
					var id = $(this).attr('class');
					if (val) {
						$('#form_builder_properties input[name='+id+']').val(val);
					}
				});
			},
			update: function(e)
			{
				var $element = $(e);
				
				var name = $element.attr('name');
				var id = $element.parents('li:not(.sub):first').attr('class');
				var rel = $element.attr('rel');
				var value = $element.val();
				var type = $element.attr('class');
				
				var found = false;
				
				$('div.attrs.'+id+' input').each(function(){
					if ($(this).attr('name') == "properties["+id+"]["+name+"]")
					{
						$(this).val(value);
						found = true;
					}
				});
				
				if (!found) {
					$('div.attrs.'+id).append("<input type='hidden' class='new_property "+name+"' name='properties["+id+"]["+name+"]'/>");
					$('.new_property').removeClass('new_property').val(value);
				}
				
				switch (type)
				{
					case 'dropdown':
						value = value.split(';');
					break;
					case 'checkbox':
						value = value.split(';');
					break;
					case 'radio':
						value = value.split(';');
					break;
					default: break;
				}
				
				if (rel && value) {
					if (!$.isArray(value)) {
						var block = $(rel).not(':input').length;
						
						if (block == 0) $(rel).val(value);
						else $(rel).html(value);
					} else {
						//its an array, oh dear!
						switch (type)
						{
							case 'dropdown':
								var newc = '';
								for (i in value) newc += '<option>'+value[i]+'</option>';
								$(rel).html(newc);
								break;
							case 'radio':
								var newc = '';
								for (i in value) newc += '<input type="radio" name="temp['+name+'][]"> '+value[i]+'<br/>';
								$(rel).html(newc);
								break;
							case 'checkbox':
								var newc = '';
								for (i in value) newc += '<input type="checkbox" name="temp['+name+'][]"> '+value[i]+'<br/>';
								$(rel).html(newc);
								break;
							default: break;
						}
					}
				}
			}
		},
		preview: function()
		{
			
			$('textarea.wysiwyg').each(function(){
				var name = $(this).attr('name');
				if (name) {
					var contents = tinyMCE.get(name).getContent();
				}
				$(this).val(contents);
			});
			
			var data = $('#form_builder_panel form').serialize();
			// alert (data);
			$.post(formDesigner.PREVIEWURL, data, function(result){
				$('#form_builder_preview').html(result);
				formDesigner.dialog('form_builder_preview');
			});
		},
		dialog: function(rel,link)
		{
			var external = $("#"+rel).hasClass('external');
			if (external) {
				
				$("#"+rel).show().html("<iframe src='"+link+"' name='"+rel+"' width='100%' height='100%' frameborder='0' border='0'></iframe>").dialog('open');
				return;
			}
			if (link) {
				if (link.indexOf('http') >= 0) {
					$("#"+rel).html("");
					$.get(link,function(result){
						$("#"+rel).html(result).show().dialog('open');
						formDesigner.layout("#"+rel);
						delete result;
					});
					return;
				}
			}
			$("#"+rel).show().dialog('open');
		},
		confirm: function(msg,callback,options)
		{
			var id = 'confirm_'+Math.ceil(100*Math.random());
			$('body').append('<div id="'+id+'"><p></p></div>');
			$('#'+id+' p').html(msg).dialog({
				modal: true,
				overlay: { 
					opacity: 0.5, 
					background: "black" 
				},
				title: 'Confirm',
				buttons: { 
					"Confirm": function() { 
						if (callback) callback(options);
						$(this).dialog("close");
						$(this).parents('div:first').remove();
					}, 
					"Cancel": function() {
						$(this).dialog("close");
						$(this).parents('div:first').remove();
					} 
				} 
			});
		}
	};
	
	
	
	function randomString(lengt)
	{
		var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
		var string_length = lengt;
		var randomstring = '';
		for (var i=0; i<string_length; i++) {
			var rnum = Math.floor(Math.random() * chars.length);
			randomstring += chars.substring(rnum,rnum+1);
		}
		return randomstring;
	}

	

	

	
