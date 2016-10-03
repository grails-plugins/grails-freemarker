grails {
    plugin {
        freemarker {
            /**
             * when referencing a freemarker view name then require the .ftl suffix
             * if this is false then every single view lookup (render view:xxx, model:yyy)
             * goes through the look up for freemarker first
             */
            requireViewSuffix = true

            /* A list of bean names that implement freemarker.cache.TemplateLoader
            'freeMarkerGrailsTemplateLoader' bean is the deafult and probably only one you need
            best to configure viewResourceLocator to look as then the GrailsFreeMarkerViewResolver has access too*/
            templateLoaders = ["freeMarkerGrailsTemplateLoader"]

            /* a list of any additional spring resource paths to search in (List<String>)*/
            templateLoaderPaths = null //["classpath:org/my/freemarker/views/"]

            viewResourceLocator{
                // a list of location paths or URLS to add to the list for searching.
                // the default here is to allow a freemarker dir in conf for helpful macros
                searchLocations = ["classpath:testftl/"]
            }
            viewResolver {
                /*blow exception in resolver or swallow it and move on */
                hideException = true

                /*allow access with URLs for resourceLoader outside of sandbox with url locations (file:, http:, etc)*/
                //TODO implement this
                //enableUrlLocations = false
            }

            tags {
                /* whether to enable the grails taglibs in the templates */
                enabled = true
                //list of tags to include
                //includeTaglibs = ['g.resource','g.ttt',etc...]
            }
            //extra settings to pass through to the Freemarker Configuration
            //see http://freemarker.sourceforge.net/docs/api/freemarker/template/Configuration.html#setSetting(java.lang.String, java.lang.String)
            // && http://freemarker.sourceforge.net/docs/api/freemarker/core/Configurable.html#setSetting(java.lang.String, java.lang.String)
            /*
            configSettings {
                //for example this will set it to be even more secure.
                new_builtin_class_resolver
            }
            */

            /* A list of bean names that implement freemarker.cache.TemplateLoader
            these will get used first. Add a database loader here for example*/
            preTemplateLoaders = null

            //tries to find templates by appending the local to the name.
            //an odd feature in freemarker that is on by default
            localizedLookup = false
        }
    }
}





// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j.main = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails',
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

             //trace  'org.springframework.web.servlet.view',
             //     'org.springframework.context.support'
    trace   'grails.plugin.freemarker.GrailsFreeMarkerViewResolver'

    debug   'grails.app.services.grails.plugin.freemarker.FreeMarkerViewService',
            'grails.plugin.freemarker.TagLibToDirectiveAndFunction',
            'grails.plugin.resourcelocator.ViewResourceLocator',
            'grails.plugin.freemarker.GrailsTemplateLoader','org.codehaus.groovy.grails.web.servlet.view'
            'grails.plugin.freemarker.GrailsFreeMarkerViewResolver'//,'org.codehaus.groovy.grails.web'
}


