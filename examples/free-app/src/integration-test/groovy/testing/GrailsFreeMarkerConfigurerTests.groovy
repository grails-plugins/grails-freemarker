package testing
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
import grails.test.mixin.integration.Integration
import grails.testing.mixin.integration.Integration
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig

import freemarker.template.Configuration
import freemarker.template.Template
import spock.lang.Specification

/**
 * @author Daniel Henrique Alves Lima
 */
@Integration
class GrailsFreeMarkerConfigurerTests extends Specification {

    def freeMarkerConfigurer
    private StringWriter sWriter = new StringWriter()
    private Exception threadException
    private ThreadGroup myThreadGroup = new ThreadGroup('x') {
        void uncaughtException(Thread t,Throwable e) {
            super.uncaughtException(t, e)
            threadException = e
        }
    }

    void testConfigReference() {
        when:
        freeMarkerConfigurer != null
        then:
        freeMarkerConfigurer instanceof FreeMarkerConfig
        freeMarkerConfigurer.configuration == freeMarkerConfigurer.configuration
    }

    void testParseRegularTemplate() {
        when:
        String result = parseFtlTemplate('[#ftl/]${s}', [s: 'ok'])
        then: 'ok' == result

        when:
        result = parseFtlTemplate('<#ftl/>${s}', [s: 'fail'])
        then:
        'fail' == result
    }

    void testParseRegularTemplateWithoutRequestContext() {
        runInParallel {
            when:
            String result = parseFtlTemplate('[#ftl/]<input type="text" name="${xyz}"/>', [xyz: 'abc'])
            then:
            result.contains('<input type="text"')
            result.contains('name="abc"')
        }
    }

    void testParseRegularView() {
        when:
        String result = parseFtlView('/demo/fmtemplate.ftl', [name: 'fmView'])
        then:
        result.contains('fmView')

        //result = parseFtlView('/demo/bluesky.ftl', [testvar: 'weirdValue'])
        //assertTrue result.contains('weirdValue')
    }

    // void testParseRegularViewWithoutRequestContext() {
    //     runInParallel {
    //         String result = parseFtlView('/demo/fmtemplate.ftl', [name: 'fmView123'])
    //         assertTrue result.contains('fmView123')
    //     }
    // }

    private parseFtlView = {String viewPath, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {
            sWriter.buffer.delete 0, sWriter.buffer.length()
        }
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = cfg.getTemplate(viewPath)
        assert template
        template.process(binding, sWriter)
        return sWriter.toString()
    }

    private parseFtlTemplate = {String templateSourceCode, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {
            sWriter.buffer.delete 0, sWriter.buffer.length()
        }
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.toString()
    }

    private runInParallel = {Closure c ->
        if (threadException) {
            throw threadException
        }

        def thread
        boolean executed = false
        Closure c1 = {
            try {
                c()
            }
            finally {
                executed = true
            }
        }

        thread = new Thread(myThreadGroup, c1 as Runnable)
        thread.daemon = true
        thread.start()
        thread.yield()

        while (thread.isAlive() && !executed) {
            Thread.sleep 1000
        }
        if (threadException) {
            throw threadException
        }
    }
}
