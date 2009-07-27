package org.springframework.grails.freemarker

class FreeMarkerTagLib {
    
    static namespace = 'fm'

    def freemarkerConfig

    def render = { attrs ->
        def templateName = attrs.template
        def template = freemarkerConfig.configuration.getTemplate("${templateName}.ftl")
        def model = attrs.model ?: [:]
        template.process(model, out)
    }
}