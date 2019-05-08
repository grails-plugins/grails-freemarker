/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import groovy.transform.CompileStatic

import org.apache.commons.io.output.StringBuilderWriter
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig

import freemarker.template.Template

/**
 * Works directly with the freemarker templates and config.
 *
 * @author Joshua Burnett
 */
@CompileStatic
class FreeMarkerTemplateService {

    static transactional = false

    FreeMarkerConfig freeMarkerConfigurer

    /**
     * get the view for a plugin.
     * sets a threadlocal and then passes call to getView(viewname, locale)
     */
    Template getTemplate(String templateName, String pluginName = null) {
        return freeMarkerConfigurer.configuration.getTemplate(templateName)
    }

    Writer render(String viewName, Map model, Writer writer, String pluginName = null) {
        render(getTemplate(viewName, pluginName), model, writer)
    }

    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     */
    Writer render(Template template, Map model, Writer writer) {
        // Consolidate static and dynamic model attributes.
        template.process(model, writer)
        return writer
    }

    /**
     * creates a new template from the string and parses/processes it with the passed in model.
     * This can be fairly inefficient as once the template is built/compiled its not cached
     * This creates/parses a new template from scratch on each call to this method
     *
     * @param templateContent a string with the template code
     * @param model the hash of data to be used in the template
     * @param writer (optional) a writer if you have one. a org.apache.commons.io.output.StringBuilderWriter will be created by default.
     * @return the resulting processed content as a writer (a StringBuilderWriter). Use writer.toString to get the string content
     */
    Writer processString(String templateContent, Map model, Writer writer = new StringBuilderWriter()) {
        Template templateInst = new Template("One-off-template-from-string", new StringReader(templateContent), freeMarkerConfigurer.configuration)
        templateInst.process(model, writer)
        return writer
    }

    /**
     * creates a new template from the passed in file name.
     * Whats different about this is that its not confined by the template loaders and so it can be a pointer to any file on the file system
     * This can be fairly inefficient as once the template is built/compiled its not cached
     * This creates/parses a new template from scratch on each call to this method
     *
     * @param templateFileName the template file name
     * @param model the hash model of data to be used in the template
     * @param writer (optional) a writer if you have one. a org.apache.commons.io.output.StringBuilderWriter will be created by default.
     * @return the resulting processed content as a writer (a StringBuilderWriter). Use writer.toString to get the string content
     */
    Writer processFileName(String templateFileName, Map model, Writer writer = new StringBuilderWriter()) {
        Template templateInst = new Template("One-off-template-from-fileName", new FileReader(templateFileName), freeMarkerConfigurer.configuration)
        templateInst.process(model, writer)
        return writer
    }
}
