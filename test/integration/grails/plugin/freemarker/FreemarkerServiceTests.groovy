package grails.plugin.freemarker

import grails.test.*

class FreemarkerServiceTests extends GroovyTestCase {
	def freemarkerService
    
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGetView() {
		def view = freemarkerService.getView("demo/index")
		assertNotNull view //.getTemplate(Locale.US)
		
	}
	
	void testGetViewPlugin() {
		def view = freemarkerService.getView("pluginTest/itWorks","freemarker-plugin-test")
		assertNotNull view //.getTemplate(Locale.US)
	}

	void testRenderName(){
		def writer = new StringWriter()
		freemarkerService.render("demo/index" , [name:"basejump",state:"IL"],  writer)
		println writer.toString()
		assertTrue writer.toString().contains("Name: basejump")
	}
	
	void testRenderNamePlugin(){
		def writer = new StringWriter()
		freemarkerService.render("gobaby" , [testvar:"basejump"],  writer, "freemarker-plugin-test")
		println writer.toString()
		assertTrue writer.toString().contains("<p>basejump</p>")
	}
	
	void testRenderStringPlugin(){
		def res = freemarkerService.renderString("gobaby" , [testvar:"basejump"], "freemarker-plugin-test")
		println res
		assertTrue res.contains("<p>basejump</p>")
	}
	
	void testRenderString(){
		def res = freemarkerService.renderString("demo/index" , [name:"basejump",state:"IL"])
		println res
		assertTrue res.contains("Name: basejump")
	}


}
