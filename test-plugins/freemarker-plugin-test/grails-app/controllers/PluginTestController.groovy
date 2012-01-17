
class PluginTestController {
    
	def freemarkerViewService
	
	def goBaby = {
		render view: '/gobaby', model: [testvar: 'fly away']
    }

	def bluesky = {
		render view: '/demo/bluesky', model: [testvar: 'fly away']
    }

	def itWorks = {
		[testvar: 'fly away']
    }

	def override = {
		render view: 'override'
    }

	def service = {
		response.setHeader("Content-Type","text/html;charset=ISO-8859-1")
		freemarkerViewService.render('itWorks.ftl', [testvar: 'flying squids'],response.writer)
		render false
	}

}