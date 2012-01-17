import geb.spock.GebSpec
 
class DemoControllerSpec extends GebSpec {
	
    def "sanity check"() {
        when:
        	go "demo/wtf"
 
        then:
		println driver.pageSource
        driver.pageSource.contains("wtf")	
    }

	//@spock.lang.IgnoreRest
	def "default index"() {
        when:
        	go "demo"
 
        then:
		def html = driver.pageSource
		html.contains('Name: Jake')
		html.contains('State: Missouri')
    }
    
    def "normal gsp"() {
        when:
        	go "demo/normal"
 
        then:
		def html = driver.pageSource
		html.contains('Name: Jake')
		html.contains('State: Missouri')
    }
	
	def "use view based on action name"() {
        when:
        	go "demo/fmtemplate"
 
        then:
        def html = driver.pageSource
		html.contains('Name: Jake')
		println html
		//fmtemplate also has a number of freemarker includes so lets make sure those are working
		//html.
    }
    
	def "Controller add prefix"() {
        when:
        	go "demo/justTheView"
 
        then:
			def html = driver.pageSource
			html.contains('Name: Abe')
			html.contains('State: Illinois')
    }
    
    def "define full path"() {
        when:
        	go "demo/fullPathToView"
 
        then:
			def html = driver.pageSource
			html.contains('Name: Abe')
    }

	def "testTaglib"() {
        when:
        	go "demo/testTaglib"
 
        then:
			def html = driver.pageSource
			html.contains('The template at /demo/fmtemplate.ftl was rendered with Name: Zack')
			html.contains('The template at /templates/freemarker/snippet.ftl was rendered with Name: Scott')
    }

	def "flash is passed through"() {
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

	def "service"() {
        when:
        	go "demo/service"
 
        then:
		def html = driver.pageSource
		html.contains('Name: Abe')
		html.contains('State: Illinois')
    }

    def verfiy

}