package grails.plugin.freemarker

class FreeMarkerViewServiceTests extends GroovyTestCase {

    FreeMarkerViewService freeMarkerViewService
    def freeMarkerViewResourceLocator

    @spock.lang.IgnoreRest
    void testGetView() {
        def res = freeMarkerViewResourceLocator.locate('demo/index.ftl')
        assert res

        assert res.getURI().toString().endsWith("demo/index.ftl")

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
        freeMarkerViewService.render("file:test-projects/views/foo.ftl" ,[:],  writer)
        println writer.toString()
        assertTrue writer.toString().contains("sitting here in the test")
    }
}
