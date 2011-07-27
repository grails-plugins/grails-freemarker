package grails.plugin.freemarker

import grails.test.*


/**
 *
 * @author Joshua Burnett
 *
 */
class FreemarkerTemplateServiceTests extends GroovyTestCase {
    FreemarkerTemplateService freemarkerTemplateService
    private String suffix = ".ftl"

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetView() {
        def view = freemarkerTemplateService.getTemplate("demo/index${suffix}")
        assertNotNull view //.getTemplate(Locale.US)

    }

    void testGetViewPlugin() {
        def view = freemarkerTemplateService.getTemplate("pluginTest/itWorks${suffix}","freemarker-plugin-test")
        assertNotNull view //.getTemplate(Locale.US)
    }

    void testRenderName(){
        def writer = new StringWriter()
        freemarkerTemplateService.render("demo/index${suffix}" , [name:"basejump",state:"IL"], writer)
        println writer.toString()
        assertTrue writer.toString().contains("Name: basejump")
    }

    void testRenderNamePlugin(){
        def writer = new StringWriter()
        freemarkerTemplateService.render("gobaby${suffix}" , [testvar:"basejump"], writer, "freemarker-plugin-test")
        println writer.toString()
        assertTrue writer.toString().contains("<p>basejump</p>")
    }

    void testRenderStringPlugin(){
        def res = freemarkerTemplateService.renderString("gobaby${suffix}" , [testvar:"basejump"], "freemarker-plugin-test")
        println res
        assertTrue res.contains("<p>basejump</p>")
    }

    void testRenderString(){
        def res = freemarkerTemplateService.renderString("demo/index${suffix}" , [name:"basejump",state:"IL"])
        println res
        assertTrue res.contains("Name: basejump")
    }
}

