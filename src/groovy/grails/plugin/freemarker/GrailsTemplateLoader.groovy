/*
 * Copyright 2002-2010 the original author or authors.
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import freemarker.cache.TemplateLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader
import org.springframework.web.context.request.RequestContextHolder;


public class GrailsTemplateLoader extends SpringTemplateLoader {

	protected final Log log = LogFactory.getLog(getClass());

	public GrailsTemplateLoader(ResourceLoader resourceLoader, String templateLoaderPath) {
		super(resourceLoader, templateLoaderPath)
		if (log.isDebugEnabled()) {
			log.debug("GrailsTemplateLoader for FreeMarker: using resource loader [$resourceLoader] and template loader path [$templateLoaderPath]");
		}
	}

	public Object findTemplateSource(String name) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Looking for FreeMarker template with name [$name]")
		}
		super.findTemplateSource(name)
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		log.debug("Looking for getReader FreeMarker template with name [$templateSource]");
		super.getReader(templateSource,  encoding)
	}

}
