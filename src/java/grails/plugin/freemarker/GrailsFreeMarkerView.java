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
package grails.plugin.freemarker;

import java.util.Map;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.groovy.grails.web.util.WebUtils;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.beans.BeansException;

/**
 * 
 * @author Jeff Brown
 * @author Joshua Burnett
 *
 */
public class GrailsFreeMarkerView extends FreeMarkerView {

    public FreeMarkerConfig freemarkerConfig;

    
    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        model.put("flash", WebUtils.retrieveGrailsWebRequest().getAttributes().getFlashScope(request));
        super.exposeHelpers(model, request);
    }

	/**
	 * called on instantiation, 
	 * overrides the super default so that it uses the FreeMarkerConfigurer that is
	 * injected into this without doing a new TaglibFactory each time. We have grails taglibs and dont need jsp taglibs
	 * the freemarker config already has one setup
	 * also, the old way only allowed 1 FreeMarkerConfig bean. This lets you have multiple FreeMarkerConfigs if need be.
	 * we don't need the 
	 */
	@Override
	protected void initServletContext(ServletContext servletContext) throws BeansException {
		setConfiguration(freemarkerConfig.getConfiguration());
	}
	


}