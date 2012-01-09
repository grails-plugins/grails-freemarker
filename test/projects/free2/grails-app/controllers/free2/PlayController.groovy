package free2

class PlayController {
	
	

    def index ={ }

	def freemarker ={ }
	
	def plugingsp(){
		render(view:"/demo/simple", plugin:"freemarker")
	}
	
	def pluginftl(){
		render(view:"/demo/included", plugin:"freemarker")
	}
	

}
