

<%@ page import="org.grails.formbuilder.Form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'form.label', default: 'Form')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${formInstance}">
            <div class="errors">
                <g:renderErrors bean="${formInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${formInstance?.id}" />
                <g:hiddenField name="version" value="${formInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="form.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${formInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="form.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${formInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="templateSource"><g:message code="form.templateSource.label" default="Template Source" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'templateSource', 'errors')}">
                                    <g:textField name="templateSource" value="${formInstance?.templateSource}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="domainClassCode"><g:message code="form.domainClassCode.label" default="Domain Class Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'domainClassCode', 'errors')}">
                                    <g:textField name="domainClassCode" value="${formInstance?.domainClassCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="domainClassFullName"><g:message code="form.domainClassFullName.label" default="Domain Class Full Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'domainClassFullName', 'errors')}">
                                    <g:textField name="domainClassFullName" value="${formInstance?.domainClassFullName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="numberOfColumnInList"><g:message code="form.numberOfColumnInList.label" default="Number Of Column In List" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'numberOfColumnInList', 'errors')}">
                                    <g:textField name="numberOfColumnInList" value="${fieldValue(bean: formInstance, field: 'numberOfColumnInList')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="numberOfRowPerPage"><g:message code="form.numberOfRowPerPage.label" default="Number Of Row Per Page" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'numberOfRowPerPage', 'errors')}">
                                    <g:textField name="numberOfRowPerPage" value="${fieldValue(bean: formInstance, field: 'numberOfRowPerPage')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="fields"><g:message code="form.fields.label" default="Fields" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formInstance, field: 'fields', 'errors')}">
                                    <g:select name="fields" from="${org.grails.formbuilder.Field.list()}" multiple="yes" optionKey="id" size="5" value="${formInstance?.fields*.id}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
