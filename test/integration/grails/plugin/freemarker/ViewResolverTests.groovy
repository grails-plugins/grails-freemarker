package grails.plugin.freemarker

import grails.test.*

class ViewResolverTests extends GroovyTestCase {
	def freemarkerViewResolver
    
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetView() {
		def view = freemarkerViewResolver.getView('demo/fmtemplate')
		assertNotNull view
    }

	void testGetViewPlugin() {
		def view = freemarkerViewResolver.getView('gobaby',"freemarker-plugin-test")
		assertNotNull view
		def writer = new StringWriter()
		view.renderToWriter([testvar:'test var'],  writer)
		assertTrue writer.toString().contains('test var')
		assertTrue writer.toString().contains('This is a ftl from a plugin')
    }

}
