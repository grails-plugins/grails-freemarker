package functional

import geb.Browser
import geb.Configuration
import geb.ConfigurationLoader
import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
// TODO: probably should be moved to free-plugin
@Integration(applicationClass=grails.plugin.freemarker.Application)
class PluginTestControllerSpec extends GebSpec {
	def setup(){

	}
	def "sanity check"() {
		when:
			go "pluginTest/goBaby"

		then:
			println driver.pageSource
			driver.pageSource.contains('This is a ftl from a plugin')
			true
	}

	def "bluesky"() {
		when:
			go "pluginTest/bluesky"

		then:
			def html = driver.pageSource
			html.contains('Blue Skies')
			true
	}

	def "override in app"() {
		when:
			go "pluginTest/override"

		then:
			def html = driver.pageSource
			html.contains('this is the one you should see')
			true
	}

	def "service in plugin"() {
		when:
			go "pluginTest/service"

		then:
			def html = driver.pageSource
			html.contains('you know it')
			true
	}
}
