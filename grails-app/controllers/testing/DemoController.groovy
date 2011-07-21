package testing

class DemoController {
    
    def index = {
        [name: 'Jake', state: 'Missouri']
    }

	def include = {
        render view: 'fmtemplate', model:[name: 'Jake']
    }

	def wtf = {
		render "wtf"
	}

    def testExplicitRenderFromController = {
        render view: 'index', model: [name: 'Abe', state: 'Illinois']
    }

    def testTaglib = {
		 //render view: 'testTaglib', model:[firstName: 'Zack', middleName: 'Scott']
        [firstName: 'Zack', middleName: 'Scott']
    }
    
    def testFlash = {
        flash.message = 'this message is in flash'
        redirect action: index
    }

	def gobaby = {
		render view: '/gobaby', model: [testvar: 'fly away'], plugin:'freemarker-plugin-test'
    }

	def bluesky = {
		render view: 'bluesky', model: [testvar: 'fly away'], plugin:'freemarker-plugin-test'
    }

	def testGspPlugin = {
		//control test
		render template: 'testGspPlugin', model: [name: 'Abe', state: 'Illinois'], plugin:'freemarker-plugin-test'
    }
}