import geb.spock.GebSpec
 
class DemoControllerSpec extends GebSpec {
    def "sanity check"() {
        when:
        	go "demo/wtf"
 
        then:
			println driver.pageSource
        	driver.pageSource.contains("wtf")	
    }

	def "default view"() {
        when:
        	go "demo"
 
        then:
			def html = driver.pageSource
			html.contains('Name: Jake')
			html.contains('State: Missouri')
    }
	
	def "Explicit Render From Controller"() {
        when:
        	go "demo/testExplicitRenderFromController"
 
        then:
			def html = driver.pageSource
			html.contains('Name: Abe')
			html.contains('State: Illinois')
    }

	def "taglib"() {
        when:
        	go "demo/testTaglib"
 
        then:
			def html = driver.pageSource
			html.contains('#before fmtemplate# The template at /demo/fmtemplate.ftl was rendered with Name: Zack #after fmtemplate#')
			html.contains('#before snippet# The template at /templates/freemarker/snippet.ftl was rendered with Name: Scott #after snippet#')
    }

	def "flash object"() {
        when:
        	go "demo/testFlash"
 
        then:
			def html = driver.pageSource
			html.contains('this message is in flash')
    }

	def "plugin gobaby"() {
        when:
        	go "demo/gobaby"
 
        then:
			def html = driver.pageSource
			html.contains('fly away')
			html.contains('This is a ftl from a plugin')
    }

	def "plugin bluesky"() {
        when:
        	go "demo/bluesky"
 
        then:
			def html = driver.pageSource
			html.contains('fly away')
			html.contains('Blue Skies, from a plugin')
    }

	def "sanity check gsp in plugin"() {
        when:
        	go "demo/testGspPlugin"
 
        then:
			def html = driver.pageSource
			html.contains('Hello from the a GSP in the plugin')
    }


}