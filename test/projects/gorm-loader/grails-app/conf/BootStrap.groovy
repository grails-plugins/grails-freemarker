import freemarker.template.*

class BootStrap {
    def freemarkerConfig
    
    def init = { servletContext ->
		def templ = "test this"
		def tpl = new gorm.loader.GormTemplate(name:'play/index.ftl',template:'overrides play/index in views')
		tpl.save(flush:true,failOnError:true)
		
		def macro = new gorm.loader.GormTemplate(name:'gormMacro.ftl',template:'<#macro jump text>gorm macro said: ${text} </#macro>')
		macro.save(flush:true,failOnError:true)
		
		def include = new gorm.loader.GormTemplate(name:'gormInclude.ftl',template:'success for gormInclude')
		include.save(flush:true,failOnError:true)
		
		assert freemarkerConfig.configuration.objectWrapper instanceof SimpleObjectWrapper
    }
    def destroy = {
    }
}
