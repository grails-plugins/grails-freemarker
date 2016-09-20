package grails.plugin.freemarker

class TaglibFromServiceTests extends GroovyTestCase {

    def freeMarkerViewService
    def executorService

    void testGetView() {
        def view = freeMarkerViewService.getView("tagPlay/index.ftl")
        assertNotNull view //.getTemplate(Locale.US)
    }
}
