package grails.plugin.freemarker

import org.springframework.grails.freemarker.GrailsTemplateLoader
import org.springframework.web.servlet.View

import freemarker.template.Configuration
import freemarker.template.SimpleHash
import freemarker.template.Template

import org.apache.commons.io.output.StringBuilderWriter

class FreemarkerService {
	static transactional = false

	def freemarkerViewResolver

	/**
	 * get the view for a plugin. 
	 * sets a threadlocal and then passes call to getView(viewname, locale)
	 */
	View getView(String viewName, String pluginName = null) {
		try{
			if(pluginName) GrailsTemplateLoader.pluginNameForView.set(pluginName)
			return freemarkerViewResolver.resolveViewName( viewName, Locale.US) 
		}finally{
			if(pluginName)  GrailsTemplateLoader.pluginNameForView.remove()
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
		def xhtmlWriter = new StringBuilderWriter() 
		return render( view ,  model,  xhtmlWriter).toString()
	}
	

	
}
