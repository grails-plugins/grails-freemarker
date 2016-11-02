package grails.plugin.freemarker

import grails.test.mixin.integration.Integration
import spock.lang.Specification

@Integration
class TaglibFromServiceTests extends Specification {

    def freeMarkerViewService
    def executorService

    void testGetView() {
        when:
        def view = freeMarkerViewService.getView("tagPlay/index.ftl")
        then:
        view //.getTemplate(Locale.US)
    }
}
