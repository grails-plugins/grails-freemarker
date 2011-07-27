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
        try{
            GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest()
            Locale locale = webRequest?.locale?:Locale.default
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
            return freemarkerViewResolver.resolveViewName( viewName, locale)
        }finally{
            if(pluginName)  GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
    }
    
    Writer render(String viewName , Map model, Writer writer, String pluginName = null){
        render( getView(viewName, pluginName) ,  model,  writer)
    }
    
    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     *
     */
    Writer render(View view , Map model, Writer writer){
        // Consolidate static and dynamic model attributes.
        Map mergedModel = new HashMap(view.attributesMap.size() + (model != null ? model.size() : 0))
        mergedModel.putAll(view.attributesMap)
        if (model)  mergedModel.putAll(model)
        
        def template = view.getTemplate(Locale.US)
        template.process(new SimpleHash(mergedModel), writer)
        return writer
    }
    
    /**
     * processes and returns the string of the processed view name
     *
     */
    String renderString(String viewName , Map model, String pluginName = null){
        return renderString(getView(viewName, pluginName),  model)
    }
    
    /**
     * processes and returns the string of the processed view
     *
     */
    String renderString(View view , Map model, String pluginName = null){
        def xhtmlWriter = new CharArrayWriter()
        return render( view ,  model,  xhtmlWriter).toString()
    }
    

    
}
