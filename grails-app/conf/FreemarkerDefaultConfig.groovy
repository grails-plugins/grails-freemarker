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

			//tries to find templates by appending the local to then name
			localizedLookup = false
			
			//when referencing a freemarker view name then require the suffix
			requireViewSuffix = false
            
            viewResolver { 
                hideException = false 
            }

            tags {
                enabled = true
            }
        }
    }
}