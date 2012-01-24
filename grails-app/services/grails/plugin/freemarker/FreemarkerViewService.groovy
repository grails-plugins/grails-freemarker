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
     * get the view for a plugin.
     * sets a threadlocal and then passes call to getTemplate(viewname, locale)
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
    
    Writer render(String viewName , Map model, Writer writer = new CharArrayWriter(), String pluginName = null){
		RenderEnvironment.bindRequestIfNull(grailsApplication.mainContext, writer) 
		View view = getView(viewName, pluginName,false)
		if(!view) throw new IllegalArgumentException("The ftl view [${viewName}] could not be found" + ( pluginName ? " with plugin [${pluginName}]" : "" ))
        render( view,  model,  writer, pluginName)
    }
    
    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     * @return the writer that was passed in or if nothing passed in then a StringBuilderWriter with the content
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

	def getLocale(){
		def webRequest = RequestContextHolder.getRequestAttributes()
		return localeResolver.resolveLocale(webRequest.request)
	}
	        
}
