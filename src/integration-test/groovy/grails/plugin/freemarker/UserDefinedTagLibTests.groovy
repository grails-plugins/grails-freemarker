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
class UserDefinedTagLibTests extends GroovyTestCase {

    def freeMarkerConfigurer
    private StringWriter sWriter = new StringWriter()

    void testEmoticon() {
        String result = parseFtlTemplate('[#ftl/][@g.emoticon happy="true"]Hi John[/@g.emoticon]')
        assertEquals 'Hi John :-)', result

        result = parseFtlTemplate('[#ftl/][@g.emoticon happy="false"]Hi Mary[/@g.emoticon]')
        assertEquals 'Hi Mary :-(', result

        result = parseFtlTemplate('[#ftl/][@g.emoticon happy="false"/]')
        assertEquals ' :-(', result

        result = parseFtlTemplate('[#ftl/]<x value="${g.emoticon({\'happy\': \'true\'})}">')
        assertEquals '<x value=" :-)">', result //??????
    }

    void testDateFormat() {
        Map binding = [now: new GregorianCalendar(2011, Calendar.JUNE, 30).time]
        String result = parseFtlTemplate('[#ftl/][@g.dateFormat format="dd-MM-yyyy" date=now /]', binding)
        assertEquals '30-06-2011', result

        result = parseFtlTemplate('[#ftl/]${now?string("dd-MM-yyyy")}', binding)
        assertEquals '30-06-2011', result

        result= parseFtlTemplate('[#ftl/]a=${g.dateFormat({"format": "dd-MM-yyyy", "date":now})}', binding)
        assertEquals 'a=30-06-2011', result
    }

    void testIsAdmin() {
        String template = '[#ftl/][@g.isAdmin user=myUser]// some restricted content[/@g.isAdmin]'
        Map model = [myUser: [admin: true]]

        String result = parseFtlTemplate(template, model)
        assertEquals '// some restricted content', result

        model.myUser.admin = false
        result = parseFtlTemplate(template, model)
        assertEquals '', result
    }

    void testRepeat() {
        String result = parseFtlTemplate('[#ftl/][@g.repeat times=3]\
<p>Repeat this 3 times! Current repeat = ${it}</p>\
[/@g.repeat]')
        assertEquals '<p>Repeat this 3 times! Current repeat = 0</p><p>Repeat this 3 times! Current repeat = 1</p><p>Repeat this 3 times! Current repeat = 2</p>', result

        result = parseFtlTemplate('[#ftl/][@g.repeat times=3]A${it}[@g.repeat times=2]B${it}[/@g.repeat][/@g.repeat]')
        assertEquals 'A0B0B1A1B0B1A2B0B1', result
    }

    void testMyRepeat() {
        String result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="j"]\
<p>Repeat this 3 times! Current repeat = ${j}</p>\
[/@my.repeat]')
        assertEquals '<p>Repeat this 3 times! Current repeat = 0</p><p>Repeat this 3 times! Current repeat = 1</p><p>Repeat this 3 times! Current repeat = 2</p>', result

        result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="j"]A${j}[@my.repeat times=2 var="j"]B${j}[/@my.repeat][/@my.repeat]')
        assertEquals 'A0B0B1A1B0B1A2B0B1', result

        result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="i"]A${i}[@my.repeat times=2 var="j"]B${i}${j}[/@my.repeat][/@my.repeat]')
        assertEquals 'A0B00B01A1B10B11A2B20B21', result
    }

    void testMySum() {
        String result = parseFtlTemplate('[#ftl/][@my.sum a=1 b=2 c=876/]')
        assertEquals '879', result

        result = parseFtlTemplate('[#ftl/][#assign result=my.sum({"a":1, "b":2, "c":876}) /]${result?string("000.00")}')
        assertEquals '879.00', result
    }

    void testMyAnotherExample() {
        String result = parseFtlTemplate('[#ftl/][@my.anotherExample /]')
        assertEquals 'example another', result
    }

    void testMySpecialForm() {
        String result = parseFtlTemplate('[#ftl/][@my.specialForm /]')
        assertTrue result.contains('<form ')
    }

    void testTagBody_unsingReverse() {
        String result = parseFtlTemplate('[#ftl/][@g.reverse]Hi Sam[/@g.reverse]');
        assert 'maS iH' == result
    }

    private parseFtlTemplate = {String templateSourceCode, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {sWriter.buffer.delete 0, sWriter.buffer.length()}
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.toString()
    }
}
