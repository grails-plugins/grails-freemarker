package org.springframework.grails.freemarker

class FreeMarkerTagLib {
    
    static namespace = 'fm'

    def freemarkerViewResolver
    def grailsApplication

    def render = { attrs ->
        def templateName = attrs.template
        def model = attrs.model ?: [:]
        def view = freemarkerViewResolver.buildView(templateName)
        view.applicationContext = grailsApplication.mainContext
        view.render(model, request, response)
    }
}