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

import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.grails.buffer.GrailsPrintWriter;
import org.grails.taglib.GroovyPageAttributes;
import org.grails.web.sitemesh.GrailsRoutablePrintWriter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author Daniel Henrique Alves Lima
 */
public class TagLibToDirectiveAndFunction implements TemplateDirectiveModel, TemplateMethodModelEx {

    private static final Map<String, String> RESERVED_WORDS_TRANSLATION;
    private final Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("serial")
    private static final Closure EMPTY_BODY = new Closure(TagLibToDirectiveAndFunction.class) {
        @SuppressWarnings("unused")
        public Object doCall(Object[] it) throws IOException, TemplateException {
            return "";
        }
    };

    static {
        Map<String, String> m = new LinkedHashMap<String, String>();
        m.put("as", "_as");
        RESERVED_WORDS_TRANSLATION = Collections.unmodifiableMap(m);
    }

    private String namespace;
    @SuppressWarnings("unused")
    private GroovyObject tagLibInstance;
    private String tagName;
    private Closure tagInstance;
    private boolean hasReturnValue;

    public TagLibToDirectiveAndFunction(String namespace,
            GroovyObject tagLibInstance, String tagName, Closure tagInstance,
            boolean hasReturnValue) {
        this.namespace = namespace;
        this.tagLibInstance = tagLibInstance;
        this.tagName = tagName;
        this.tagInstance = tagInstance;
        this.hasReturnValue = hasReturnValue;
    }

    @SuppressWarnings("serial")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (log.isDebugEnabled()) {
            log.debug("exec(): @" + namespace + "." + tagName);
        }
        try {
            CharArrayWriter writer = new CharArrayWriter();
            tagInstance.invokeMethod("pushOut", writer);

            Object args = null;
            Object body = null;
            if (arguments != null) {
                if (arguments.size() > 0) {
                    args = arguments.get(0);
                }
                if (arguments.size() > 1) {
                    body = arguments.get(1);
                }
            }

            Object result = null;
            args = args != null ? unwrapParams((TemplateHashModelEx) args, true) : unwrapParams(Collections.EMPTY_MAP, true);

            if (tagInstance.getMaximumNumberOfParameters() == 1) {
                result = tagInstance.call(args);
            }
            else {
                Closure bodyClosure = EMPTY_BODY;

                if (body != null || hasReturnValue) {
                    final Object fBody = body;
                    bodyClosure = new Closure(this) {
                        @SuppressWarnings("unused")
                        public Object doCall(Object it) throws IOException,TemplateException {
                            return fBody;
                        }
                    };
                }

                result = tagInstance.call(new Object[] { args, bodyClosure });
                if (result == null) {
                    // writer.flush();
                    result = writer.toString();
                }
            }

            return result;
        }
        catch (RuntimeException e) {
            throw new TemplateModelException(e);
        }
        finally {
            tagInstance.invokeMethod("popOut", null);
        }
    }

    @SuppressWarnings("serial")
    @Override
    public void execute(final Environment env,@SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            final TemplateDirectiveBody body) throws TemplateException,IOException {
        try {

            Writer wout = env.getOut();
            if (wout instanceof GrailsRoutablePrintWriter){
                wout = ((GrailsRoutablePrintWriter)wout).getOut();
            }

            tagInstance.invokeMethod("pushOut", wout);

            params = unwrapParams(params, true);
            if (log.isDebugEnabled()) {
                log.debug("wout is "+ wout.getClass());
                log.debug("execute(): @" + namespace + "." + tagName);
                log.debug("execute(): params " + params);
                log.debug("execute(): body " + body);
                log.debug("hasReturnValue " + hasReturnValue);
            }
            Object result = null;
            if (tagInstance.getMaximumNumberOfParameters() == 1) {
                
                result = tagInstance.call(params);
            }
            else {
                Closure bodyClosure = EMPTY_BODY;

                if (body != null) {
                    bodyClosure = new Closure(this) {

                        @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
                        public Object doCall(Object it) throws IOException,TemplateException {
                            ObjectWrapper objectWrapper = env.getObjectWrapper();
                            Map<String, TemplateModel> oldVariables = null;
                            TemplateModel oldIt = null;
                            StringWriter bodyOutput = new StringWriter();

                            if (log.isDebugEnabled()) {
                                log.debug("doCall it " + it);
                            }

                            boolean itIsAMap = false;
                            if (it != null) {
                                if (it instanceof Map) {
                                    itIsAMap = true;
                                    oldVariables = new LinkedHashMap<String, TemplateModel>();
                                    Map<String, Object> itMap = (Map) it;
                                    for (Map.Entry<String, Object> entry : itMap.entrySet()) {
                                        oldVariables.put(entry.getKey(), env.getVariable(entry.getKey()));
                                    }
                                }
                                else {
                                    oldIt = env.getVariable("it");
                                }
                            }

                            try {
                                if (it != null) {
                                    if (itIsAMap) {
                                        Map<String, Object> itMap = (Map) it;
                                        for (Map.Entry<String, Object> entry : itMap.entrySet()) {
                                            env.setVariable(entry.getKey(),objectWrapper.wrap(entry.getValue()));
                                        }
                                    }
                                    else {
                                        env.setVariable("it",objectWrapper.wrap(it));
                                    }
                                }
                                //Writer wout = (Writer) tagInstance.getProperty("out");

                                body.render(new GrailsPrintWriter(bodyOutput));
                            }
                            finally {
                                if (oldVariables != null) {
                                    for (Map.Entry<String, TemplateModel> entry : oldVariables.entrySet()) {
                                        env.setVariable(entry.getKey(),entry.getValue());
                                    }
                                }
                                else if (oldIt != null) {
                                    env.setVariable("it", oldIt);
                                }
                            }

                            //return "";
                            return bodyOutput.getBuffer().toString();
                        }
                    };
                }

                result = tagInstance.call(new Object[] { params, bodyClosure });
            }

            if (log.isDebugEnabled()) {
                log.debug("hasReturnValue " + hasReturnValue);
                //log.debug("result " + result);
            }
            //FIXME this used to check for hasReturnValue but since I can't get out passed in right then I always append the result
            if (result != null && hasReturnValue) {
                //if (result != null) {
                env.getOut().append(result.toString());
            }
        }
        catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new TemplateException(e, env);
        }
        finally {
            tagInstance.invokeMethod("popOut", null);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Map unwrapParams(TemplateHashModelEx params,
            Boolean translateReservedWords) throws TemplateModelException {

        if (translateReservedWords == null) {
            translateReservedWords = true;
        }

        Map unwrappedParams = new GroovyPageAttributes(new LinkedHashMap());

        TemplateModelIterator keys = params.keys().iterator();
        while (keys.hasNext()) {
            String oldKey = keys.next().toString();
            Object value = params.get(oldKey);
            if (value != null) {
                value = DeepUnwrap.permissiveUnwrap((TemplateModel) value);
            }

            String key = null;
            if (translateReservedWords) {
                key = RESERVED_WORDS_TRANSLATION.get(oldKey);
            }
            if (key == null) {
                key = oldKey;
            }

            unwrappedParams.put(key, value);
        }

        return unwrappedParams;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Map unwrapParams(Map params, Boolean translateReservedWords)
            throws TemplateModelException {
        if (translateReservedWords == null) {
            translateReservedWords = true;
        }

        Map unwrappedParams = new GroovyPageAttributes(new LinkedHashMap());
        Iterator<Map.Entry> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            Object value = entry.getValue();
            if (value != null) {
                value = DeepUnwrap.permissiveUnwrap((TemplateModel) value);
            }

            String key = null;
            if (translateReservedWords) {
                key = RESERVED_WORDS_TRANSLATION.get(entry.getKey());
            }
            if (key == null) {
                key = (String) entry.getKey();
            }

            unwrappedParams.put(key, value);
        }

        return unwrappedParams;
    }
}
