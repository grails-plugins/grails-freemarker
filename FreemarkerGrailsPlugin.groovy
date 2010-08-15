/* Copyright 2009 Jeff Brown
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
import grails.util.BuildSettingsHolder

class FreemarkerGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/demo/*",
            "grails-app/controllers/*",
            "grails-app/views/templates/**"
    ]

    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"
    def title = "FreeMarker Grails Plugin"
    def description = '''\
The Grails FreeMarker plugin provides support for rendering FreeMarker templates
as views.
'''

    def documentation = "http://grails.org/plugin/freemarker"

    def doWithSpring = {
        boolean developmentMode = !application.warDeployed

        freemarkerConfig(org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer) {
            if(developmentMode) {
                templateLoaderPath="file:${BuildSettingsHolder.settings.baseDir.absolutePath}/grails-app/views"
            } else {
                templateLoaderPath="/WEB-INF/grails-app/views"
            }
        }
        freemarkerViewResolver(org.springframework.grails.freemarker.GrailsFreeMarkerViewResolver) {
            prefix = ''
            suffix = '.ftl'
            order = 10
        }
    }
}
