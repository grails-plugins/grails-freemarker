package testing

import grails.test.mixin.integration.Integration
import spock.lang.Specification

@Integration
class GrailsFreeMarkerViewResolverTests extends Specification {

    def freeMarkerViewResolver

    void testGetView() {
        when:
        def view = freeMarkerViewResolver.resolveViewName("/demo/index.ftl",Locale.US)
        then:
        view //.getTemplate(Locale.US)
    }

}
