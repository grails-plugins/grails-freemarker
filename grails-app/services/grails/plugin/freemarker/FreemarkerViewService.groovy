package grails.plugin.freemarker

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;

import org.springframework.web.servlet.View
import org.springframework.web.servlet.support.RequestContext;

import freemarker.template.Configuration
import freemarker.template.SimpleHash
import freemarker.template.Template

/**
 *
 * @author Joshua Burnett
 *
 */
class FreemarkerViewService {    
    static transactional = false

    def freemarkerViewResolver

    /**
     * get the view for a plugin.
     * sets a threadlocal and then passes call to getTemplate(viewname, locale)
     */
    View getView(String viewName, String pluginName = null) {
        GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest()
		Locale locale = webRequest?.locale?:Locale.default
		log.debug("getView called with viewname : $viewName and pluginName : $pluginName")
		return freemarkerViewResolver.resolveViewName( viewName, locale)
    }
    
    Writer render(String viewName , Map model, Writer writer, String pluginName = null){
        render( getView(viewName, pluginName) ,  model,  writer, pluginName)
    }
    
    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     *
     */
    Writer render(View view , Map model, Writer writer,String pluginName = null){
		try{
            GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest()
            Locale locale = webRequest?.locale?:Locale.default
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
			log.debug("primary render called with view : $view ")
	        // Consolidate static and dynamic model attributes.
	        Map mergedModel = new HashMap(view.attributesMap.size() + (model != null ? model.size() : 0))
	        mergedModel.putAll(view.attributesMap)
	        if (model)  mergedModel.putAll(model)

	        def template = view.getTemplate(locale)
	        template.process(new SimpleHash(mergedModel), writer)
	        return writer
        }finally{
            if(pluginName)  GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
		
    }
    
    /**
     * processes and returns the string of the processed view name
     *
     */
    String renderString(String viewName , Map model, String pluginName = null){
        return renderString(getView(viewName, pluginName),  model,pluginName)
    }
    
    /**
     * processes and returns the string of the processed view
     *
     */
    String renderString(View view , Map model, String pluginName = null){
        def charWriter = new CharArrayWriter()
        return render( view ,  model,  charWriter, pluginName).toString()
    }
    

    
}
