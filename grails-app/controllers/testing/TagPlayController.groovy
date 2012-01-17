package testing

class TagPlayController {
    
	def freemarkerViewService
	def grailsApplication

    def sanity = {
        render "wtf"
    }
    
    def index = {
        [name: 'Jake', state: 'Missouri']
    }
    //let grails add the controller name prefix 
    def justTheView = {
        render view: 'basic.ftl', model: [name: 'Abe', state: 'Illinois']
    }
    
	//passes through to index, sitemesh should be set here too
    def basic = {
		render view:'basic.ftl'
    }
    
    def test2 = {
		render view:'/demo/fmtempalte.ftl'
    }
    
    //a simple normal gsp for a sanity check
    def normal = {
        [name: 'Jake', state: 'Missouri']
    }

}