package org.springframework.grails.freemarker

import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

public class GrailsFreeMarkerViewResolver extends FreeMarkerViewResolver {

    protected View loadView(String viewName, Locale locale) {
        def view = null
        try {
            view = super.loadView(viewName, locale)
        } catch(e) {
            // return null if an exception occurs so the rest of the view
            // resolver chain gets an opportunity to  generate a View
        }
        return view
    }
}

