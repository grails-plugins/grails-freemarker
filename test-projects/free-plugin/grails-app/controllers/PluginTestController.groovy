
class PluginTestController {
    
	def freemarkerViewService
	
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
		freemarkerViewService.render('itWorks.ftl', [testvar: 'flying squids'],response.writer)
		render false
	}

}