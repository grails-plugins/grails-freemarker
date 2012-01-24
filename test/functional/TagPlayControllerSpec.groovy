import geb.spock.GebSpec
 
class TagPlayControllerSpec extends GebSpec {
	
	def executorService
	
	/*NOTE NOTE THESE WILL FAIL IN GRAILS 1.3.7*/
    def "grails taglibs"() {
        when:
        go "tagPlay"
 
        then:
		def html = driver.pageSource //.replaceAll("[\n\r]", "")
		println html
		browser.$('#link').firstElement().toString().trim() == '<a href="/freemarker/tagPlay/sanity" id="link">'
		browser.$('#fromSiteMesh').text() == 'from sitemesh layout'
		browser.$('#link').text() == 'sanity'
		browser.$('#numFormat').text() == '$9,999.90'
		browser.$('#messageCode').text() == 'dabba doo'
		browser.$('#repeat').text() == 'Repeat 0 Repeat 1 Repeat 2'
		
    }

	def "test async background"() {
        when:
        go "tagPlay/async"
 
        then:
		def html = driver.pageSource //.replaceAll("[\n\r]", "")
		println html
	
		//sitemesh will not work from service so this should be null now
		browser.$('#fromSiteMesh').text() == null
		browser.$('#link').text() == 'sanity'
		browser.$('#link').firstElement().toString().trim() == '<a href="/freemarker/tagPlay/sanity" id="link">'	
		browser.$('#numFormat').text() == '$9,999.90'
		browser.$('#messageCode').text() == 'dabba doo'
		browser.$('#repeat').text() == 'Repeat 0 Repeat 1 Repeat 2'
		
    }

	
	
}