package grails.plugin.freemarker

class GrailsFreeMarkerViewResolverTests extends GroovyTestCase {

    def freeMarkerViewResolver

    void testGetView() {
        def view = freeMarkerViewResolver.resolveViewName("/demo/index.ftl",Locale.US)
        assert view //.getTemplate(Locale.US)
    }

}
