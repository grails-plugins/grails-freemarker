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

import groovy.util.Eval;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.springframework.web.servlet.view.AbstractUrlBasedView;


/**
 * 
 * @author Jeff Brown
 * @author Daniel Henrique Alves Lima
 *
 */
public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver implements GrailsApplicationAware {

    private final Log exceptionLog = LogFactory.getLog(getClass().getName() + ".EXCEPTION");
	private final Log log = LogFactory.getLog(getClass().getName());
	private GrailsApplication grailsApplication;
	private boolean hideException = false;
	
    public GrailsFreeMarkerViewResolver() { 
        setViewClass(GrailsFreeMarkerView.class);
    }

	@Override
    protected View loadView(String viewName, Locale locale) {
        View view = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("loadview for " + viewName + ", locale " + locale);
            }
            view = super.loadView(viewName, locale);
        } catch(Exception e) {
            // return null if an exception occurs so the rest of the view
            // resolver chain gets an opportunity to  generate a View
            if (hideException) {
                exceptionLog.debug("loadView", e);
            } else {
                exceptionLog.error("loadView", e);
                throw new RuntimeException(e);
            }
        }
        return view;
    }
	
	//just so we have good logging
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("buildView for " + viewName);
        }
		AbstractUrlBasedView view = super.buildView(viewName);
        if (log.isDebugEnabled()) {
            log.debug("view url is " + view);
        }
		return view;
	}

    @Override
    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
        if (this.grailsApplication != null) {
            this.hideException = (Boolean) Eval.x(this.grailsApplication, "x.mergedConfig.asMap(true).grails.plugin.freemarker.viewResolver.hideException");
        }
    }
}

