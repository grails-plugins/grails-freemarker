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

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import grails.core.GrailsApplication;
import grails.core.GrailsClass;
import grails.core.GrailsTagLibClass;
import grails.core.support.GrailsApplicationAware;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.grails.core.artefact.TagLibArtefactHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Henrique Alves Lima
 */
public abstract class AbstractTagLibAwareConfigurer extends FreeMarkerConfigurer implements GrailsApplicationAware {

    public static final String CONFIGURED_ATTRIBUTE_NAME = "_" + AbstractTagLibAwareConfigurer.class.getName() + ".configured";

    private final Log log = LogFactory.getLog(getClass());
    private GrailsApplication grailsApplication;

    @Override
    public void setConfiguration(Configuration configuration) {
        if (log.isDebugEnabled()) {
            log.debug("setConfiguration(): configuration " + configuration);
        }
        super.setConfiguration(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        Configuration configuration = super.getConfiguration();
        if (log.isDebugEnabled()) {
            log.debug("getConfiguration(): configuration " + configuration);
        }
        reconfigure(configuration);
        return configuration;
    }

    //called in onChange to reset the configuration
    protected void reconfigure() {
        Configuration configuration = super.getConfiguration();
        if (configuration != null) {
            synchronized (configuration) {
                configuration.removeCustomAttribute(CONFIGURED_ATTRIBUTE_NAME);
                reconfigure(configuration);
            }
        }
    }

    protected void reconfigure(Configuration configuration) {
        // TODO: Reconfiguration of reloaded tags
        if (configuration != null) {
            synchronized (configuration) {
                Boolean isConfigured = (Boolean) configuration.getCustomAttribute(CONFIGURED_ATTRIBUTE_NAME);
                if (isConfigured == null || isConfigured == false) {
                    try {
                        ApplicationContext springContext = grailsApplication.getMainContext();

                        Map<String, Map<String, TemplateModel>> sharedVars = new LinkedHashMap<String, Map<String, TemplateModel>>();
                        GrailsClass[] tagLibClasses = grailsApplication.getArtefacts(TagLibArtefactHandler.TYPE);

                        for (GrailsClass grailsClass : tagLibClasses) {
                            GrailsTagLibClass tagLibClass = (GrailsTagLibClass) grailsClass;
                            String namespace = tagLibClass.getNamespace();

                            if (log.isDebugEnabled()) {
                                log.debug("reconfigure(): Exposing tag '"+ namespace + "' ("+ tagLibClass.getFullName() + ")");
                            }

                            GroovyObject tagLibInstance = getTagLibInstance(springContext, tagLibClass.getFullName());
                            Map<String, TemplateModel> sharedVar = sharedVars.get(namespace);
                            if (sharedVar == null) {
                                sharedVar = new LinkedHashMap<String, TemplateModel>();
                                sharedVars.put(namespace, sharedVar);
                            }

                            Set<String> tagNamesWithReturn = tagLibClass.getTagNamesThatReturnObject();
                            if (tagNamesWithReturn == null) {
                                tagNamesWithReturn = Collections.emptySet();
                            }
                            else {
                                if (log.isDebugEnabled()) log.debug("found TagNamesThatReturnObject" + tagNamesWithReturn);
                            }

                            for (String tagName : tagLibClass.getTagNames()) {
                                if (log.isDebugEnabled()) log.debug("reconfigure(): tag " + tagName);
                                if (tagName.equals("grailsApplication")){
                                    continue;
                                }
                                Object tagInstanceObject = null;
                                try {
                                    tagInstanceObject = tagLibInstance.getProperty(tagName);
                                }
                                catch (IllegalStateException e) {
                                    //Workaround for properties exposed as tags and dependent of RequestAttributes
                                    log.debug("reconfigure() error on " + tagName, e);
                                }
                                if (tagInstanceObject != null && tagInstanceObject instanceof Closure) {
                                    Closure tagInstance = (Closure) tagInstanceObject;
                                    sharedVar.put(tagName,new TagLibToDirectiveAndFunction(
                                            namespace, tagLibInstance,
                                            tagName, tagInstance, tagNamesWithReturn.contains(tagName)));
                                }
                            }
                        }

                        for (Map.Entry<String, Map<String, TemplateModel>> entry : sharedVars.entrySet()) {
                            // if (log.isDebugEnabled()) log.debug("reconfigure(): @" + entry.getKey()+ ". = " + entry.getValue());
                            configuration.setSharedVariable(entry.getKey(),entry.getValue());
                        }

                        configuration.setCustomAttribute(AbstractTagLibAwareConfigurer.CONFIGURED_ATTRIBUTE_NAME,Boolean.TRUE);
                    }
                    catch (TemplateModelException e) {
                        throw new RuntimeException(e);
                    }
                }//end if
            }//end snchronized
        }//end if
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    protected abstract GroovyObject getTagLibInstance(ApplicationContext springContext, String className);
}
