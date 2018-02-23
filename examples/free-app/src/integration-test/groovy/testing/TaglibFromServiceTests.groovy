package testing

import grails.plugin.freemarker.FreeMarkerViewService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class TaglibFromServiceTests extends Specification {

    FreeMarkerViewService freeMarkerViewService
    def executorService

    void testGetView() {
        when:
        def view = freeMarkerViewService.getView("tagPlay/index.ftl")
        then:
        assert view  != null//.getTemplate(Locale.US)
    }
}
