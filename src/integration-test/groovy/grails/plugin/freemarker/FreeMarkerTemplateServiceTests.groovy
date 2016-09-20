package grails.plugin.freemarker

/**
 * @author Joshua Burnett
 */
class FreeMarkerTemplateServiceTests extends GroovyTestCase {

    FreeMarkerTemplateService freeMarkerTemplateService

    private String suffix = ".ftl"

    void testGetView() {
        def view = freeMarkerTemplateService.getTemplate("demo/index${suffix}")
        assert view //.getTemplate(Locale.US)
    }

    void testGetViewPlugin() {
        def view = freeMarkerTemplateService.getTemplate("pluginTest/itWorks${suffix}","free-plugin")
        assert view //.getTemplate(Locale.US)
    }

    void testRenderName() {
        def writer = new StringWriter()
        freeMarkerTemplateService.render("demo/index${suffix}" , [name:"basejump", state:"IL"], writer)
        println writer.toString()
        assert writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin() {
        def writer = new StringWriter()
        freeMarkerTemplateService.render("gobaby${suffix}" , [testvar:"basejump"], writer, "free-plugin")
        println writer.toString()
        assert writer.toString().contains("<p>basejump</p>")
    }
}
