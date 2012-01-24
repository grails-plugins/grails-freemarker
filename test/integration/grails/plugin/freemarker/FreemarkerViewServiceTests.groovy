package grails.plugin.freemarker

import grails.test.*

class FreemarkerViewServiceTests extends GroovyTestCase {
    FreemarkerViewService freemarkerViewService
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetView() {
        def view = freemarkerViewService.getView("demo/index")
        assertNotNull view //.getTemplate(Locale.US)
        
    }
    
    void testGetViewPlugin() {
        def view = freemarkerViewService.getView("pluginTest/itWorks.ftl","freemarker-plugin-test")
        assertNotNull view //.getTemplate(Locale.US)
    }

    void testRenderName(){
        def writer = new StringWriter()
        freemarkerViewService.render("demo/index" , [name:"basejump",state:"IL"],  writer)
        println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }
    
    void testRenderNamePlugin(){
        def writer = new StringWriter()
        freemarkerViewService.render("gobaby" , [testvar:"basejump"],  writer, "freemarker-plugin-test")
        println writer.toString()
        assertTrue writer.toString().contains("<p>basejump</p>")
    }
    
    


}
