
	var Admin = {}; //Stripped from Admin System
	var tinyMCE = false; //Placeholder until tinyMCE is loaded at end of DOM.
	
	$(document).ready(function(){
		Admin.formbuilder.init();
	});
	
	Admin.formbuilder = {
		BASEURL: 'formbuilder.php',
		PREVIEWURL: 'preview.php',
		init: function()
		{
			Admin.formbuilder.layout('body');
			Admin.formbuilder.tinymce();
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
					$.get(Admin.formbuilder.BASEURL+'?action=element&type='+type,function(result){
						$(e).removeClass('loading');
						$(into).prepend(result);
						var $newrow = $(into).find('li:first');
						//style
						Admin.formbuilder.editors();
						Admin.formbuilder.properties($newrow);
						Admin.formbuilder.layout($newrow);
						//show
						$newrow.hide().slideDown('slow');
						$(into).sortable("refresh");
						delete result;
					});
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
					Admin.formbuilder.editors();
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
		remove: function(e)
		{
			Admin.formbuilder.confirm("Really remove this element?",function(options){
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
				var title = $(this).attr('rel');
				
				$('#form_builder_panel li.on').removeClass('on');
				$(this).parents('li:first').addClass('on');
				
				$.get(Admin.formbuilder.BASEURL+'?action=properties&type='+title+'&id='+id,function(result){
					$('#form_builder_properties').html(result);
					Admin.formbuilder.attr.get(id);
					Admin.formbuilder.layout('#form_builder_properties');
					$('#form_builder_properties li *:input').keyup(function(){
						Admin.formbuilder.attr.update(this);
					});
					
					delete result;
				});
				
				return false;
			});
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
			
			$.post(Admin.formbuilder.PREVIEWURL,data,function(result){
				$('#form_builder_preview').html(result);
				Admin.formbuilder.dialog('form_builder_preview');
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
						Admin.formbuilder.layout("#"+rel);
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