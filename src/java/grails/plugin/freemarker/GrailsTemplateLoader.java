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
import java.io.File;
import java.util.List;

import groovy.lang.GroovyObject;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.GrailsPluginManager;
import org.codehaus.groovy.grails.plugins.GrailsPlugin;
import org.codehaus.groovy.grails.commons.GrailsResourceUtils;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.pages.GroovyPagesUriService;
import org.codehaus.groovy.grails.web.pages.GroovyPagesUriSupport;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler;
import org.codehaus.groovy.grails.web.pages.FastStringWriter;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

import grails.util.GrailsUtil;
import grails.util.GrailsNameUtils;
//import grails.util.PluginBuildSettings;

import freemarker.cache.TemplateLoader;
import javax.annotation.PostConstruct;


/**
*
* @author Joshua Burnett
* 
*/
public class GrailsTemplateLoader implements TemplateLoader, ApplicationContextAware {
	protected final Log log = LogFactory.getLog(getClass());
	
	//injected when spring sets up bean
	private GrailsApplication grailsApplication;
    private GrailsPluginManager pluginManager;
	private GroovyPagesUriService groovyPagesUriService;
	
	private ApplicationContext applicationContext;
	private ResourceLoader resourceLoader;
	
	//this will be empty for GRails 2 and set to "/WEB-INF" for 1.3.7
	private String webInfPrefix = "/WEB-INF";
	
	private static final ThreadLocal<String> pluginNameForTemplate = new ThreadLocal<String>();
	
    private static final String VIEW_PATH = "/grails-app/views"; 
	private static final String PLUGIN_NAME_ATTRIBUTE = "pluginNameFromRenderCall";

	
	public GrailsTemplateLoader() {}

	@PostConstruct
    public void init() {
		//if in dev or reloadable then this is the one we want
		if(applicationContext.containsBeanDefinition("groovyPageResourceLoader")){
			log.debug("must be runnning in dev so using the groovyPageResourceLoader");
			resourceLoader = (ResourceLoader)applicationContext.getBean("groovyPageResourceLoader");
		}else{
			resourceLoader = applicationContext;
		}
		
		//grails 2 changed the way the groovyPageResourceLoader works, it used to take care of lopping off the web-inf for us
		//during dev time but it no longer does
		if(GrailsUtil.getGrailsVersion().compareTo("2.0.0") >= 0 && !grailsApplication.isWarDeployed()){
			if (log.isDebugEnabled()) log.debug("nulling webInfPrefix for GRails 2");
			webInfPrefix ="";
		}
	}
	
	public Object findTemplateSource(String templateName) throws IOException {
	    if (log.isDebugEnabled()){log.debug("Looking for FreeMarker template with name ["+ templateName + "]");}

		GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest();
		HttpServletRequest request = webRequest != null? webRequest.getCurrentRequest() : null;
		GrailsApplicationAttributes gAttributes = webRequest != null? webRequest.getAttributes() : null;
		
		GroovyObject controller = gAttributes != null? (GroovyObject)gAttributes.getController(request) : null;
		
		StringBuilder ftlUrl = new StringBuilder();
		ftlUrl.append(webInfPrefix).append(VIEW_PATH).append("/").append(templateName);
		Resource resource = resourceLoader.getResource(ftlUrl.toString());
		
		if ( !resource.exists()) {
			String pluginName = checkForPluginName(request);
			resource = resolveViewForControllerOrPluginName(controller, templateName, pluginName );
		}
		if(resource != null && resource.exists()){
		    if (log.isDebugEnabled()){log.debug("FOUND FTL wth url ["+ ftlUrl + "] ");}
			return resource;
		}else{
			return null;
		}
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		log.debug("Looking with getReader FreeMarker template with name ["+ templateSource + "]");
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

	/**
     * Attempts to resolve a template relative to a controller. Finds the right url if its in a plugin
     *
     * @param controller The controller to resolve the template relative to
     * @param templateName The templates name
     * @return The URI of the template
     */
    Resource resolveViewForControllerOrPluginName(GroovyObject controller, String templateName, String pluginName) {
        StringBuilder ftlUrl = new StringBuilder();

		Resource resource = null;
		
		if(pluginName != null){
			String contextPath = pluginManager.getPluginPath(pluginName);
			if (contextPath != null) {
				ftlUrl.append(webInfPrefix).append(contextPath).append(VIEW_PATH).append("/").append(templateName);
				resource = resourceLoader.getResource(ftlUrl.toString());
			}
			if (log.isDebugEnabled()){log.debug("pluginName set for ftl template, ended up with url ["+ ftlUrl + "] and contextpath ["+ contextPath + "]");}
		}
		else if (controller != null) {
			String pathToView = pluginManager != null? pluginManager.getPluginViewsPathForInstance(controller) : null;
			if (pathToView != null) {
				ftlUrl = new StringBuilder();
				ftlUrl.append(webInfPrefix).append(pathToView).append("/").append(templateName);
				resource = resourceLoader.getResource(ftlUrl.toString());
				if(resource.exists()){
					if (log.isDebugEnabled()) log.debug("pathToView .. url [" + ftlUrl + "]");
					return resource;
				}else{
					//try prefixing the controller name
					String logicalName = GrailsNameUtils.getLogicalPropertyName(controller.getClass().getName(), ControllerArtefactHandler.TYPE);
					ftlUrl = new StringBuilder();
					ftlUrl.append(webInfPrefix).append(pathToView).append("/").append(logicalName).append("/").append(templateName);
					resource = resourceLoader.getResource(ftlUrl.toString());
					
					if (log.isDebugEnabled()) log.debug("pathToView logicalControllerName url [" + ftlUrl + "]");
				}
			}
			
			if(resource==null || !resource.exists()){
				String uri = groovyPagesUriService.getNoSuffixViewURI(controller,templateName);
				ftlUrl = new StringBuilder();
				ftlUrl.append(webInfPrefix).append(VIEW_PATH).append(uri);
				resource = resourceLoader.getResource(ftlUrl.toString());
			}
			if (log.isDebugEnabled()) log.debug("Controller set, pluginame not set, ended up with built url [" + ftlUrl + "]");
		}
		return resource;
    }
		
	/**
	 * checks to see if this should look for template in a plugin that was either set during a render call 
	 * or via the service using the pluginNameForView threadLocal
	 */
	String checkForPluginName(HttpServletRequest request) {
		String pluginName = request != null ? (String) request.getAttribute(PLUGIN_NAME_ATTRIBUTE) : null;
		//if the thread local pluginNam is set then us that no matter what. provides for an ovverride
		if(pluginNameForTemplate.get() != null) pluginName = (String) pluginNameForTemplate.get();
		
		if (log.isDebugEnabled()) log.debug("pluginName is ["+ pluginName + "] ");
        
        return pluginName;
	}
	
	
    public static ThreadLocal<String> getPluginNameForTemplate() {
        return pluginNameForTemplate;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }
    public void setPluginManager(GrailsPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
	public void setGroovyPagesUriService(GroovyPagesUriService groovyPagesUriService){
		this.groovyPagesUriService = groovyPagesUriService;
	}
    // 
    // public ResourceLoader getResourceLoader() {
    //     return resourceLoader;
    // }
    // 
    // public void setResourceLoader(ResourceLoader resourceLoader) {
    //     this.resourceLoader = resourceLoader;
    // }

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
	
}




