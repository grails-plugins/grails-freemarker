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

import freemarker.cache.TemplateLoader;
import grails.plugin.viewtools.ViewResourceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.web.pages.discovery.DefaultGroovyPageLocator;
import org.codehaus.groovy.grails.web.pages.discovery.GroovyPageStaticResourceLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * locates the ftl template source whether in development or WAR deployed mode
 * @see ViewResourceLocator as it leans heavily on that
 *
 * Cacheing is done in the freemarker internals but it will always get the source file
 * so it can call getLastModified to see if it needs to be recompiled
 * @TODO in the future we should look at caching the FOUND resource here, in the GrailsFreemarkerViewResolver
 * and/or the ViewResourceLocator as it can spin through a lot of plugin directories to find it
 *
 * @author Joshua Burnett
 */
public class GrailsTemplateLoader implements TemplateLoader, ApplicationContextAware {

	protected final Log log = LogFactory.getLog(getClass());

	//injected when spring sets up bean
	private GrailsApplication grailsApplication;
	private GroovyPageStaticResourceLocator grailsResourceLocator;
	private ViewResourceLocator viewResourceLocator;

	private ApplicationContext applicationContext;
	private ResourceLoader resourceLoader;

	private String webInfPrefix = "/WEB-INF";

	@PostConstruct
	public void init() {
		//if we are in development or config is set to reloadable GSPs then this is the one we want
		if (applicationContext.containsBeanDefinition("groovyPageResourceLoader")) {
			log.debug("must be runnning in dev so using the groovyPageResourceLoader");
			resourceLoader = (ResourceLoader)applicationContext.getBean("groovyPageResourceLoader");
			webInfPrefix ="";
		}
		else {
			log.debug("using the applicationContext as resource loader");
			//in a production deployment, so just use the applicationContext
			resourceLoader = applicationContext;
		}

	}

	//Implementation of the TemplateLoader iface
	// the passed in templateName does not start with a "/" unlike grails and spring paths
	public Object findTemplateSource(String templateName) throws IOException {
		//debugInfo(templateName);
		if (log.isDebugEnabled()) {log.debug("GrailsTemplateLoader Looking for template with name ["+ templateName + "]");}

		//add the slash back on
		//templateName = "/"+templateName;

		Resource resource = viewResourceLocator.getResource(templateName);
		if (resource != null && resource.exists()) {
			if (log.isDebugEnabled()) {log.debug("viewResourceLocator found ["+ templateName + "] ");}
			return resource;
		}

		return null;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		if (log.isDebugEnabled()) log.debug("Looking with getReader FreeMarker template with name ["+ templateSource + "]");
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
			if (log.isDebugEnabled()) {log.debug("Could not obtain last-modified timestamp for FreeMarker template in " + templateSource + " : " + ex);}
			return -1;
		}
	}

	public void closeTemplateSource(Object templateSource) throws IOException {}

	/**
	 * Attempts to resolve a template relative to a controller. Finds the right url if its in a plugin
	 *
	 */
//	Resource resolveViewForControllerOrPluginName(GroovyObject controller, String templateName, String pluginName) {
//		StringBuilder ftlUrl = new StringBuilder();
//
//		Resource resource = null;
//
//		if (controller != null) {
//			//first check if its a controller in a plugin
//			String pathToView = pluginManager != null? pluginManager.getPluginViewsPathForInstance(controller) : null;
//			if (pathToView != null) {
//				ftlUrl = new StringBuilder();
//				ftlUrl.append(webInfPrefix).append(pathToView).append("/").append(templateName);
//				resource = resourceLoader.getResource(ftlUrl.toString());
//				if (resource.exists()) {
//					if (log.isDebugEnabled()) log.debug("found pathToView .. url [" + ftlUrl + "]");
//					return resource;
//				}
//
//				//try prefixing the controller name
//				String logicalName = GrailsNameUtils.getLogicalPropertyName(controller.getClass().getName(), ControllerArtefactHandler.TYPE);
//				ftlUrl = new StringBuilder();
//				ftlUrl.append(webInfPrefix).append(pathToView).append("/").append(logicalName).append("/").append(templateName);
//				resource = resourceLoader.getResource(ftlUrl.toString());
//				if (log.isDebugEnabled()) log.debug("pathToView logicalControllerName url [" + ftlUrl + "]");
//			}
//
//			if (resource==null || !resource.exists()) {
//				String uri = groovyPagesUriService.getNoSuffixViewURI(controller,templateName);
//				ftlUrl = new StringBuilder();
//				ftlUrl.append(webInfPrefix).append(VIEW_PATH).append(uri);
//				//trying
//				resource = resourceLoader.getResource(ftlUrl.toString());
//			}
//			if (log.isDebugEnabled()) log.debug("Controller set, pluginame not set, ended up with built url [" + ftlUrl + "]");
//		}
//		return resource;
//	}

	public void setGrailsApplication(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setGrailsResourceLocator(GroovyPageStaticResourceLocator grailsResourceLocator) {
		this.grailsResourceLocator = grailsResourceLocator;
	}

	public void setViewResourceLocator(ViewResourceLocator viewResourceLocator) {
		this.viewResourceLocator = viewResourceLocator;
	}


	void debugInfo(String templateName){
		DefaultGroovyPageLocator.PluginViewPathInfo pluginViewPathInfo = DefaultGroovyPageLocator.getPluginViewPathInfo("/"+templateName);
        String path = pluginViewPathInfo.basePath;
        String pluginName = pluginViewPathInfo.pluginName;
        String pathRelativeToPlugin = pluginViewPathInfo.path;
        log.debug("path: " + path);
        log.debug("pluginName: " + pluginName);
        log.debug("pathRelativeToPlugin: " + pathRelativeToPlugin);
        
		log.debug(resourceLoader);
		log.debug("webInfPrefix: " + webInfPrefix);
		log.debug("isWarDeployed: " + grailsApplication.isWarDeployed());
	}
}
