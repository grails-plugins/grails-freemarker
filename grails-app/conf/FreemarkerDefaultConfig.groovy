grails {
    plugin {
        freemarker {
            //preTemplateLoaderBeanName = null /* Deprecated*/
            
            /* A list of template loaders or strings - Strings will be used as beanNames. */
            preTemplateLoaders = null
            templateLoaderPaths = null
            //postTemplateLoaderBeanName = null /* Deprecated */
            
            /* A list of template loaders or strings - Strings will be used as beanNames. */
            postTemplateLoaders = null
            
            viewResolver { 
                hideException = false 
            }
            
            tags {
                enabled = true
            }
        }
    }
}