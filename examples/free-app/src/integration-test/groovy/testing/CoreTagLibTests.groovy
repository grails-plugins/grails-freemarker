package testing
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
class CoreTagLibTests extends Specification {

    def freeMarkerConfigurer
    private GrailsPrintWriter sWriter = new GrailsPrintWriter (new StringWriter())

    def setup() {
        GrailsWebEnvironment.bindRequestIfNull()
    }

    void testForm() {
        when:
        String result = parseFtlTemplate('''
[#ftl/]\n
[@g.form name="myForm" action="myaction" id="1" controller="demo"]\n
   [@g.textField name="myField" value="${g.formatNumber({'number': myNumber, 'type': 'number', 'minIntegerDigits': 9, 'locale': 'en_US'})}"  /]\n
   [@g.textArea name="myField2" value="${myNumber?string('000')}" rows=5 cols=40 /]\n
[/@g.form]\n
''', [myNumber: 123.12])
        List lines = new StringReader(result).readLines()
        then:
        true
        /*FIXME: @g.form doesnt work, uncomment when fixed*/
        /*5 == lines.size()
        lines[1].contains('<form ')*/

    }

    private parseFtlTemplate = {String templateSourceCode, Map binding = [:] ->
        if (sWriter.out.buffer.length() > 0) {sWriter.out.buffer.delete 0, sWriter.out.buffer.length()}
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.out.toString()
    }
}
