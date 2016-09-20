package testing

import grails.test.spock.IntegrationSpec

class DemoControllerIntegrationSpec extends IntegrationSpec {
    def freeMarkerViewService

    DemoController controller

    def setup() {
        controller = new DemoController()
        controller.freeMarkerViewService = freeMarkerViewService
    }

    def cleanup() {
    }

    void "test index"() {
        when:
        controller = new DemoController()
        controller.index()

        then:
        '/demo/index.ftl' == controller.modelAndView.viewName

    }

    void "test normal"() {
        when:
            controller.normalGSP()
        then:
            controller.response.status == 200
    }

    void "test includes"() {
        when:
            controller.includes()
        then:
            controller.response.status == 200
            '/demo/fmtemplate.ftl' == controller.modelAndView.viewName
    }

    void "test subdir"() {
        when:
            controller.subdir()
        then:
            controller.response.status == 200
            '/demo/subdir/subdir.ftl' == controller.modelAndView.viewName
    }

    void "test fullPathToView"() {
        when:
        controller.fullPathToView()
        then:
        controller.response.status == 200
        '/demo/fmtemplate.ftl' == controller.modelAndView.viewName
    }

    void "test urlView"() {
        when:
        controller.urlView()
        then:
        controller.response.status == 200
        '/file:test/views/foo.ftl' == controller.modelAndView.viewName
    }
}
