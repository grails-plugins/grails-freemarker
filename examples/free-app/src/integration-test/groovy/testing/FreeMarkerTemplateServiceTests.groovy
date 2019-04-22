package testing

import grails.plugin.freemarker.FreeMarkerTemplateService
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * @author Joshua Burnett
 */
@Integration
class FreeMarkerTemplateServiceTests extends Specification {

    FreeMarkerTemplateService freeMarkerTemplateService

    private String suffix = ".ftl"

    void testGetView() {
        when:
        def view = freeMarkerTemplateService.getTemplate("demo/index${suffix}")
        then:
        view //.getTemplate(Locale.US)
    }

    void testGetViewPlugin() {
        when:
        def view = freeMarkerTemplateService.getTemplate("pluginTest/itWorks${suffix}", "free-plugin")
        then:
        view //.getTemplate(Locale.US)
    }

    void testRenderName() {
        when:
        def writer = new StringWriter()
        freeMarkerTemplateService.render("demo/index${suffix}", [name: "basejump", state: "IL"], writer)
        println writer.toString()
        then:
        writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin() {
        when:
        def writer = new StringWriter()
        freeMarkerTemplateService.render("gobaby${suffix}", [testvar: "basejump"], writer, "free-plugin")
        println writer.toString()
        then:
        writer.toString().contains("<p>basejump</p>")
    }
}
