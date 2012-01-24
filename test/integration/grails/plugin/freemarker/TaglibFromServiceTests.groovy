package grails.plugin.freemarker

import grails.test.*
import org.springframework.web.context.request.RequestContextHolder

class TaglibFromServiceTests extends GroovyTestCase {
    def freemarkerViewService
	def executorService
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetView() {
        def view = freemarkerViewService.getView("tagPlay/index")
        assertNotNull view //.getTemplate(Locale.US)
        
    }

}
