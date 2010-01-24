package org.springframework.grails.freemarker

class FreeMarkerTagLib {

    static namespace = 'fm'

    def freemarkerConfig

    def render = { attrs ->
        if(!attrs.template)
        throwTagError("Tag [fm:render] is missing required attribute [template]")
        def templateName = attrs.template
        def template
        if(templateName[0] == '/') {
            template = freemarkerConfig.configuration.getTemplate("${templateName}.ftl")
        } else {
            def controllerUri = grailsAttributes.getControllerUri(request)
            template = freemarkerConfig.configuration.getTemplate("${controllerUri}/${templateName}.ftl")
        }
        def model = attrs.model ?: [:]
        template.process(model, out)
    }
}