package testing

import grails.util.Holders

class TagPlayController {

	def freemarkerViewService
	def grailsApplication

	def sanity = {
		render "wtf"
	}

	def index = {
		def context = Holders.getServletContext()
		log.info "*** scontext  is ${context.getContextPath()} "
		[name: 'Jake', state: 'Missouri']
	}

	//let grails add the controller name prefix
	def justTheView = {
		render view: 'basic.ftl', model: [name: 'Abe', state: 'Illinois']
	}

	def service = {
		def wout = freemarkerViewService.render('/tagPlay/index.ftl', [name: 'Abe', state: 'Illinois'])
		println "what the hell  $wout"
		render wout
	}

	def async = {
		log.debug "calling freemarkerViewService.render"
		def wout
		runAsync{
			try{
				log.debug "calling freemarkerViewService.render"
				wout = freemarkerViewService.render('/tagPlay/index.ftl', [name: 'Abe', state: 'Illinois'])
				log.debug  "what the hell  $wout"
				//log.info "html " + wout
			}
			catch(e) {
				println "what the hell - $e"
				log.error e
			}
		}
		sleep 1500
		render wout.toString()
	}
}
