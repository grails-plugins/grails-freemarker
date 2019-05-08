/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

/**
 * @author Jeff Brown
 */
@Slf4j
@CompileDynamic
class FreeMarkerTagLib {

    static namespace = 'fm'

    FreeMarkerViewService freeMarkerViewService
    //def freeMarkerConfigurer

    def render = { attrs ->
        if (!attrs.template) {
            throwTagError("Tag [fm:render] is missing required attribute [template]")
        }

        String templateName = attrs.template
        if (!templateName.endsWith('.ftl')) {
            templateName = "${templateName}.ftl"
        }
        def controllerUri = grailsAttributes.getControllerUri(request)
        if (!templateName.startsWith('/') && controllerUri) {
            templateName = "${controllerUri}/${templateName}"
        }

        Map atmodel = attrs.model ?: [:]
        //grab the model thats in the page and controller as well
        Map model = pageScope.getVariablesMap() + atmodel
        log.debug("model: $model")

        freeMarkerViewService.render(templateName, model , out)

    }
}
