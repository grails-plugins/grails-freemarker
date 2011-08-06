
class PluginTestController {
    
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

}