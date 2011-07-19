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

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerView
import freemarker.template.SimpleHash;
import freemarker.template.Template;

class GrailsFreeMarkerView extends FreeMarkerView {

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) {
        model.flash = WebUtils.retrieveGrailsWebRequest().attributes.getFlashScope(request)
        super.exposeHelpers(model, request)
    }

	//normal AbstractView.render() -> AbstractTemplateView.renderMergedOutputModel ->  FreeMarkerView.renderMergedTemplateModel -> doRender
	/**
	 * skips the dependency on the response and request. Renders straight to a passed in writer. Expects a model as well
	 * 
	 */
	void renderToWriter(Map<String, Object> model, Writer writer){ 
		// Consolidate static and dynamic model attributes.
		Map mergedModel = new HashMap(super.staticAttributes.size() + (model != null ? model.size() : 0))
		mergedModel.putAll(this.staticAttributes);
		if (model)  mergedModel.putAll(model)

		processTemplateToWriter(getTemplate(Locale.US), mergedModel, writer);
	}
	
	void processTemplateToWriter(Template template, Map<String, Object> model, Writer writer)  {
		
		template.process(new SimpleHash(model), writer);
	}

	
}