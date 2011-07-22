/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.grails.freemarker

import javax.servlet.http.HttpServletRequest;

import freemarker.cache.TemplateLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.ui.freemarker.SpringTemplateLoader
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.AbstractUrlBasedView

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.GrailsPluginManager;
import org.codehaus.groovy.grails.plugins.PluginManagerAware;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;


/**
*
* @author Joshua Burnett
* 
*/
public class GrailsTemplateLoader implements TemplateLoader, ResourceLoaderAware, GrailsApplicationAware, PluginManagerAware {
	protected final Log log = LogFactory.getLog(getClass())
	
	//injected when spring sets up bean
	GrailsApplication grailsApplication
	GrailsPluginManager pluginManager
	//@Resource def groovyPageResourceLoader
	ResourceLoader resourceLoader
	
	static ThreadLocal pluginNameForView = new ThreadLocal()
	
	static final String grailsAppPrefix = '/WEB-INF/grails-app/views/' 
	static final String FTL_SUFFIX = ".ftl"
	static final String PLUGIN_NAME_ATTRIBUTE = "pluginNameFromRenderCall"
	
	public GrailsTemplateLoader() {
	}
	
	public Object findTemplateSource(String viewName) throws IOException {
		log.debug("Looking for FreeMarker template with name [$viewName]")

		def ctx = grailsApplication.mainContext

		GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest()
		HttpServletRequest request = webRequest?.getCurrentRequest()
		def controller = webRequest?.getAttributes()?.getController(request)
		
		ResourceLoader loader = establishResourceLoader()
		String ftlUrl = grailsAppPrefix + viewName //+ ".ftl"
		def resource = loader.getResource(ftlUrl)
		
		if (!resource.exists()) {
			String pluginName = checkForPluginName(request)
			ftlUrl = resolveViewForControllerOrPluginName(controller, viewName, pluginName );
			resource = loader.getResource(ftlUrl);
		}
		if(resource.exists()){
			log.debug("FOUND FTL wth url [$ftlUrl] ");
			return resource
		}else{
			return null
		}
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		log.debug("Looking for getReader FreeMarker template with name [$templateSource]");
		try {
			return new InputStreamReader(templateSource.getInputStream(), encoding)
		}
		catch (IOException ex) {
			log.debug("Could not find FreeMarker template: $resource")
			throw ex;
		}
	}


	public long getLastModified(Object templateSource) {
		try {
			return templateSource.lastModified();
		}
		catch (IOException ex) {
			log.debug("Could not obtain last-modified timestamp for FreeMarker template in $resource : $ex")
			return -1;
		}
	}

	public void closeTemplateSource(Object templateSource) throws IOException {}

	/**
     * Attempts to resolve a view relative to a controller. Finds the right url if its in a plugin
     *
     * @param controller The controller to resolve the view relative to
     * @param viewName The views name
     * @param loader The ResourceLoader to use
     * @return The URI of the view
     */
    String resolveViewForControllerOrPluginName( controller, String viewName, String pluginName = null) {
	
		
        String ftlUrl = "${grailsAppPrefix}${viewName}" // try to resolve the view relative to the controller first, this allows us to support views provided by plugins

		if(pluginName){
			String contextPath = pluginManager.getPluginPath(pluginName)
			if (contextPath ) {
				ftlUrl = "/WEB-INF$contextPath/grails-app/views/$viewName"
			}
			log.debug("pluginName set for ftl view, ended up with url [$ftlUrl] and contextpath [$contextPath]");
		}
		else if (controller) {
			String pathToView = pluginManager ? pluginManager.getPluginViewsPathForInstance(controller) : null
			if (pathToView ) {
				ftlUrl = "/WEB-INF${pathToView}/${viewName}" //+ FTL_SUFFIX;
			}
			log.debug("Controller was not null, pluginame not set so built url [$ftlUrl] ");
		}
		
		return ftlUrl;
    }

	
	ResourceLoader establishResourceLoader() {
		//containsBeanDefinition
		if(grailsApplication.mainContext.containsBeanDefinition('groovyPageResourceLoader')){
			return grailsApplication.mainContext.groovyPageResourceLoader
		}
		//its deployed in a war or does need a special groovyPageResourceLoader so use the one that is injected from the ResourceLoaderAware
        return resourceLoader
	}
	
	/**
	 * checks to see if this should look for view in a plugin that was either set during a render call 
	 * or via the service using the pluginNameForView threadLocal
	 */
	String checkForPluginName(request) {
		String pluginName = request ? request.getAttribute(PLUGIN_NAME_ATTRIBUTE) : null
		//if the thread local pluginNam is set then us that no matter what. provides for an ovverride
		if(pluginNameForView.get()) pluginName = pluginNameForView.get()
		log.debug("pluginName is [$pluginName] ");
        return pluginName
	}

}




