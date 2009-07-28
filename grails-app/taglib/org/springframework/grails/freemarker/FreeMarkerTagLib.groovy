package org.springframework.grails.freemarker

class FreeMarkerTagLib {
    
    static namespace = 'fm'

    def freemarkerViewResolver
    def grailsApplication

    def render = { attrs ->
        if(!attrs.template)
            throwTagError("Tag [fm:render] is missing required attribute [template]")

        def templateName = attrs.template
        def model = attrs.model ?: [:]
        def view
        if(templateName[0] == '/') {
            view = freemarkerViewResolver.buildView(templateName)
        } else {
            def controllerUri = grailsAttributes.getControllerUri(request)
            view = freemarkerViewResolver.buildView("${controllerUri}/${templateName}")
        }
        view.applicationContext = grailsApplication.mainContext
        view.render(model, request, response)
    }
}