/*
 * Copyright 2010 the original author or authors.
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
package freeApp

import freemarker.template.Configuration
import freemarker.template.Template
import grails.plugin.viewtools.GrailsWebEnvironment
import grails.test.mixin.integration.Integration
import org.grails.buffer.GrailsPrintWriter
import spock.lang.Specification


/**
 * @author Daniel Henrique Alves Lima
 */
@Integration
class TagLibAwareConfigurerTests extends Specification {

    def freeMarkerConfigurer
    def grailsApplication
    private GrailsPrintWriter sWriter = new GrailsPrintWriter (new StringWriter())
    private Exception threadException
    private ThreadGroup myThreadGroup = new ThreadGroup('x') {
        void uncaughtException(Thread t,Throwable e) {
            super.uncaughtException(t, e)
            threadException = e
        }
    }

    def setup() {
        GrailsWebEnvironment.bindRequestIfNull()
    }

    void testConfigReference() {
        when:
        assert  freeMarkerConfigurer != null
        then:
        freeMarkerConfigurer instanceof AbstractTagLibAwareConfigurer
    }

    void testAvailableTagLibs() {
        when:
        Configuration config = freeMarkerConfigurer.configuration
        Set names = config.getSharedVariableNames()
        then:
        names.contains('g')
         names.contains('plugin')
         names.contains('sitemesh')
    }

    void testParseRegularTemplate() {
        when:
        String result = parseFtlTemplate('[#ftl/]${s}', [s: 'ok'])
        then:
        'ok' == result
        when:
        result = parseFtlTemplate('<#ftl/>${s}', [s: 'fail'])
        then:
        'fail' == result
    }

    void testParseFmTagsTemplate() {
        /*FIXME: @g.form doesnt work, uncomment when fixed*/
        /*when:
        String result = parseFtlTemplate('[#ftl/][@g.form /]')
        then:
        result.contains('<form')
        result.contains('</form>')*/
        when:
        String result2 = parseFtlTemplate('[#ftl/]<a href="${g.message({\'code\': \'abc\', \'default\': \'xyz\'})}">')
        then:
        '<a href="xyz">' == result2
    }

    void testParseFmTagsTemplateWithoutRequestContext() {
        String result
        runInParallel {
            when:
            result = parseFtlTemplate('[#ftl/][@g.textField name="abc"/]')
            then:
            result.contains('<input type="text"')
            result.contains('name="abc"')

            try {
                when:
                result = parseFtlTemplate('[#ftl/][@g.message code="abc" default="xyz" /]')
                then:
                fail('This tag cannot run without a thread-bound request')
            }
            catch (e) {
                then:
                e.message.contains('No thread-bound request found:')
            }
        }
    }

    private parseFtlTemplate = { String templateSourceCode, Map binding = [:] ->
        if (sWriter.out.buffer.length() > 0) {sWriter.out.buffer.delete 0, sWriter.out.buffer.length()}
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.out.toString()
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
