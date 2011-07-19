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

import javax.servlet.ServletContext;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.ui.freemarker.SpringTemplateLoader
import org.springframework.context.ResourceLoaderAware;

import javax.annotation.Resource

/**
 * Bean to configure FreeMarker for use in Grails by overriding getResourceLoader 
 * and using instead either the groovyPageResourceLoader thats setup for dev or
 * the servletContextLoader that is being used by groovyPagesTemplateEngine
 *
 * @author Joshua Burnett
 * 
 */
public class GrailsFreeMarkerConfigurer extends FreeMarkerConfigurer {//implements FreeMarkerConfig, ApplicationContextAware,ResourceLoaderAware  {

	@Resource def groovyPageResourceLoader
	
	protected ResourceLoader getResourceLoader() {

		if(groovyPageResourceLoader){
			return groovyPageResourceLoader
		}
		//its deployed in a war or does need a special groovyPageResourceLoader so use the one that is injected from the ResourceLoaderAware
        return super.resourceLoader
	}
	
	protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
		//println templateLoaderPath
		return new GrailsTemplateLoader(getResourceLoader(), templateLoaderPath);
	}
	

}
