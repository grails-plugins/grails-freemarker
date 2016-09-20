package gorm.loader

class PlayController {

    def index() { 
		//loaderAttribute is optional. 
		//In this example we use it so we skip  the GormTemplateLoader unless its set. See the GormLoaderService
		render view:'index.ftl'
	}
	
	def sanity() { 
		render view:'sanity.ftl'
	}
	
	def noprefix() { 
		render view:'sanity'
	}
	
	def goodStuff() { 
		render view:'goodStuff.ftl'
	}
	
	def wlayout() { 
		
	}
}
