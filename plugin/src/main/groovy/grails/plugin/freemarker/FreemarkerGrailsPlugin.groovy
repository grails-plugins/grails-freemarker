/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import org.grails.core.artefact.TagLibArtefactHandler
import org.springframework.context.ApplicationContext
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer

import grails.core.GrailsClass
import grails.plugins.Plugin
import grails.util.Environment

/**
 * @author Jeff Brown
 * @author Daniel Henrique Alves Lima
 * @author Joshua Burnett
 */
class FreemarkerGrailsPlugin extends Plugin {
    def version = "3.0.1"
    def grailsVersion = "3.2.0 > *"
    def pluginExcludes = [
            "grails-app/views/**/*",
            "grails-app/controllers/**/*",
            "grails-app/services/grails/plugin/freemarker/test/**/*",
            "src/groovy/grails/plugin/freemarker/test/**/*",
            "src/docs/**/*",
            "grails-app/i18n/*",
            'grails-app/taglib/**/test/**/*',
            'scripts/**/Eclipse.groovy',
            "test-plugins/**/*",
            "web-app/**/*"
    ]

    def title = "FreeMarker Views Plugin"
    def description = 'The Grails FreeMarker plugin provides support for rendering FreeMarker templates as views.'
    def documentation = "https://github.com/grails-plugins/grails-freemarker"
    def license = "APACHE"
    def developers = [
            [name: 'Jeff Brown', email: "jeff.brown@springsource.com"],
            [name: 'Joshua Burnett', email: 'joshdev@9ci.com'],
            [name: 'Daniel Henrique Alves Lima', email: 'xxx']
    ]

    def scm = [url: "https://github.com/grails-plugins/grails-freemarker"]
    def issueManagement = [system: "GITHUB", url: "http://github.com/grails-plugins/grails-freemarker"]

    def observe = ["controllers", 'groovyPages']
    def loadAfter = ['controllers', 'groovyPages', 'pluginConfig']

    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"

    Closure doWithSpring() {
        { ->
            def freeconfig = getFreeConfig()
            Properties freeconfigProp = new Properties()
            if (freeconfig.hasProperty('configSettings') && freeconfig.configSettings) {
                freeconfigProp = freeconfig.configSettings.toProperties()
            }
            String ftlSuffix = '.ftl'

            freeMarkerViewResourceLocator(grails.plugin.viewtools.ViewResourceLocator) { bean ->
                searchBinaryPlugins = true //whether to look in binary plugins, does not work in grails2

                //initial searchLocations
                searchPaths = freeconfig.viewResourceLocator.searchLocations

                //resourceLoaders to use right after searchLocations above are scanned
                //searchLoaders = [ref('tenantViewResourceLoader')]

                // in dev mode there will be a groovyPageResourceLoader with base dir set to the running project
                if (Environment.isDevelopmentEnvironmentAvailable()) {
                    grailsViewPaths = ["/grails-app/views"]
                    webInfPrefix = ""
                }

            }

            freeMarkerGrailsTemplateLoader(GrailsTemplateLoader) { bean ->
                bean.autowire = "byName"
                viewResourceLocator = ref('freeMarkerViewResourceLocator')
            }

            def resolveLoaders = { List loaders ->
                return loaders.collect { (it instanceof CharSequence) ? ref(it.toString()) : it }
            }

            Class configClass = freeconfig.tags.enabled == true ? TagLibAwareConfigurer : FreeMarkerConfigurer

            /* factory to return FreeMarkerConfig
             * @see org.springframework.web.servlet.view.freemarker.FreeMarkerConfig */
            freeMarkerConfigurer(configClass) {
                freemarkerSettings = freeconfigProp
                if (freeconfig.templateLoaders) {
                    // default config has the 'freeMarkerGrailsTemplateLoader'
                    postTemplateLoaders = resolveLoaders(freeconfig.templateLoaders)
                }
            }

            //the key bean that kicks it all off using Spring/Grails ViewResolver concepts
            freeMarkerViewResolver(GrailsFreeMarkerViewResolver) {
                //viewLocator = ref("viewLocator")
                viewResourceLocator = ref("freeMarkerViewResourceLocator")
                freeMarkerConfigurer = ref('freeMarkerConfigurer')
                prefix = ''
                suffix = ftlSuffix
                requireViewSuffix = freeconfig.requireViewSuffix
                //hideException = freeconfig.viewResolver.hideException
                order = 10
            }

            if (freeconfig.tags.enabled == true) {
                // Now go through tag libraries and configure them in spring too. With AOP proxies and so on
                for (taglib in grailsApplication.tagLibClasses) {
                    "${taglib.fullName}_fm"(taglib.clazz) { bean ->
                        bean.autowire = true
                        bean.lazyInit = true
                        // Taglib scoping support could be easily added here. Scope could be based on a static field in the taglib class.
                        //bean.scope = 'request'
                    }
                }

                "${TagLibPostProcessor.name}"(TagLibPostProcessor) { bean ->
                    bean.autowire = "byName"
                }
            }
        }
    }

    @Override
    void doWithDynamicMethods() {
        def ctx = getApplicationContext()
//        for (controller in application.controllerClasses) {
//            modRenderMethod(application, controller)
//        }

        /** Fremarker configuration mods **/
        def configLocalizedLookup = grailsApplication.config.grails.plugin.freemarker.localizedLookup
        //turn off the localizedLookup by default
        if (!configLocalizedLookup) {
            ctx.freeMarkerConfigurer.configuration.localizedLookup = false
        }
    }

    @Override
    void onChange(Map<String, Object> event) {
        def freeconfig = getFreeConfig() as Map
//        if (application.isControllerClass(event.source) ) {
//            modRenderMethod(application, event.source)
//        }

        if (freeconfig?.tags?.enabled == true && grailsApplication.isArtefactOfType(TagLibArtefactHandler.TYPE, event.source)) {
            GrailsClass taglibClass = grailsApplication.addArtefact(TagLibArtefactHandler.TYPE, event.source)
            if (taglibClass) {
                // replace tag library bean
                def beanName = taglibClass.fullName
                def beans = beans {
                    "${beanName}_fm"(taglibClass.clazz) { bean ->
                        bean.autowire = true
                        //bean.scope = 'request'
                    }

                    "${TagLibPostProcessor.name}"(TagLibPostProcessor) { bean ->
                        bean.autowire = "byName"
                    }
                }
                beans.registerBeans(event.ctx)
                org.grails.plugins.web.taglib.FormTagLib
                //event.manager?.getGrailsPlugin('groovyPages')?.doWithDynamicMethods(event.ctx)

                ApplicationContext springContext = grailsApplication.mainContext
                for (configurerBeanName in springContext.getBeanNamesForType(AbstractTagLibAwareConfigurer)) {
                    def configurer = springContext.getBean(configurerBeanName)
                    configurer.reconfigure()
                }
            }
        }
    }

    def getFreeConfig() {
        def freeconfig = getConfig()
        freeconfig << getConfig().grails.plugin.freemarker
        freeconfig
    }
//
//    def modRenderMethod(application, controller) {
//        def original = controller.metaClass.getMetaMethod("render", [Map] as Class[])
//
//        controller.metaClass.render = {Map args ->
//            //if args has a pluginName then set it in a threadlocal so we can get to it from the freemarkerViewResolver
//            def request = WebUtils.retrieveGrailsWebRequest()?.getCurrentRequest()
//            if (args.plugin && request) {
//                request.setAttribute(GrailsTemplateLoader.PLUGIN_NAME_ATTRIBUTE,args.plugin)
//            }
//            if (args.loaderAttribute && request) {
//                request.setAttribute("loaderAttribute",args.loaderAttribute)
//            }
//            original.invoke(delegate, [args] as Object[])
//        }
//    }
}
