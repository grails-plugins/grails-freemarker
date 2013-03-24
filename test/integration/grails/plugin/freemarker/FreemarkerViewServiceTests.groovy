package grails.plugin.freemarker

class FreemarkerViewServiceTests extends GroovyTestCase {

    FreemarkerViewService freemarkerViewService

    void testGetView() {
        def view = freemarkerViewService.getView("demo/index")
        assertNotNull view //.getTemplate(Locale.US)
    }

    void testGetViewPlugin() {
        def view = freemarkerViewService.getView("pluginTest/itWorks.ftl","freemarker-plugin-test")
        assertNotNull view //.getTemplate(Locale.US)
    }

    void testRenderName() {
        def writer = new StringWriter()
        freemarkerViewService.render("demo/index" , [name:"basejump",state:"IL"],  writer)
        println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin() {
        def writer = new StringWriter()
        freemarkerViewService.render("gobaby" , [testvar:"basejump"],  writer, "freemarker-plugin-test")
        println writer.toString()
        assertTrue writer.toString().contains("<p>basejump</p>")
    }

    void testRenderInThisPugin() {
        def writer = new StringWriter()
        freemarkerViewService.render("/demo/index" , [name:"basejump",state:"IL"],  writer, "freemarker")
        println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }
}
