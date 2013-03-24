/*
* Copyright 2009-2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package grails.plugin.freemarker

/**
 * @author Jeff Brown
 */
class FreeMarkerTagLib {

    static namespace = 'fm'

    def freemarkerConfig

    def render = { attrs ->
        if (!attrs.template) {
            throwTagError("Tag [fm:render] is missing required attribute [template]")
        }

        def templateName = attrs.template
        def template
        if (templateName[0] == '/') {
            template = freemarkerConfig.configuration.getTemplate("${templateName}.ftl")
        }
        else {
            def controllerUri = grailsAttributes.getControllerUri(request)
            template = freemarkerConfig.configuration.getTemplate("${controllerUri}/${templateName}.ftl")
        }
        def model = attrs.model ?: [:]
        template.process(model, out)
    }
}
