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
package grails.plugin.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;
import grails.plugin.freemarker.GrailsFreeMarkerView;

/**
 * @author Daniel Henrique Alves Lima
 */
public class TagLibAwareView extends GrailsFreeMarkerView {

    private final Log log = LogFactory.getLog(getClass());

    public TagLibAwareView() {
        log.debug("constructor()");
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        if (log.isDebugEnabled()) {
            log.debug("setConfiguration(): configuration " + configuration);
        }

        Boolean isConfigured = (Boolean) configuration
                .getCustomAttribute(AbstractTagLibAwareConfigurer.CONFIGURED_ATTRIBUTE_NAME);
        if (!isConfigured) {
            String message = "FreeMarker Tags configuration is missing: A "
                    + getClass().getSimpleName()
                    + " bean should be defined.";
            log.error("setConfiguration(): " + message);
            throw new RuntimeException(message);
        }

        super.setConfiguration(configuration);
    }

}
