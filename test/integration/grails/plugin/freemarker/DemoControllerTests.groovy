package grails.plugin.freemarker

import grails.test.*
import testing.*

class DemoControllerTests extends GroovyTestCase {
	
	def controller
	
	void setup(){
		controller = new DemoController()
		super.setUp()
	}

	void testSanity() {
		def controller = new DemoController()
        controller.wtf()
		println " reponse "+ controller.response.contentAsString
		assert controller.response.contentAsString.contains('wtf')
    }
	
	void testtestExplicitRenderFromController() {
		def controller = new DemoController()
        controller.testExplicitRenderFromController()
		println " reponse "+ controller.response.contentAsString
		assert controller.response.contentAsString.contains('Name: Jake')
    }
    
/*    void testExplicitRenderFromController() {
        get '/demo/testExplicitRenderFromController'
        assertStatus 200
        assertContentContains 'Name: Abe'
        assertContentContains 'State: Illinois'
    }
    
    void testTaglib() {
        get '/demo/testTaglib'
        assertStatus 200
        assertContentContains '#before fmtemplate# The template at /demo/fmtemplate.ftl was rendered with Name: Zack #after fmtemplate#'
        assertContentContains '#before snippet# The template at /templates/freemarker/snippet.ftl was rendered with Name: Scott #after snippet#'
    }
    
    void testFlash() {
        get '/demo/testFlash'
        assertStatus 200
        assertContentContains 'Name: Jake'
        assertContentContains 'State: Missouri'
        assertContentContains 'this message is in flash'
    }*/
}
