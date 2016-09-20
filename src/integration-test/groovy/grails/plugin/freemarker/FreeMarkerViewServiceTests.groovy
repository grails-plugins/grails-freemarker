package grails.plugin.freemarker

class FreeMarkerViewServiceTests extends GroovyTestCase {

    FreeMarkerViewService freeMarkerViewService

    void testGetView() {
        def view = freeMarkerViewService.getView("/demo/index.ftl")
        assert view //.getTemplate(Locale.US)
    }

    void testGetViewPlugin() {
        def view = freeMarkerViewService.getView("/pluginTest/itWorks.ftl","free-plugin")
        assert view //.getTemplate(Locale.US)
    }

    void testRenderName() {
        def writer = new StringWriter()
        freeMarkerViewService.render("demo/index.ftl" , [name:"basejump", state:"IL"],  writer)
        //println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin() {
        def writer = new StringWriter()
        freeMarkerViewService.render("gobaby.ftl" , [testvar:"basejump"],  writer )
        //println writer.toString()
        assertTrue writer.toString().contains("<p>basejump</p>")
    }

    void testRenderInThisPugin() {
        def writer = new StringWriter()
        freeMarkerViewService.render("/demo/index.ftl" , [name:"basejump", state:"IL"],  writer)
        println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }

    void testRender_file_in_test_dir() {
        def writer = new StringWriter()
        freeMarkerViewService.render("file:test/views/foo.ftl" ,[:],  writer)
        println writer.toString()
        assertTrue writer.toString().contains("sitting here in the test")
    }
}
