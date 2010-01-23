class DemoController {
    
    def index = {
        [name: 'Jake', state: 'Missouri']
    }

    def testExplicitRenderFromController = {
        render view: 'index', model: [name: 'Abe', state: 'Illinois']
    }

    def testTaglib = {
        [firstName: 'Zack', middleName: 'Scott']
    }
    
    def testFlash = {
        flash.message = 'this message is in flash'
        redirect action: index
    }
}