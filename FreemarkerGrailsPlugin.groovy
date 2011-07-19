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
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import grails.util.BuildSettingsHolder
import org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest

class FreemarkerGrailsPlugin {
    // the plugin version
    def version = "0.5-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
	def observe = ["controllers"]
	def loadAfter = ['controllers']
    
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/**/*",
		"grails-app/controllers/**/*",
		"grails-app/services/grails/plugin/freemarker/test/**/*",
		"src/groovy/grails/plugin/freemarker/test/**/*",
		"plugins/**/*",
		"web-app/**/*"
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
		freemarkerConfig(grails.plugin.freemarker.GrailsFreeMarkerConfigurer){
			templateLoaderPath="/"
		}
		
		freemarkerViewResolver(grails.plugin.freemarker.GrailsFreeMarkerViewResolver) {
            prefix = GrailsApplicationAttributes.PATH_TO_VIEWS
            suffix = '.ftl'
            order = 10
        }
    }

	def doWithDynamicMethods = { ctx ->
		for (controller in application.controllerClasses) {
			modRenderMethod(application, controller)
        }
    }

	def onChange = { event ->
		if (application.isControllerClass(event.source) ) {
			modRenderMethod(application, event.source)
		}
	}
	
	def modRenderMethod(application, clazz){
		def render = new RenderDynamicMethod()
		clazz.metaClass.render = {Map args ->
			//if args has a pluginName then set it in a threadlocal so we can get to it from the freemarkerViewResolver 
			if(args.plugin){
				application.mainContext.freemarkerViewResolver.pluginNameForView.set(args.plugin)
				//println "added ${args.plugin}"
			}
			render.invoke(delegate, "render", [args] as Object[])	
        }
	}
}
