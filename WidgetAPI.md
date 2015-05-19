# Overview #
The Widget class consists of 2 set API methods:
  * `abstract` methods must be implemented by subclass
  * optional methods that have default implementation and may override by subclass

You can view the latest implementation of [Widget](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/widget/Widget.groovy) class and it's subclasses such as [PlainText](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/widget/PlainText.groovy) and [SingleLineText](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/widget/SingleLineText.groovy) for reference.

## API methods MUST implement by subclass ##
<table border='1'>
<tr>
<th>API</th><th>Description</th>
</tr>
<tr>
<td valign='top'>
<pre><code>String getWidgetTemplateText(<br>
  String name, <br>
  Object settings,<br>
  Locale locale, <br>
  FormDesignerView formDesignerView<br>
)<br>
</code></pre>
</td>
<td>
Return template text of the widget.<br>
<br>
<b>Arguments:</b>
<ul><li><b>name:</b> Field name from <code>Field.name</code> property.<br>
</li><li><b>settings:</b> JSON object from <code>Field.settings</code> property (created by <code>JSON.parse(field.settings)</code>).<br>
</li><li><b>locate:</b> locale of current request.<br>
</li><li><b>formDesignerView</b>: If invoked by <code>FormViewerTemplateService</code>, it is null. Otherwise, invoked by <code>FormTemplateService</code>, the value can be <code>FormDesignerView.CREATE</code>, <code>FormDesignerView.EDIT</code> or <code>FormDesignerView.SHOW</code>. Please see the latest implementation of <a href='http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/FormDesignerView.groovy'>FormDesignerView enum class</a>.<br>
</td>
</tr>
<tr>
<td valign='top'>
<pre><code>String getWidgetReadOnlyTemplateText(<br>
  String name, <br>
  Object settings,<br>
  Locale locale, <br>
  FormDesignerView formDesignerView<br>
)<br>
</code></pre>
</td>
<td>
Return template text of read only state of the widget. Use by show view of the Form Viewer.</li></ul>

<b>Arguments:</b>
<ul><li>Same with <code>getWidgetTemplateText()</code> above.<br>
</td>
</tr>
</table></li></ul>

## API methods MAY implement by subclass ##
<table border='1'>
<tr>
<th>API</th><th>Description</th>
</tr>
<tr>
<td valign='top'>
<pre><code>String getFieldClasses(<br>
  Object settings, <br>
  Locale locale<br>
)<br>
</code></pre>
</td>
<td>
Return class string that will be append to <code>class</code> attribute of <code>&lt;div class="ctrlHolder"&gt;</code> such as <code>&lt;div class="ctrlHolder topAlign"&gt;</code>.<br>
<br>
<b>Arguments:</b>
<ul><li><b>settings:</b> JSON object from <code>Field.settings</code> property (created by <code>JSON.parse(field.settings)</code>).<br>
</li><li><b>locate:</b> locale of current request.<br>
</td>
</tr>
<tr>
<td valign='top'>
<pre><code>String getFieldStyles(<br>
  Object settings, <br>
  Locale locale<br>
)<br>
</code></pre>
</td>
<td>
Return style string that will be append to <code>style</code> attribute of <code>&lt;div class="ctrlHolder"&gt;</code> such as <code>&lt;div class="ctrlHolder topAlign" style="color: #ffffff; background-color: #000000;"&gt;</code>.</li></ul>

<b>Arguments:</b>
<ul><li><b>settings:</b> JSON object from <code>Field.settings</code> property (created by <code>JSON.parse(field.settings)</code>).<br>
</li><li><b>locate:</b> locale of current request.<br>
</td>
</tr>
<tr>
<td valign='top'>
<pre><code>String getFieldConstraints(<br>
  Object settings<br>
)<br>
</code></pre>
</td>
<td>
Return constraints string of the field/property to be included in the generated domain class. Persistent field such as Single Line Text <i>shall</i> override this method.</li></ul>

<b>Arguments:</b>
<ul><li><b>settings:</b> JSON object from <code>Field.settings</code> property (created by <code>JSON.parse(field.settings)</code>).<br>
</td>
</tr>
<tr>
<td valign='top'>
<pre><code>Object getFieldValue(<br>
  Object settings, <br>
  Locale locale<br>
)<br>
</code></pre>
</td>
<td>
Return default value of the field/property.</li></ul>

<b>Arguments:</b>
<ul><li><b>settings:</b> JSON object from <code>Field.settings</code> property (created by <code>JSON.parse(field.settings)</code>).<br>
</li><li><b>locate:</b> locale of current request.<br>
</td>
</tr>
</table>