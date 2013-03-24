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
package grails.plugin.freemarker

import freemarker.template.Configuration
import freemarker.template.Template

/**
 * @author Daniel Henrique Alves Lima
 */
class TagLibAwareConfigurerTests extends GroovyTestCase {

    def freemarkerConfig
    def grailsApplication
    private StringWriter sWriter = new StringWriter()
    private Exception threadException
    private ThreadGroup myThreadGroup = new ThreadGroup('x') {
        void uncaughtException(Thread t,Throwable e) {
            super.uncaughtException(t, e)
            threadException = e
        }
    }

    void testConfigReference() {
        assertNotNull freemarkerConfig
        assertTrue freemarkerConfig instanceof AbstractTagLibAwareConfigurer
    }

    void testAvailableTagLibs() {
        Configuration config = freemarkerConfig.configuration
        Set names = config.getSharedVariableNames()
        assertTrue names.contains('g')
        assertTrue names.contains('plugin')
        assertTrue names.contains('sitemesh')
    }

    void testParseRegularTemplate() {
        String result = parseFtlTemplate('[#ftl/]${s}', [s: 'ok'])
        assertEquals 'ok', result

        result = parseFtlTemplate('<#ftl/>${s}', [s: 'fail'])
        assertEquals 'fail', result
    }

    void testParseFmTagsTemplate() {
        String result = parseFtlTemplate('[#ftl/][@g.form /]')
        assertTrue result, result.contains('<form')
        assertTrue result, result.contains('</form>')

        result = parseFtlTemplate('[#ftl/]<a href="${g.message({\'code\': \'abc\', \'default\': \'xyz\'})}">')
        assertEquals '<a href="xyz">', result
    }

    void testParseFmTagsTemplateWithoutRequestContext() {
        runInParallel {
            String result = parseFtlTemplate('[#ftl/][@g.textField name="abc"/]')
            assertTrue result, result.contains('<input type="text"')
            assertTrue result, result.contains('name="abc"')

            try {
                result = parseFtlTemplate('[#ftl/][@g.message code="abc" default="xyz" /]')
                fail('This tag cannot run without a thread-bound request')
            }
            catch (e) {
                assertTrue e.message.contains('No thread-bound request found:')
            }
        }
    }

    private parseFtlTemplate = { String templateSourceCode, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {sWriter.buffer.delete 0, sWriter.buffer.length()}
        Configuration cfg = freemarkerConfig.configuration
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
