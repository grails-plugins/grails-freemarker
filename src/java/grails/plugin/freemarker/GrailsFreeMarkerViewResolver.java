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
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * 
 * @author Jeff Brown
 * @author Daniel Henrique Alves Lima
 *
 */
public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver{//} implements GrailsApplicationAware {
    private final Log exceptionLog = LogFactory.getLog("GrailsFreeMarkerViewResolver.EXCEPTION");
	private final Log log = LogFactory.getLog(GrailsFreeMarkerViewResolver.class);
	
	//injected autowired
	//GrailsApplication grailsApplication
	private FreeMarkerConfig freemarkerConfig;
 	private boolean hideException = true;
	private boolean requireViewSuffix = false;
	
	//end inject
	
	static final String FTL_SUFFIX = ".ftl";
	
    // public GrailsFreeMarkerViewResolver() { 
    //     setViewClass(GrailsFreeMarkerView.class);
    // }

	@Override
	protected Class requiredViewClass() {
		return GrailsFreeMarkerView.class;
	}

	@Override
    protected View loadView(String viewName, Locale locale) {
        View view = null;
		if(requireViewSuffix && !viewName.endsWith(FTL_SUFFIX)) {
			return null;
		}
		else if (viewName.endsWith(FTL_SUFFIX)){
			//remove the .ftl suffix if it was added so the normal process can add it later
			viewName = viewName.substring(0,viewName.length() - FTL_SUFFIX.length());
		}
        try {
            if (log.isDebugEnabled()) log.debug("loadview for " + viewName + ", locale " + locale);
            GrailsFreeMarkerView gview = (GrailsFreeMarkerView) buildView(viewName);
            gview.freemarkerConfig = freemarkerConfig;
    		View result = (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(gview, viewName);
    		view =  (gview.checkResource(locale) ? result : null);

		} catch(Exception e) {
            // by default return null if an exception occurs so the rest of the view
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
	
	//Override just so we have some logging
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        if (log.isDebugEnabled()) log.debug("starting buildView with " + viewName);
		AbstractUrlBasedView view = super.buildView(viewName);
        if (log.isDebugEnabled())  log.debug("buildView view url is " + view);
		return view;
	}

    public void setFreemarkerConfig(FreeMarkerConfig freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
    }
    
    public void setHideException(boolean hideException) {
		this.hideException = hideException;
    }

	public void setRequireViewSuffix(boolean requireViewSuffix) {
		this.requireViewSuffix = requireViewSuffix;
    }
}

