/*
 * Copyright 2011 the original author or authors.
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

import freemarker.template.Configuration
import freemarker.template.Template

import org.apache.commons.io.output.StringBuilderWriter

/**
 * A service to work directly with the freemarker templates and config.
 * 
 * @author Joshua Burnett
 *
 */
class FreemarkerTemplateService {

    static transactional = false

    def freemarkerConfig

    /**
     * get the view for a plugin.
     * sets a threadlocal and then passes call to getView(viewname, locale)
     */
    Template getTemplate(String templateName, String pluginName = null) {
        try{
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.set(pluginName)
            return freemarkerConfig.configuration.getTemplate(templateName)
        }finally{
            if(pluginName) GrailsTemplateLoader.pluginNameForTemplate.remove()
        }
    }

    Writer render(String viewName , Map model, Writer writer, String pluginName = null){
        render( getTemplate(viewName, pluginName) , model, writer)
    }

    /**
     * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
     *
     */
    Writer render(Template template , Map model, Writer writer){
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
    Writer processString(String templateContent , Map model, Writer writer = new StringBuilderWriter()){
        Template templateInst = new Template("One-off-template-from-string",new StringReader(templateContent),freemarkerConfig.configuration)
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
    Writer processFileName(String templateFileName , Map model, Writer writer = new StringBuilderWriter()){
        Template templateInst = new Template("One-off-template-from-fileName",new FileReader(templateFileName),freemarkerConfig.configuration)
        templateInst.process(model, writer)
        return writer
    }
    
}

