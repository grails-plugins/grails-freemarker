package functional

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback

// TODO: probably should be moved to free-plugin
@Integration
@Rollback
class PluginTestControllerSpec extends GebSpec {
    def setup(){

    }
    void "sanity check"() {
        when:
            go "/pluginTest/goBaby"

        then:
            println driver.pageSource
            driver.pageSource.contains('This is a ftl from a plugin')
            true
    }

    void "bluesky"() {
        when:
            go "/pluginTest/bluesky"

        then:
            def html = driver.pageSource
            html.contains('Blue Skies')
            true
    }

    /*void "override in app"() {
        when:
            go "/pluginTest/override"

        then:
            def html = driver.pageSource
            html.contains('this is the one you should see')
            true
    }*/

    void "service in plugin"() {
        when:
            go "/pluginTest/service"

        then:
            def html = driver.pageSource
            html.contains('you know it')
            true
    }
}
