package org.grails.formbuilder.widget

import org.grails.formbuilder.Field
import org.codehaus.groovy.grails.web.pages.FastStringWriter

abstract class Widget {
  String getTemplateText(Field field, Integer index, Boolean forBuilder = false) {
	  FastStringWriter out = new FastStringWriter()
	   out << '<div class="ctrlHolder">'
	   out << getWidgetTemplateText(field, forBuilder)
		 if (forBuilder) {
			  out << """\
				 <a class="ui-corner-all closeButton" href="#"><span class="ui-icon ui-icon-close">delete this widget</span></a>
				 <div class="fieldProperties">
					  [@g.hiddenField name="fields[$index].id" value="${field.id}" /]
					  [@g.hiddenField name="fields[$index].name" value="${field.name}" /]
					  [@g.hiddenField name="fields[$index].type" value="${field.type}" /]
					  [@g.hiddenField name="fields[$index].settings" value="${field.settings}" /]
					  [@g.hiddenField name="fields[$index].sequence" value="${field.sequence}" /]
					  [@g.hiddenField name="fields[$index].status" /]
				 </div>
			  """
		    }
		  out << '</div>'
		  return out.toString()
    }
  
  abstract String getWidgetTemplateText(Field field, Boolean forBuilder)
}
