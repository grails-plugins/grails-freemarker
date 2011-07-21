package org.springframework.grails.freemarker

import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
import org.apache.commons.logging.LogFactory
import org.springframework.web.servlet.view.AbstractUrlBasedView


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

