package functional

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback

@Integration
@Rollback
class DemoControllerSpec extends GebSpec {

    void "sanity check"() {
        when:
            go "/demo/wtf"

        then:
            println driver.pageSource
            driver.pageSource.contains("they work, you are sane")
    }

    //@spock.lang.IgnoreRest
    void "default index"() {
        when:
            go "/demo"

        then:
        def html = driver.pageSource
            html.contains('Name: Jake')
            html.contains('State: Missouri')
    }

    void "normal gsp sanity"() {
        when:
            go "/demo/normalGSP"

        then:
            def html = driver.pageSource
            html.contains('Name: Jake')
            html.contains('State: Missouri')
    }

    void "subdir"() {
        when:
            go "/demo/subdir"

        then:
            def html = driver.pageSource
            html.contains('subdir info')
    }

    void "subdir2"() {
        when:
            go "/demo/testTaglib"

        then:
            def html = driver.pageSource
            html.contains('and first name from controller model Zack')
    }


}
