package org.springframework.grails.freemarker

class FreeMarkerTagLib {

    static namespace = 'fm'

    //def freemarkerConfig
	def freemarkerViewResolver
	
    def render = { attrs ->
        if(!attrs.template)
        throwTagError("Tag [fm:render] is missing required attribute [template]")
        def templateName = attrs.template
        //def template
		def view
        if(templateName[0] == '/') {
			view = freemarkerViewResolver.getView( templateName)
            //template = freemarkerConfig.configuration.getTemplate("${templateName}.ftl")
        } else {
            def controllerUri = grailsAttributes.getControllerUri(request)
			view = freemarkerViewResolver.getView( "${controllerUri}/${templateName}")
            //template = freemarkerConfig.configuration.getTemplate("${controllerUri}/${templateName}.ftl")
        }
        def model = attrs.model ?: [:]
        //template.process(model, out)
		view.renderToWriter(model,out)
    }
}