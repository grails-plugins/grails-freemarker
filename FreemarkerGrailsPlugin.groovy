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
import org.springframework.grails.freemarker.GrailsTemplateLoader
import org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod
import org.codehaus.groovy.grails.web.util.WebUtils

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

		def freeconfig = application.config.grails.plugin.freemarker
		
		freemarkerGrailsTemplateLoader(org.springframework.grails.freemarker.GrailsTemplateLoader){ bean ->
			bean.autowire = "byName"
		}

        freemarkerConfig(org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer) {
			if(freeconfig.preTemplateLoaderBeanName){
				preTemplateLoaders = [ref("$freeconfig.preTemplateLoaderBeanName")]
			}
			if(freeconfig.templateLoaderPaths){
				templateLoaderPaths = configLoaderPaths
			}
			if(freeconfig.postTemplateLoaderBeanName){
				postTemplateLoaders = [ref('freemarkerGrailsTemplateLoader'),ref("$freeconfig.postTemplateLoaderBeanName")] 
			}else{
				postTemplateLoaders = [ref('freemarkerGrailsTemplateLoader')]
			}
        }
        freemarkerViewResolver(org.springframework.grails.freemarker.GrailsFreeMarkerViewResolver) {
            prefix = ''
            suffix = '.ftl'
            order = 10
        }
    }

	def doWithDynamicMethods = { ctx ->
		
		for (controller in application.controllerClasses) {
			modRenderMethod(application, controller)
        }
		
		/** Fremarker configuration mods **/
		def configLocalizedLookup = application.config.grails.plugin.freemarker.localizedLookup
		//turn off the localizedLookup by default
		if(!configLocalizedLookup) {
			ctx.freemarkerConfig.configuration.localizedLookup = false
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
				def request = WebUtils.retrieveGrailsWebRequest()?.getCurrentRequest()
				if(request) {
					request.setAttribute(GrailsTemplateLoader.PLUGIN_NAME_ATTRIBUTE,args.plugin)
				}
			}
			render.invoke(delegate, "render", [args] as Object[])	
        }
	}
}
