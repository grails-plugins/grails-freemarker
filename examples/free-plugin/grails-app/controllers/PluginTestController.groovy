
class PluginTestController {

    def freeMarkerViewService

    def goBaby = {
        render view: '/gobaby.ftl', model: [testvar: 'fly away']
    }

    def bluesky = {
        render view: '/demo/bluesky.ftl', model: [testvar: 'fly away']
    }

    def override = {
        render view: 'override.ftl'
    }

    def service = {
        response.setHeader("Content-Type","text/html;charset=ISO-8859-1")
        freeMarkerViewService.render('pluginTest/itWorks.ftl', [testvar: 'flying squids'],response.writer)
        render false
    }

    def serviceDemoIndex = {
        def view = freeMarkerViewService.getView("/demo/index.ftl")
        assert view //.getTemplate(Locale.US)
        response.setHeader("Content-Type","text/html;charset=ISO-8859-1")
        freeMarkerViewService.render('/demo/index.ftl', [testvar: 'flying squids'],response.writer)
        render false
    }

}
