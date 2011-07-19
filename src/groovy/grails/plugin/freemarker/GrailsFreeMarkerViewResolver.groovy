/*
 * Copyright 2002-2009 the original author or authors.
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

import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
import org.springframework.web.servlet.view.AbstractUrlBasedView
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.GrailsPluginManager;
import org.codehaus.groovy.grails.plugins.PluginManagerAware;
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.codehaus.groovy.grails.web.util.WebUtils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import grails.util.GrailsUtil


public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver {//implements ResourceLoaderAware {//,PluginManagerAware{

    static final Log log = LogFactory.getLog(GrailsFreeMarkerViewResolver.class);

	private String localPrefix

    static final String FTL_SUFFIX = ".ftl"

	static ThreadLocal pluginNameForView = new ThreadLocal()
	
    
	//inject these when setting up the bean definition
	
	@Resource def groovyPagesTemplateEngine
	@Resource def grailsApplication
	@Resource GrailsPluginManager pluginManager
	@Resource ResourceLoader resourceLoader
	
    
    public GrailsFreeMarkerViewResolver() { 
        viewClass = GrailsFreeMarkerView
		if(GrailsUtil.isDevelopmentEnv()) {
        	cache = false
		}
    }
	
	/**
	 * if you are calling from a service and/or are manually chasing the view then use this. 
	 * It uses the super View.resolveViewName and will look in the cache for the view first
	 */
	View getView(String viewName) {
		getView( viewName, Locale.US)
	}
	
	/**
	 * if you are calling from a service and/or are manually chasing the view, and have a local then use this. 
	 * It uses the super View.resolveViewName will look in the cache for the view first
	 */
	View getView(String viewName, Locale locale) {
		if(!viewName.startsWith('/') ) viewName = "/"+viewName
		super.resolveViewName( viewName, locale) //there is no need for anyohter locale for this as we are just forcing something so the caching is consistent
	}
	
	/**
	 * get the view for a plugin. 
	 * sets a threadlocal adn then passes call to getView(viewname, locale)
	 */
	View getView(String viewName, String pluginName) {
		getView( viewName,  Locale.US,  pluginName)
	}
	
	/**
	 * get the view for a plugin. 
	 * sets a threadlocal and then passes call to getView(viewname, locale)
	 */
	View getView(String viewName, Locale locale, String pluginName) {
		try{
			log.debug("called with plugin [$pluginName] ");
			pluginNameForView.set(pluginName)
			return this.getView(viewName, locale)
		}finally{
			pluginNameForView.remove()
		}
	}
	
	//gets here by AbstractCachingViewResolver.resolveViewName -> urlrslv.createView -> this.loadView
	@Override
    protected View loadView(String viewName, Locale locale) {
        def view = null
        try {
			//this is in UrlBasedViewResolver and calls its loadView which calls UrlBasedViewResolver Buildview which those are the guts
			//fix view name if it doesn't start with a '/'
			if(!viewName.startsWith('/')){
				viewName = "/"+viewName
			}
			view = buildView(viewName)
			if(view){
				view = applicationContext.autowireCapableBeanFactory.initializeBean(view, viewName);
				//view = view.checkResource(locale) ? result : null
			}else{
				return null
			}
        } catch(e) {
            // return null if an exception occurs so the rest of the view
            // resolver chain gets an opportunity to  generate a View
            log.error 'loadView', e
			//e.printStackTrace()
        } finally {
			pluginNameForView.remove()
		}
        return view 
    }

	//this gets called by the super class UrlBasedViewResolver.loadView
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		log.debug("building viewName $viewName")
		View view = super.buildView(viewName)
		//now figure out what the url really should be and use that
		String url = createGrailsUrl(viewName)
		if(url){
			view.url = url
		}else{
			return null
		}
		return view;
	}

	// does the special stuff to figure out what directory should be used for finding the view in grails or looking in a plugin if needed
	String createGrailsUrl(String viewName) {
		// try GSP if res is null
		def ctx = getApplicationContext()

        GrailsWebRequest webRequest = WebUtils.retrieveGrailsWebRequest()
        HttpServletRequest request = webRequest?.getCurrentRequest()
        GroovyObject controller = webRequest?.getAttributes()?.getController(request)
		
        ResourceLoader loader = establishResourceLoader()
		def res = null
		
		//TODO I don't think this is needed but I left it since its used in the grailsViewResolver
        String format = request?.getAttribute(GrailsApplicationAttributes.CONTENT_FORMAT) ?: null

		String ftlUrl 
        
        if (format) {
			ftlUrl = localPrefix + viewName + "." + format + FTL_SUFFIX
            res = loader.getResource(ftlUrl);
            if (!res.exists()) {
                ftlUrl = resolveViewForController(controller, viewName, loader);
                res = loader.getResource(ftlUrl);
            }
        }

        if (res == null || !res.exists()) {
            ftlUrl = localPrefix + viewName + FTL_SUFFIX;
            res = loader.getResource(ftlUrl);
            if (!res.exists()) {
                ftlUrl = resolveViewForController(controller, viewName, loader);
                res = loader.getResource(ftlUrl);
            }
        }

        if (res.exists()) {
			log.debug("found freemarker view at [$ftlUrl] ");
            return ftlUrl;
        }else{
			log.debug("Freemarker view not found");
			return null
		}
	}
	
	
	
	/**
     * Attempts to resolve a view relative to a controller. Finds the right url if its in a plugin
     *
     * @param controller The controller to resolve the view relative to
     * @param viewName The views name
     * @param loader The ResourceLoader to use
     * @return The URI of the view
     */
    String resolveViewForController(GroovyObject controller, String viewName, ResourceLoader loader) {

        String ftlUrl// try to resolve the view relative to the controller first, this allows us to support views provided by plugins

		if(pluginNameForView.get()){
			String contextPath = pluginManager.getPluginPath(pluginNameForView.get())
			log.debug("contextPath is [$contextPath] ");
			if (contextPath ) {
                ftlUrl = '/WEB-INF' + contextPath + '/grails-app/views' + viewName + FTL_SUFFIX
            }
            else {
                ftlUrl = localPrefix + viewName + FTL_SUFFIX;
            }
			log.debug("pluginNameForView had a value and built [$ftlUrl] ");
		}
		else if (controller) {
            String pathToView = pluginManager ? pluginManager.getPluginViewsPathForInstance(controller) : null
            if (pathToView ) {
                ftlUrl = '/WEB-INF' + pathToView + viewName + FTL_SUFFIX;
            }
            else {
                ftlUrl = localPrefix + viewName + FTL_SUFFIX;
            }
			log.debug("Controller was not null and built url [$ftlUrl] ");
        }
        else {
            ftlUrl = localPrefix + viewName + FTL_SUFFIX;
        }
        if (log.isDebugEnabled()) {
            log.debug("Attempting to resolve view for URI [$ftlUrl] using ResourceLoader [${loader.getClass().name}]");
        }
        return ftlUrl;
    }

	
	private ResourceLoader establishResourceLoader() {
        //if a groovyPageResourceLoader exists then use it. groovyPageResourceLoader is the one that is setup while in development (run-app,etc)
		if (applicationContext.containsBean("groovyPageResourceLoader")) {
            return applicationContext.getBean("groovyPageResourceLoader");
        }
		//its deployed in a war or does need a special groovyPageResourceLoader so use the one that is injected from the ResourceLoaderAware
        return resourceLoader
    }

	@Override
    public void setPrefix(String prefix) {
        super.setPrefix(prefix);
        this.localPrefix = prefix;
    }
    
}

