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
package org.springframework.grails.freemarker

import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
import org.apache.commons.logging.LogFactory
import org.springframework.web.servlet.view.AbstractUrlBasedView


/**
 * 
 * @author Jeff Brown
 *
 */
public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver {

    private final exceptionLog = LogFactory.getLog(getClass().getName() + '.EXCEPTION')
	private final log = LogFactory.getLog(getClass().getName())
	
    public GrailsFreeMarkerViewResolver() { 
        viewClass = GrailsFreeMarkerView
    }

	@Override
    protected View loadView(String viewName, Locale locale) {
        View view = null
        try {
            log.debug("loadview for $viewName")
			view = super.loadView(viewName,locale)
        } catch(e) {
            // return null if an exception occurs so the rest of the view
            // resolver chain gets an opportunity to  generate a View
            exceptionLog.warn 'loadView', e
        }
        return view
    }
	
	//just so we have good logging
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		log.debug("buildView for $viewName")
		def view = super.buildView(viewName)
		log.debug("view url is $view.url")
		return view;
	}
}

