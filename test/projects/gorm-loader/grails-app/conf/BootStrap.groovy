class BootStrap {

    def init = { servletContext ->
		def templ = "test this"
		def tpl = new gorm.loader.GormTemplate(name:'play/index.ftl',template:'overrides play/index in views')
		tpl.save(flush:true,failOnError:true)
		
    }
    def destroy = {
    }
}
