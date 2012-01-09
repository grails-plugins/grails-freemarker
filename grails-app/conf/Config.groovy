// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    root { info() }
    
	appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d{HH:mm:ss,SSS} %-5p %c{3} %x - %m%n')
		//console name:'stdout', layout:pattern(conversionPattern: '%d{HH:mm:ss,SSS} [%t] %-5p %c %x - %m%n')
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'

	trace  'org.springframework.web.servlet.view',
			'org.springframework.context.support'

	debug  'grails.plugin.freemarker.GrailsTemplateLoader',
			'grails.plugin.freemarker.GrailsFreeMarkerViewResolver',
			'grails.app.service.grails.plugin.freemarker.FreemarkerViewService',
			'org.codehaus.groovy.grails.web.pages.GroovyPageResourceLoader'
			//'org.springframework.ui.freemarker',
		//'org.springframework.web.servlet.view.freemarker',
		//'org.springframework.core.io.DefaultResourceLoader',
		//'org.springframework.web.context.support.ServletContextResourceLoader'
}

grails.doc.authors='Daniel Henrique Alves Lima (text revised by Gislaine Fonseca Ribeiro and others)'
grails.doc.license='Apache License 2.0'


grails.doc.alias = [
  configuration: "3.1. Configuration"
]
