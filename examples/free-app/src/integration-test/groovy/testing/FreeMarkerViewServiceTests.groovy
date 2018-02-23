package testing

import grails.plugin.freemarker.FreeMarkerViewService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class FreeMarkerViewServiceTests extends Specification {

    FreeMarkerViewService freeMarkerViewService
    def freeMarkerViewResourceLocator

    void testGetView() {
        when:
        def res = freeMarkerViewResourceLocator.locate('demo/index.ftl')
        then:
        res
        res.getURI().toString().endsWith("demo/index.ftl")
        when:
        def view = freeMarkerViewService.getView("/demo/index.ftl")
        then:
        view //.getTemplate(Locale.US)
    }
//
//    void testGetViewPlugin() {
//        def view = freeMarkerViewService.getView("/pluginTest/itWorks.ftl","free-plugin")
//        assert view //.getTemplate(Locale.US)
//    }

    void testRenderName() {
        when:
        def writer = new StringWriter()
        freeMarkerViewService.render("demo/index.ftl" , [name:"basejump", state:"IL"],  writer)
        //println writer.toString()
        then:
        writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin() {
        when:
        def writer = new StringWriter()
        freeMarkerViewService.render("gobaby.ftl" , [testvar:"basejump"],  writer )
        //println writer.toString()
        then:
        writer.toString().contains("<p>basejump</p>")
    }

    void testRenderInThisPugin() {
        when:
        def writer = new StringWriter()
        freeMarkerViewService.render("/demo/index.ftl" , [name:"basejump", state:"IL"],  writer)
        println writer.toString()
        then:
        writer.toString().contains("Name: basejump")
    }

    void testRender_file_in_test_dir() {
        when:
        def writer = new StringWriter()
        freeMarkerViewService.render("file:../views/foo.ftl" ,[:],  writer)
        println writer.toString()
        then:
        writer.toString().contains("sitting here in the test")
    }
}
