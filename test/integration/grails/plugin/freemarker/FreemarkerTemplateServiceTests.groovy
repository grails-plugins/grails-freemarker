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

}

