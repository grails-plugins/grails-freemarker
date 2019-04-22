package grails.plugin.freemarker

import freemarker.template.SimpleHash
import freemarker.template.Template
import grails.core.GrailsApplication
import grails.plugin.viewtools.GrailsWebEnvironment
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.freemarker.FreeMarkerView

/**
 * Retrieves and processes views for freemarker tempalates.
 *
 * @author Joshua Burnett
 */
@CompileStatic
@Slf4j
class FreeMarkerViewService {

    static transactional = false

    GrailsFreeMarkerViewResolver freeMarkerViewResolver
    //ViewResourceLocator freeMarkerViewResourceLocator
    GrailsApplication grailsApplication
    LocaleResolver localeResolver

    View getView(String viewName, Locale locale = null){
        locale = locale ?: getLocale()
        //GrailsWebEnvironment.bindRequestIfNull(grailsApplication.mainContext)
        return freeMarkerViewResolver.resolveViewName( viewName, locale)
    }

    /**
     * Calls getView to grab the freemarker tempalte and and then passes to render(view,model...)
     */
    Writer render(String viewName , Map model, Writer writer = new CharArrayWriter()){
        GrailsWebEnvironment.bindRequestIfNull(grailsApplication.mainContext, writer)
        FreeMarkerView view = (FreeMarkerView)freeMarkerViewResolver.resolveViewName( viewName, getLocale())
        if (!view) {
            throw new IllegalArgumentException("The ftl view [${viewName}] could not be found" )
        }
        render( view,  model,  writer)
    }

    /**
     * processes the freemarker template in the View.
     * sets the plugin thread local if passed in and bind a request if none exists before processing.
     *
     * @param view  the GrailsFreeMarkerView/FreeMarkerView that holds the template
     * @param model the hash model the should be passed into the freemarker tempalate
     * @param writer (optional) a writer if you have one. a CharArrayWriter will be created by default.
     * @return the writer that was passed in.
     */
    Writer render(FreeMarkerView view , Map model, Writer writer = new CharArrayWriter()){

        if (!view) {
            throw new IllegalArgumentException("The 'view' argument cannot be null")
        }
        log.debug("primary render called with view : $view ")
        // Consolidate static and dynamic model attributes.
        Map attributesMap = view.attributesMap
        int mapSize = attributesMap.size() + (model != null ? model.size() : 0)
        Map mergedModel = new HashMap(mapSize)
        mergedModel.putAll(attributesMap)
        if (model)  mergedModel.putAll(model)
        GrailsWebEnvironment.bindRequestIfNull(grailsApplication.mainContext, writer)
        Template template = getTemplate(view)
        template.process(new SimpleHash(mergedModel), writer)
        return writer

    }

    @CompileDynamic
    public Template getTemplate(FreeMarkerView view){
        view.getTemplate(getLocale())
    }

    /**
     * returns the local by using the localResolver and the webrequest from RequestContextHolder.getRequestAttributes()
     */
    Locale getLocale() {
        def locale
        def request = GrailsWebRequest.lookup()?.currentRequest
        if (request) locale = localeResolver?.resolveLocale(request)
        if(locale == null) {
            locale = Locale.default
        }
        return locale
    }
}
