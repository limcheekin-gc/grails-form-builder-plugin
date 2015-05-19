# Installation #
Install the plugin into your project with the following command:
```
grails install-plugin form-builder
```

# Configuration #
After the plugin installed into your project, the following configurations will be appended to your project's Config.groovy file:
```
formBuilder {
  reloadUpdatedDomainClassesInMs = 60000
  SingleLineText {
    type = 'String'
    defaultConstraints = [maxSize: 255]
  }
}
```
  * `reloadUpdatedDomainClassesInMs` allow you to specify interval in millisecond of how frequently the quartz scheduled job checks the `DomainClass.source` update and reload the dynamic domain class if it is updated.

**Note:** You need to change the value of `dbCreate` from `create-drop` to `update` in `DataSource.groovy` file even for development environment, otherwise the form builder plugin will not work properly.

# Run the application #
Run your application with the following command:
```
grails run-app
```

By open your browser at http://localhost:8080/[AppName]/form, you should see the following screen:

![http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/list.png](http://grails-form-builder-plugin.googlecode.com/svn/wiki/images/list.png)