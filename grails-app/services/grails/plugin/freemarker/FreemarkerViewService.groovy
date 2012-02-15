package grails.plugin.freemarker

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.web.servlet.View
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.context.request.RequestContextHolder


import freemarker.template.Configuration
import freemarker.template.SimpleHash
import freemarker.template.Template

import org.apache.commons.io.output.StringBuilderWriter

/**
 * A service to retireive and process views for freemarker tempalates.
 * 
 * @author Joshua Burnett
 *
 */
class FreemarkerViewService {    
    static transactional = false

    def freemarkerViewResolver
	def grailsApplication
	def localeResolver

    /**
     * get the view for, a plugin if sepcieified.
     * sets a threadlocal and then passes call to getTemplate(viewname, locale)
     * @param viewName      the name of the view, usually will be realtive to the view path
     * @param pluginName    (optional) the name of the plugin it should try for the location on the view
     * @param removePluginNameFromThread (optional) in the finally of this method remove the pluginNameForTemplate threadlocal (defaults to true )
     *                                                     
     */
    View getView(String viewName, String pluginName = null, boolean removePluginNameFromThread = true) {
		try{
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
			log.debug("getView called with viewname : $viewName and pluginName : $pluginName")
			return freemarkerViewResolver.resolveViewName( viewName, getLocale())
        }finally{
            if(pluginName && removePluginNameFromThread)  GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
    }
    
    /**
     * Calls getView to grab the freemarker tempalte and and then passes to render(view,model...) 
     */
    Writer render(String viewName , Map model, Writer writer = new CharArrayWriter(), String pluginName = null){
		RenderEnvironment.bindRequestIfNull(grailsApplication.mainContext, writer) 
		View view = getView(viewName, pluginName,false)
		if(!view) throw new IllegalArgumentException("The ftl view [${viewName}] could not be found" + ( pluginName ? " with plugin [${pluginName}]" : "" ))
        render( view,  model,  writer, pluginName)
    }
    
    /**
     * processes the freemarker template in the View. 
     * sets the plugin thread local if passed in and bind a request if none exists before processing.
     * 
     * @param view  the GrailsFreeMarkerView that holds the template
     * @param model the hash model the should be passed into the freemarker tempalate
     * @param writer (optional) a writer if you have one. a CharArrayWriter will be created by default. 
     * @param pluginName (optional) the plugin to look in for the view
     * @return the writer that was passed in.
     */
    Writer render(View view , Map model, Writer writer = new CharArrayWriter() , String pluginName = null){
		try{
			if(!view) throw new IllegalArgumentException("The 'view' argument cannot be null")
			log.debug("primary render called with view : $view ")
			if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
			// Consolidate static and dynamic model attributes.
	        Map mergedModel = new HashMap(view.attributesMap.size() + (model != null ? model.size() : 0))
	        mergedModel.putAll(view.attributesMap)
	        if (model)  mergedModel.putAll(model)
			RenderEnvironment.bindRequestIfNull(grailsApplication.mainContext, writer) 
			def template = view.getTemplate(getLocale())
		    template.process(new SimpleHash(mergedModel), writer)
	        return writer
        }finally{
            if(pluginName)  GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
		
    }

    /**
     * returns the local by using the localResolver and the webrequest from RequestContextHolder.getRequestAttributes()
     */
	def getLocale(){
		def webRequest = RequestContextHolder.getRequestAttributes()
		return localeResolver.resolveLocale(webRequest.request)
	}
	        
}
