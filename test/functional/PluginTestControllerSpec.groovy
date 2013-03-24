import geb.spock.GebSpec

class PluginTestControllerSpec extends GebSpec {
	def "sanity check"() {
		when:
			go "pluginTest/goBaby"

		then:
			println driver.pageSource
			driver.pageSource.contains('This is a ftl from a plugin')
	}

	def "bluesky"() {
		when:
			go "pluginTest/bluesky"

		then:
			def html = driver.pageSource
			html.contains('Blue Skies')
	}

	def "override in app"() {
		when:
			go "pluginTest/override"

		then:
			def html = driver.pageSource
			html.contains('this is the one you should see')
	}

	def "no render and itWorks"() {
		when:
			go "pluginTest/itWorks"

		then:
			def html = driver.pageSource
			html.contains('you know it')
	}

	def "service in plugin"() {
		when:
			go "pluginTest/service"

		then:
			def html = driver.pageSource
			html.contains('you know it')
	}
}
