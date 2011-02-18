package org.grails.formbuilder

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class ReloadUpdatedDomainClassesJob {
    def timeout =  CH.config.formBuilder.reloadUpdatedDomainClassesInMs
    def startDelay = 60000
    def concurrent = false
    def domainClassService

    def execute(context) {
        Date currentTime = new Date()
        def lastExecuted = context.jobDetail.jobDataMap['lastExecuted'] ?: currentTime
        context.jobDetail.jobDataMap['lastExecuted'] = currentTime
        println "oldLastExecuted: ${lastExecuted}, newLastExecuted: ${context.jobDetail.jobDataMap['lastExecuted']}"
        domainClassService.reloadUpdatedDomainClasses(lastExecuted)
    }
}
