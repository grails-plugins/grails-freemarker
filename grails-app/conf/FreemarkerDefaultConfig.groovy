grails {
    plugin {
        freemarker {
            preTemplateLoaderBeanName = null
            templateLoaderPaths = null
            postTemplateLoaderBeanName = null
            
            viewResolver { 
                hideException = false 
            }
            
            tags {
                enabled = true
            }
        }
    }
}