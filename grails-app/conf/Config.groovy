log4j = {
    root { info() }

    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d{HH:mm:ss,SSS} %-5p %c{3} %x - %m%n')
        //console name:'stdout', layout:pattern(conversionPattern: '%d{HH:mm:ss,SSS} [%t] %-5p %c %x - %m%n')
    }

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
    // trace   'grails.plugin.freemarker.GrailsFreeMarkerViewResolver'
    //         //'org.springframework.web.servlet.view'

    debug   'foo.start',
            'grails.app.services.grails.plugin.freemarker.FreeMarkerViewService',
            //'grails.app.taglib.grails.plugin.freemarker',
            //'grails.plugin.freemarker.TagLibToDirectiveAndFunction',
            'grails.plugin.freemarker.GrailsFreeMarkerViewResolver',
            'grails.plugin.viewtools',
            'grails.plugin.freemarker.GrailsFreeMarkerView',
            'grails.plugin.freemarker.GrailsTemplateLoader',
            'org.codehaus.groovy.grails.web',
            'org.springframework.ui.freemarker.SpringTemplateLoader',
            'foo.end'

            // *****************  ANY DEBUG WITH THE FOLLOWING BLOWs WEIRD NULL POINTER!!!!!!
           //'org.codehaus.groovy.grails.web.pages'

}

grails.doc.authors='Daniel Henrique Alves Lima (text revised by Gislaine Fonseca Ribeiro and others)'
grails.doc.license='Apache License 2.0'

grails.doc.alias = [
  configuration: "3.1. Configuration"
]

