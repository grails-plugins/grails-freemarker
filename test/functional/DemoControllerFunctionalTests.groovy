class DemoControllerFunctionalTests extends functionaltestplugin.FunctionalTestCase {

    void testAsDefaultView() {
        get '/demo'
        assertStatus 200
        assertContentContains 'Name: Jake'
        assertContentContains 'State: Missouri'
    }
    
    void testExplicitRenderFromController() {
        get '/demo/testExplicitRenderFromController'
        assertStatus 200
        assertContentContains 'Name: Abe'
        assertContentContains 'State: Illinois'
    }
    
    void testTaglib() {
        get '/demo/testTaglib'
        assertStatus 200
        assertContentContains 'The template at /demo/fmtemplate.ftl was rendered with Name: Zack'
        assertContentContains 'The template at /templates/freemarker/snippet.ftl was rendered with Name: Scott'
    }
}
