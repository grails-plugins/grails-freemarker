import freemarker.template.*

class BootStrap {
    def freemarkerConfig
    
    def init = { servletContext ->
		def templ = "test this"
		def tpl = new gorm.loader.GormTemplate(name:'play/index.ftl',template:'overrides play/index in views')
		tpl.save(flush:true,failOnError:true)
		assert freemarkerConfig.configuration.objectWrapper instanceof SimpleObjectWrapper
    }
    def destroy = {
    }
}
