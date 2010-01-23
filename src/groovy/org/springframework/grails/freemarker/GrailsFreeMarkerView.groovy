package org.springframework.grails.freemarker

import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerView

class GrailsFreeMarkerView extends FreeMarkerView {

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) {
        model.flash = WebUtils.retrieveGrailsWebRequest().attributes.getFlashScope(request)
        super.exposeHelpers(model, request)
    }
}