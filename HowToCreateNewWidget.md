# Pre-requisite #
Thanks for your consideration to contribute to the project,
we assumed that you have gone through the [Architecture](Architecture.md) document
and have basic understanding of the design of the Form Builder plugin
prior to reading this document.

Also, before you can create a server-side widget, you need to create the
equivalent client-side field first by refer to [How To Create New Field](http://code.google.com/p/jquery-form-builder-plugin/wiki/HowToCreateNewField).

# How To Create New Widget In 2 Steps #
1. Create the equivalent Groovy class for the corresponding [JQuery Form Builder's](http://code.google.com/p/jquery-form-builder-plugin) field. The name of Groovy class must be same with value specified in `options._type` property of the field. You can refer to the [Widget API documentation](http://code.google.com/p/grails-form-builder-plugin/wiki/WidgetAPI) and [Plain Text](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/widget/PlainText.groovy) or [Single Line Text](http://code.google.com/p/grails-form-builder-plugin/source/browse/trunk/src/groovy/org/grails/formbuilder/widget/SingleLineText.groovy) implementation.

2. Add the necessary widget-specific configuration under `formBuilder` block in Config.groovy file for testing and update `_Install.groovy` script upon completion. For example, the following code is configuration of Single Line Text:
```
formBuilder {
  ...
  SingleLineText {
    type = 'String'
    defaultConstraints = [maxSize: 255]
  }
}
```