package grails.plugin.freemarker

import org.springframework.grails.freemarker.GrailsTemplateLoader

import freemarker.template.Configuration
import freemarker.template.Template


class FreemarkerService {
    
    static transactional = false

    def freemarkerConfig

    /**
     * get the view for a plugin.
     * sets a threadlocal and then passes call to getView(viewname, locale)
     */
    Template getTemplate(String templateName, String pluginName = null) {
        try{
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
            return freemarkerConfig.configuration.getTemplate(templateName)
        }finally{
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
    }

    Writer render(String viewName , Map model, Writer writer, String pluginName = null){
        render( getTemplate(viewName, pluginName) , model, writer)
    }

    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     *
     */
    Writer render(Template template , Map model, Writer writer){
        // Consolidate static and dynamic model attributes.

        template.process(model, writer)
        return writer
    }

    /**
     * processes and returns the string of the processed view name
     *
     */
    String renderString(String templateName , Map model, String pluginName = null){
        return renderString(getTemplate(templateName, pluginName), model)
    }

    /**
     * processes and returns the string of the processed view
     *
     */
    String renderString(Template view , Map model, String pluginName = null){
        def xhtmlWriter = new CharArrayWriter()
        return render(view , model, xhtmlWriter).toString()
    }
}

