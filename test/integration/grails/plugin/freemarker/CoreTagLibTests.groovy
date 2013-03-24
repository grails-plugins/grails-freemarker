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
class CoreTagLibTests extends GroovyTestCase {

    def freemarkerConfig
    private StringWriter sWriter = new StringWriter()

    void testForm() {
        String result = parseFtlTemplate('''
[#ftl/]\n
[@g.form name="myForm" action="myaction" id="1"]\n
   [@g.textField name="myField" value="${g.formatNumber({'number': myNumber, 'type': 'number', 'minIntegerDigits': 9, 'locale': 'en_US'})}"  /]\n
   [@g.textArea name="myField2" value="${myNumber?string('000')}" rows=5 cols=40 /]\n
[/@g.form]\n
''', [myNumber: 123.12])

        List lines = new StringReader(result).readLines()
        assertEquals 5, lines.size()

        println lines
        assertTrue "Unexpected '${lines[1]}'", lines[1].contains('<form ')
        assertTrue "Unexpected '${lines[1]}'", lines[1].contains('name="myForm"')
        assertTrue "Unexpected '${lines[2]}'", lines[2].contains('<input type="text" name="myField"')
        assertTrue "Unexpected '${lines[2]}'", lines[2].contains('000,000,123')
        assertTrue "Unexpected '${lines[3]}'", lines[3].contains('<textarea ')
        assertTrue "Unexpected '${lines[3]}'", lines[3].contains('123')
        assertTrue "Unexpected '${lines[4]}'", lines[4].contains('</form>')
    }

    private parseFtlTemplate = {String templateSourceCode, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {sWriter.buffer.delete 0, sWriter.buffer.length()}
        Configuration cfg = freemarkerConfig.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.toString()
    }
}
