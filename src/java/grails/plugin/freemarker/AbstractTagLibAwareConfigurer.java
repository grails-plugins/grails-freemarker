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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.commons.GrailsTagLibClass;
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;

/**
 * @author Daniel Henrique Alves Lima
 */
public abstract class AbstractTagLibAwareConfigurer extends
        FreeMarkerConfigurer implements GrailsApplicationAware {

    public static final String CONFIGURED_ATTRIBUTE_NAME = "_"
            + AbstractTagLibAwareConfigurer.class.getName() + ".configured";

    private final Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("unused")
    private String suffix = null;

    //private AutoConfigHelper helper = null;

    private GrailsApplication grailsApplication = null;

    @Override
    public void setConfiguration(Configuration configuration) {
        if (log.isDebugEnabled()) {
            log.debug("setConfiguration(): configuration " + configuration);
        }
        // this.helper.autoConfigure(false, configuration);
        super.setConfiguration(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = super.getConfiguration();
        if (log.isDebugEnabled()) {
            log.debug("getConfiguration(): configuration " + configuration);
        }
        // this.helper.autoConfigure(false, configuration);

        reconfigure(configuration);

        return configuration;
    }

    public void setSuffix(String suffix) {
        if (log.isDebugEnabled()) {
            log.debug("setSuffix(): suffix " + suffix);
        }
        this.suffix = suffix;
        // this.helper = new AutoConfigHelper(suffix);
    }

    protected void reconfigure() {
        Configuration configuration = super.getConfiguration();
        if (configuration != null) {
            synchronized (configuration) {
                configuration
                        .removeCustomAttribute(AbstractTagLibAwareConfigurer.CONFIGURED_ATTRIBUTE_NAME);
                reconfigure(configuration);
            }
        }
    }

    protected void reconfigure(Configuration configuration) {
        // TODO: Reconfiguration of reloaded tags
        if (configuration != null) {
            synchronized (configuration) {
                Boolean isConfigured = (Boolean) configuration
                        .getCustomAttribute(AbstractTagLibAwareConfigurer.CONFIGURED_ATTRIBUTE_NAME);

                if (isConfigured == null || isConfigured == false) {
                    try {
                        ApplicationContext springContext = grailsApplication
                                .getMainContext();

                        Map<String, Map<String, TemplateModel>> sharedVars = new LinkedHashMap<String, Map<String, TemplateModel>>();
                        GrailsClass[] tagLibClasses = grailsApplication
                                .getArtefacts(TagLibArtefactHandler.TYPE);

                        for (GrailsClass grailsClass : tagLibClasses) {
                            GrailsTagLibClass tagLibClass = (GrailsTagLibClass) grailsClass;
                            String namespace = tagLibClass.getNamespace();

                            if (log.isDebugEnabled()) {
                                log.debug("reconfigure(): Exposing tag '"
                                        + namespace + "' ("
                                        + tagLibClass.getFullName() + ")");
                            }

                            GroovyObject tagLibInstance = getTagLibInstance(
                                    springContext, tagLibClass.getFullName());
                            Map<String, TemplateModel> sharedVar = sharedVars
                                    .get(namespace);
                            if (sharedVar == null) {
                                sharedVar = new LinkedHashMap<String, TemplateModel>();
                                sharedVars.put(namespace, sharedVar);
                            }
                            
                            Set<String> tagNamesWithReturn = tagLibClass.getTagNamesThatReturnObject();
                            if (tagNamesWithReturn == null) {tagNamesWithReturn = Collections.emptySet();}

                            for (String tagName : tagLibClass.getTagNames()) {
                                if (log.isDebugEnabled()) {
                                    log.debug("reconfigure(): tag " + tagName);
                                }
                                Object tagInstanceObject = null;
                                try {
                                    tagInstanceObject = tagLibInstance
                                            .getProperty(tagName);
                                } catch (IllegalStateException e) {
                                    /*
                                     * Workaround for properties exposed as tags
                                     * and dependent of RequestAttributes
                                     */
                                    log.debug("reconfigure()", e);
                                }
                                if (tagInstanceObject != null
                                        && tagInstanceObject instanceof Closure) {
                                    Closure tagInstance = (Closure) tagInstanceObject;
                                    sharedVar.put(tagName,
                                            new TagLibToDirectiveAndFunction(
                                                    namespace, tagLibInstance,
                                                    tagName, tagInstance, tagNamesWithReturn.contains(tagName)));
                                }
                            }

                        }

                        for (Map.Entry<String, Map<String, TemplateModel>> entry : sharedVars
                                .entrySet()) {
                            if (log.isDebugEnabled()) {
                                log.debug("reconfigure(): @" + entry.getKey()
                                        + ". = " + entry.getValue());
                            }
                            configuration.setSharedVariable(entry.getKey(),
                                    entry.getValue());
                        }

                        configuration
                                .setCustomAttribute(
                                        AbstractTagLibAwareConfigurer.CONFIGURED_ATTRIBUTE_NAME,
                                        Boolean.TRUE);
                    } catch (TemplateModelException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    @Override
    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    abstract protected GroovyObject getTagLibInstance(
            ApplicationContext springContext, String className);

}