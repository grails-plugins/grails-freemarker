// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    
    appenders {
      console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
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

	debug  'org.springframework.grails.freemarker'//,
	//'org.codehaus.groovy.grails.web.pages.GroovyPageResourceLoader' 
	//	'org.codehaus.groovy.grails.web.pages.GroovyPageResourceLoader'//,
		//'org.springframework.ui.freemarker',
		//'org.springframework.web.servlet.view.freemarker',
		//'org.springframework.core.io.DefaultResourceLoader',
		//'org.springframework.web.context.support.ServletContextResourceLoader'
}
