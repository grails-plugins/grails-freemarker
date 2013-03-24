package grails.plugin.freemarker

class TaglibFromServiceTests extends GroovyTestCase {

    def freemarkerViewService
    def executorService

    void testGetView() {
        def view = freemarkerViewService.getView("tagPlay/index")
        assertNotNull view //.getTemplate(Locale.US)
    }
}
