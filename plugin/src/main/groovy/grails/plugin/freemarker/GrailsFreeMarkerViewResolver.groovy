/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.core.io.ContextResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.AbstractUrlBasedView
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

import freemarker.template.Configuration
import grails.plugin.viewtools.ViewResourceLocator
import no.api.freemarker.java8.Java8ObjectWrapper

/**
 * Uses Springs ViewResolver design concepts. The primary lookup uses {@link grails.plugin.resourcelocator.ViewResourceLocator}
 * The main DispatcherServlet spins through and calls the ViewResolvers ViewResolver.resolveViewName(String viewName, Locale locale)
 * The inheritance chain here is FreeMarkerViewResolver -> AbstractTemplateViewResolver -> UrlBasedViewResolver -> AbstractCachingViewResolver
 * AbstractCachingViewResolver holds the resolveViewName() which calls loadView() and buildView()
 *
 * This uses the {@link grails.plugin.resourcelocator.ViewResourceLocator} to locate the resource
 * GrailsTemplateLoader does the lifting and caching and lastmodified checking as a freemarker internal
 * The reason for this is that the GrailsTemplateLoader will get called if a [#include] is used in the ftl.
 * So freemarker has to have templateLoaders setup.
 *
 * This resolver sets up a GrailsFreeMarkerView->FreeMarkerView and the meat of the logic is in
 * {@link org.springframework.web.servlet.view.freemarker.FreeMarkerView}
 *
 * the FreeMarkerView gets a freemarker.template.Configuration from the freeMarkerConfigurer.
 * {@link org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer}
 *
 * Then when DispatcherServlet calls FreeMarkerView.render() it uses the freeMarkerGrailsTemplateLoader
 * and any other TemplateLoaders that are set in the FreeMarkerConfigurer and template.Configuration
 * !!!!! That is how it actually finds the files
 * {@link grails.plugin.freemarker.GrailsTemplateLoader}
 *
 * This get called before Grails GroovyPageViewResolver and some of the code is
 * loosely based on that. This gets used simply by registering it as a spring bean (we call it freeMarkerViewResolver)
 * GroovyPageViewResolver details:
 *   - (setup can also be seen in GroovyPagesGrailsPlugin)
 *   - has setOrder(Ordered.LOWEST_PRECEDENCE - 20) which is basically Integer.MAX_VALUE - 20
 *
 * @TODO in the future we should look at caching the FOUND resource here, in the GrailsFreemarkerViewResolver
 * and/or the ViewResourceLocator as it can spin through a lot of plugin directories to find it
 *
 * @author Jeff Brown
 * @author Daniel Henrique Alves Lima
 * @author Joshua Burnett
 */
@Log4j
//log
@CompileStatic
@SuppressWarnings(["LoggerForDifferentClass"])
public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver {

    private static final Log EXCEPTION_LOG = LogFactory.getLog("GrailsFreeMarkerViewResolver.EXCEPTION")
    //private final Log log = LogFactory.getLog(getClass());

    //injected autowired
    //GrailsApplication grailsApplication
    FreeMarkerConfig freeMarkerConfigurer
    ViewResourceLocator viewResourceLocator
    boolean hideException = true
    boolean requireViewSuffix = true //set when bean is setup

    //end inject

    //static final String FTL_SUFFIX = ".ftl";

    // public GrailsFreeMarkerViewResolver() {
    //     setViewClass(GrailsFreeMarkerView.class);
    // }

    @Override
    protected Class<?> requiredViewClass() {
        return GrailsFreeMarkerView.class
    }

    //Overriden for logging
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        log.debug("resolveViewName with $viewName")

        return super.resolveViewName(viewName, locale)
    }

    /**
     * AbstractCachingViewResolver calls this if it doesn't get a hit on the cache
     * Most of the calls to this will look like "/demo/xxx.ftl" etc..
     *
     * @param viewName
     * @param locale
     * @return
     */
    @Override
    @SuppressWarnings(['CatchException', 'ThrowRuntimeException'])
    protected View loadView(String viewName, Locale locale) {
        if (requireViewSuffix && !viewName.endsWith(suffix)) {
            //fast exit
            return null
        }
        log.debug("loadview running for ${viewName}, locale  $locale")

        if (viewName.endsWith(suffix)) {
            //remove the .ftl suffix if it was added so the normal process can add it later
            viewName = viewName.substring(0, viewName.length() - suffix.length())
        }

        View view = null
        try {
            GrailsFreeMarkerView gview = (GrailsFreeMarkerView) buildView(viewName)
            freeMarkerConfigurer.getConfiguration().setObjectWrapper(new Java8ObjectWrapper(Configuration.getVersion()))
            gview.freeMarkerConfigurer = freeMarkerConfigurer
            View result = (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(gview, viewName)

            Resource resource = viewResourceLocator.getResource(gview.url)
            if (resource?.exists()) {
                if (resource instanceof ContextResource) {
                    gview.setUrl(resource.getURL().toString())
                }
                //this now call the freemarker getTemplate to make sure it can load/parse the template.
                // its all cached so it only loads it once
                view = (gview.checkResource(locale) ? result : null)
            }

        }
        catch (Exception e) {
            // by default return null if an exception occurs so the rest of the view
            // resolver chain gets an opportunity to  generate a View
            if (hideException) {
                EXCEPTION_LOG.debug("loadView", e)
            } else {
                EXCEPTION_LOG.error("loadView", e)
                throw new RuntimeException(e)
            }
        }
        return view
    }

    //Override so we can set the bean name
    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = super.buildView(viewName)
        view.setBeanName(viewName)
        log.debug("buildView ran with view: [$view] , url: [ ${view.url}]")
        return view
    }

}
