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
package grails.plugin.freemarker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.GrailsPluginManager;
import org.codehaus.groovy.grails.plugins.PluginManagerAware;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import freemarker.cache.TemplateLoader;


/**
*
* @author Joshua Burnett
* 
*/
public class GrailsTemplateLoader implements TemplateLoader, ResourceLoaderAware, GrailsApplicationAware, PluginManagerAware {
	protected final Log log = LogFactory.getLog(getClass());
	
	//injected when spring sets up bean
	private GrailsApplication grailsApplication;

    private GrailsPluginManager pluginManager;
	private ResourceLoader resourceLoader;
	
	private static final ThreadLocal<String> pluginNameForTemplate = new ThreadLocal<String>();
	
    private static final String grailsAppPrefix = "/WEB-INF/grails-app/views/"; 
	//static final String FTL_SUFFIX = ".ftl"
	private static final String PLUGIN_NAME_ATTRIBUTE = "pluginNameFromRenderCall";
	
	public GrailsTemplateLoader() {
	}
	
	public Object findTemplateSource(String templateName) throws IOException {
	    if (log.isDebugEnabled()){log.debug("Looking for FreeMarker template with name ["+ templateName + "]");}

		//ApplicationContext ctx = grailsApplication.getMainContext();

		GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest();
		HttpServletRequest request = webRequest != null? webRequest.getCurrentRequest() : null;
		GrailsApplicationAttributes gAttributes = webRequest != null? webRequest.getAttributes() : null;
		
		Object controller = gAttributes != null? gAttributes.getController(request) : null;
		
		ResourceLoader loader = establishResourceLoader();
		String ftlUrl = grailsAppPrefix + templateName; //+ ".ftl"
		Resource resource = loader.getResource(ftlUrl);
		
		if (!resource.exists()) {
			String pluginName = checkForPluginName(request);
			ftlUrl = resolveViewForControllerOrPluginName(controller, templateName, pluginName );
			resource = loader.getResource(ftlUrl);
		}
		if(resource.exists()){
		    if (log.isDebugEnabled()){log.debug("FOUND FTL wth url ["+ ftlUrl + "] ");}
			return resource;
		}else{
			return null;
		}
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		log.debug("Looking for getReader FreeMarker template with name ["+ templateSource + "]");
		try {
			return new InputStreamReader(((Resource) templateSource).getInputStream(), encoding);
		}
		catch (IOException ex) {
		    log.error("Could not find FreeMarker template: "+ templateSource);
			throw ex;
		}
	}


	public long getLastModified(Object templateSource) {
		try {
			return ((Resource)templateSource).lastModified();
		}
		catch (IOException ex) {
		    if (log.isDebugEnabled()){log.debug("Could not obtain last-modified timestamp for FreeMarker template in " + templateSource + " : " + ex);}
			return -1;
		}
	}

	public void closeTemplateSource(Object templateSource) throws IOException {}

	String resolveViewForControllerOrPluginName(Object controller, String templateName) {
	    return resolveViewForControllerOrPluginName(controller, templateName, null);
	}
	
	/**
     * Attempts to resolve a template relative to a controller. Finds the right url if its in a plugin
     *
     * @param controller The controller to resolve the template relative to
     * @param templateName The templates name
     * @param loader The ResourceLoader to use
     * @return The URI of the template
     */
    String resolveViewForControllerOrPluginName(Object controller, String templateName, String pluginName) {
        String ftlUrl = grailsAppPrefix + templateName; // try to resolve the template relative to the controller first, this allows us to support templates provided by plugins

		if(pluginName != null){
			String contextPath = pluginManager.getPluginPath(pluginName);
			if (contextPath != null) {
				ftlUrl = "/WEB-INF" + contextPath + "/grails-app/views/" + templateName;
			}
			if (log.isDebugEnabled()){log.debug("pluginName set for ftl template, ended up with url ["+ ftlUrl + "] and contextpath ["+ contextPath + "]");}
		}
		else if (controller != null) {
			String pathToView = pluginManager != null? pluginManager.getPluginViewsPathForInstance(controller) : null;
			if (pathToView != null) {
				ftlUrl = "/WEB-INF" +  pathToView + "/" + templateName; //+ FTL_SUFFIX;
			}
			if (log.isDebugEnabled()) {log.debug("Controller was not null, pluginame not set so built url [" + ftlUrl + "]");}
		}
		
		return ftlUrl;
    }

	
	ResourceLoader establishResourceLoader() {
		//containsBeanDefinition
		if(grailsApplication.getMainContext().containsBeanDefinition("groovyPageResourceLoader")){
			return (ResourceLoader) grailsApplication.getMainContext().getBean("groovyPageResourceLoader");
		}
		//its deployed in a war or does need a special groovyPageResourceLoader so use the one that is injected from the ResourceLoaderAware
        return resourceLoader;
	}
	
	/**
	 * checks to see if this should look for template in a plugin that was either set during a render call 
	 * or via the service using the pluginNameForView threadLocal
	 */
	String checkForPluginName(HttpServletRequest request) {
		String pluginName = request != null ? (String) request.getAttribute(PLUGIN_NAME_ATTRIBUTE) : null;
		//if the thread local pluginNam is set then us that no matter what. provides for an ovverride
		if(pluginNameForTemplate.get() != null) {pluginName = (String) pluginNameForTemplate.get();}
		if (log.isDebugEnabled()) {log.debug("pluginName is ["+ pluginName + "] ");}
        return pluginName;
	}

	
    public GrailsApplication getGrailsApplication() {
        return grailsApplication;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    public GrailsPluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(GrailsPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static ThreadLocal<String> getPluginNameForTemplate() {
        return pluginNameForTemplate;
    }

}




