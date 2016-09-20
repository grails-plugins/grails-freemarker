/* Copyright 2009 Jeff Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import grails.plugin.freemarker.AbstractTagLibAwareConfigurer
import grails.plugin.freemarker.GrailsFreeMarkerViewResolver
import grails.plugin.freemarker.GrailsTemplateLoader
import grails.plugin.freemarker.TagLibAwareConfigurer
import grails.plugin.freemarker.TagLibPostProcessor
import grails.plugin.viewtools.ViewResourceLocator
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.context.ApplicationContext
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.codehaus.groovy.grails.web.pages.discovery.DefaultGroovyPageLocator
/**
 * @author Jeff Brown
 * @author Daniel Henrique Alves Lima
 * @author Joshua Burnett
 */
class FreemarkerGrailsPlugin {
    def version        = "2.0.0-SNAPSHOT"
    def grailsVersion  = "2.3 > *"
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

    def title           = "FreeMarker Views Plugin"
    def description     = 'The Grails FreeMarker plugin provides support for rendering FreeMarker templates as views.'
    def documentation   = "https://github.com/grails-plugins/grails-freemarker"
    def license         = "APACHE"
    def developers      = [
            [name: 'Jeff Brown', email: "jeff.brown@springsource.com"],
            [name: 'Joshua Burnett', email: 'joshdev@9ci.com'],
            [name: 'Daniel Henrique Alves Lima', email: 'xxx']
    ]

    def scm             = [ url: "https://github.com/grails-plugins/grails-freemarker" ]
    def issueManagement = [ system: "GITHUB", url: "http://github.com/grails-plugins/grails-freemarker" ]

    def observe = ["controllers", 'groovyPages']
    def loadAfter = ['controllers', 'groovyPages']


    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"

    def doWithSpring = {
        def freeconfig = application.mergedConfig.asMap(true).grails.plugin.freemarker
        String ftlSuffix = '.ftl'

        freeMarkerViewResourceLocator(ViewResourceLocator) { bean ->
            //bean.lazyInit = true

            //initial serach locations, default adds ["classpath:freemarker/"]
            searchLocations = freeconfig.viewResourceLocator.searchLocations

            //in dev mode there will be a groovyPageResourceLoader that helps find the views
            if(!application.warDeployed){
                developmentResourceLoader = ref('groovyPageResourceLoader')
            }

        }

        freeMarkerGrailsTemplateLoader(GrailsTemplateLoader) { bean ->
            bean.autowire = "byName"
            viewResourceLocator = ref('freeMarkerViewResourceLocator')
        }

        def resolveLoaders = {List loaders ->
            return loaders.collect{ (it instanceof CharSequence) ? ref(it.toString()) : it }
        }

        Class configClass = freeconfig.tags.enabled == true ? TagLibAwareConfigurer : FreeMarkerConfigurer

        /* factory to return FreeMarkerConfig
         * @see org.springframework.web.servlet.view.freemarker.FreeMarkerConfig */
        freeMarkerConfigurer(configClass) {

            if (freeconfig.templateLoaderPaths) {
                postTemplateLoaders = freeconfig.templateLoaderPaths as String[]
            }

            if (freeconfig.templateLoaders) {
                // default config has the 'freeMarkerGrailsTemplateLoader'
                postTemplateLoaders = resolveLoaders(freeconfig.templateLoaders)
            }

            freemarkerSettings = application.mergedConfig.grails.plugin.freemarker.configSettings?.toProperties()
        }

        //the key bean that kicks it all off using Spring/Grails ViewResolver concepts
        freeMarkerViewResolver(GrailsFreeMarkerViewResolver) {
            //viewLocator = ref("viewLocator")
            viewResourceLocator = ref("freeMarkerViewResourceLocator")
            freeMarkerConfigurer = ref('freeMarkerConfigurer')
            prefix = ''
            suffix = ftlSuffix
            requireViewSuffix = freeconfig.requireViewSuffix
            hideException = freeconfig.viewResolver.hideException
            order = 10
        }

        if (freeconfig.tags.enabled == true) {
            // Now go through tag libraries and configure them in spring too. With AOP proxies and so on
            for (taglib in application.tagLibClasses) {
                "${taglib.fullName}_fm"(taglib.clazz) { bean ->
                    bean.autowire = true
                    bean.lazyInit = true
                    // Taglib scoping support could be easily added here. Scope could be based on a static field in the taglib class.
                    //bean.scope = 'request'
                }
            }

            "${TagLibPostProcessor.name}"(TagLibPostProcessor) {
                grailsApplication = ref('grailsApplication')
            }
        }
    }

    def doWithDynamicMethods = { ctx ->

//        for (controller in application.controllerClasses) {
//            modRenderMethod(application, controller)
//        }

        /** Fremarker configuration mods **/
        def configLocalizedLookup = application.config.grails.plugin.freemarker.localizedLookup
        //turn off the localizedLookup by default
        if (!configLocalizedLookup) {
            ctx.freeMarkerConfigurer.configuration.localizedLookup = false
        }
    }

    def onChange = { event ->
        def freeconfig = application.mergedConfig.asMap(true).grails.plugin.freemarker
//        if (application.isControllerClass(event.source) ) {
//            modRenderMethod(application, event.source)
//        }

        if (freeconfig.tags.enabled == true && application.isArtefactOfType(TagLibArtefactHandler.TYPE, event.source)) {
            GrailsClass taglibClass = application.addArtefact(TagLibArtefactHandler.TYPE, event.source)
            if (taglibClass) {
                // replace tag library bean
                def beanName = taglibClass.fullName
                def beans = beans {
                    "${beanName}_fm"(taglibClass.clazz) { bean ->
                        bean.autowire = true
                        //bean.scope = 'request'
                    }

                    "${TagLibPostProcessor.name}"(TagLibPostProcessor) {
                        grailsApplication = ref('grailsApplication')
                    }
                }
                beans.registerBeans(event.ctx)

                //event.manager?.getGrailsPlugin('groovyPages')?.doWithDynamicMethods(event.ctx)

                def ApplicationContext springContext = application.mainContext
                for (configurerBeanName in springContext.getBeanNamesForType(AbstractTagLibAwareConfigurer)) {
                    def configurer = springContext.getBean(configurerBeanName)
                    configurer.reconfigure()
                }
            }
        }
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
