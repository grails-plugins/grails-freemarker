import geb.spock.GebSpec

class DemoControllerSpec extends GebSpec {

	def "sanity check"() {
		when:
			go "demo/wtf"

		then:
			println driver.pageSource
			driver.pageSource.contains("they work, you are sane")
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

	def "normal gsp sanity"() {
		when:
			go "demo/normalGSP"

		then:
			def html = driver.pageSource
			html.contains('Name: Jake')
			html.contains('State: Missouri')
	}

	def "subdir"() {
		when:
			go "demo/subdir"

		then:
			def html = driver.pageSource
			html.contains('subdir info')
	}

	def "subdir2"() {
		when:
			go "demo/testTaglib"

		then:
			def html = driver.pageSource
			html.contains('and first name from controller model Zack')
	}


}
