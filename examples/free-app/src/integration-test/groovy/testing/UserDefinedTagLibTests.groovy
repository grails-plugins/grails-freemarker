package testing

import freemarker.template.Configuration

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
import freemarker.template.Template
import grails.plugin.viewtools.GrailsWebEnvironment
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * @author Daniel Henrique Alves Lima
 */
@Integration
class UserDefinedTagLibTests extends Specification {

    def freeMarkerConfigurer
    private StringWriter sWriter = new StringWriter()

    def setup() {
        GrailsWebEnvironment.bindRequestIfNull()
    }

    void testEmoticon() {
        when:
        String result = parseFtlTemplate('[#ftl/][@g.emoticon happy="true"]Hi John[/@g.emoticon]')
        then:
        'Hi John :-)' == result

        when:
        result = parseFtlTemplate('[#ftl/][@g.emoticon happy="false"]Hi Mary[/@g.emoticon]')
        then:'Hi Mary :-(' == result

        when:
        result = parseFtlTemplate('[#ftl/][@g.emoticon happy="false"/]')
        then: ' :-(' == result

        when:
        result = parseFtlTemplate('[#ftl/]<x value="${g.emoticon({\'happy\': \'true\'})}">')
        then:
        '<x value=" :-)">' == result //??????
    }

    void testDateFormat() {
        when:
        Map binding = [now: new GregorianCalendar(2011, Calendar.JUNE, 30).time]
        String result = parseFtlTemplate('[#ftl/][@g.dateFormat format="dd-MM-yyyy" date=now /]', binding)
        then:
        '30-06-2011' == result

        when:
        result = parseFtlTemplate('[#ftl/]${now?string("dd-MM-yyyy")}', binding)
        then:
        '30-06-2011' == result

        when:
        result= parseFtlTemplate('[#ftl/]a=${g.dateFormat({"format": "dd-MM-yyyy", "date":now})}', binding)
        then:
        'a=30-06-2011' == result
    }

    void testIsAdmin() {
        when:
        String template = '[#ftl/][@g.isAdmin user=myUser]// some restricted content[/@g.isAdmin]'
        Map model = [myUser: [admin: true]]

        String result = parseFtlTemplate(template, model)
        then: '// some restricted content' == result

        when:
        model.myUser.admin = false
        result = parseFtlTemplate(template, model)
        then:
        '' == result
    }

    void testRepeat() {
        when:
        String result = parseFtlTemplate('[#ftl/][@g.repeat times=3]\
<p>Repeat this 3 times! Current repeat = ${it}</p>\
[/@g.repeat]')
        then:
        '<p>Repeat this 3 times! Current repeat = 0</p><p>Repeat this 3 times! Current repeat = 1</p><p>Repeat this 3 times! Current repeat = 2</p>' == result
        when:
        result = parseFtlTemplate('[#ftl/][@g.repeat times=3]A${it}[@g.repeat times=2]B${it}[/@g.repeat][/@g.repeat]')
        then:
        'A0B0B1A1B0B1A2B0B1' == result
    }

    void testMyRepeat() {
        when:
        String result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="j"]\
<p>Repeat this 3 times! Current repeat = ${j}</p>\
[/@my.repeat]')
        then:
        '<p>Repeat this 3 times! Current repeat = 0</p><p>Repeat this 3 times! Current repeat = 1</p><p>Repeat this 3 times! Current repeat = 2</p>' == result

        when:
        result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="j"]A${j}[@my.repeat times=2 var="j"]B${j}[/@my.repeat][/@my.repeat]')
        then: 'A0B0B1A1B0B1A2B0B1' == result

        when:
        result = parseFtlTemplate('[#ftl/][@my.repeat times=3 var="i"]A${i}[@my.repeat times=2 var="j"]B${i}${j}[/@my.repeat][/@my.repeat]')
        then:
        'A0B00B01A1B10B11A2B20B21' == result
    }

    void testMySum() {
        when:
        String result = parseFtlTemplate('[#ftl/][@my.sum a=1 b=2 c=876/]')
        then:
        '879' == result
        when:
        result = parseFtlTemplate('[#ftl/][#assign result=my.sum({"a":1, "b":2, "c":876}) /]${result?string("000.00")}')
        then:
        '879.00' == result
    }

    void testMyAnotherExample() {
        when:
        String result = parseFtlTemplate('[#ftl/][@my.anotherExample /]')
        then:
        'example another' == result
    }

    void testMySpecialForm() {
        when:
        String result = parseFtlTemplate('[#ftl/][@my.specialForm /]')
        then:
        result.contains('<form ')
    }

    void testTagBody_unsingReverse() {
        when:
        String result = parseFtlTemplate('[#ftl/][@g.reverse]Hi Sam[/@g.reverse]');
        then:
        'maS iH' == result
    }

    private parseFtlTemplate = {String templateSourceCode, Map binding = [:] ->
        if (sWriter.buffer.length() > 0) {sWriter.buffer.delete 0, sWriter.buffer.length()}
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(templateSourceCode), cfg)
        template.process (binding, sWriter)
        return sWriter.toString()
    }
}
