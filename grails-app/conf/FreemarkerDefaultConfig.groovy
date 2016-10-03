grails {
    plugin {
        freemarker {
            /**
             * when referencing a freemarker view name then require the .ftl suffix
             * if this is false then every single view lookup (render view:xxx, model:yyy)
             * goes through the look up for freemarker first
             */
            requireViewSuffix = true

            /* A list of bean names that implement freemarker.cache.TemplateLoader
            'freeMarkerGrailsTemplateLoader' bean is the deafult and probably only one you will ever need
            best to configure viewResourceLocator to look as then the GrailsFreeMarkerViewResolver has access too*/
            templateLoaders = ["freeMarkerGrailsTemplateLoader"]

            viewResourceLocator{
                // a list of location paths or URLS to add to the list for searching.
                // the default here is to allow a templates dir in conf for helpful macros
                searchLocations = ["classpath:templates/","classpath:freemarker/"] //consistent with spring boot defaults
            }
            viewResolver {
                /*blow exception in resolver or swallow it and move on */
                //hideException = true

                /*allow access with URLs for resourceLoader outside of sandbox with url locations (file:, http:, etc)*/
                //TODO implement this
                //enableUrlLocations = false
            }

            tags {
                /* whether to enable the grails taglibs in the templates */
                enabled = true
                //list of tags to include
                //TODO implement this
                //includeTaglibs = ['g.resource','g.ttt',etc...]
            }
            //extra settings to pass through to the Freemarker Configuration
            //see http://freemarker.sourceforge.net/docs/api/freemarker/template/Configuration.html#setSetting(java.lang.String, java.lang.String)
            // && http://freemarker.sourceforge.net/docs/api/freemarker/core/Configurable.html#setSetting(java.lang.String, java.lang.String)
            /*
            configSettings {
                //for example this will set it to be even more secure.
                new_builtin_class_resolver
            }
            */

            //tries to find templates by appending the local to the name.
            //an odd feature in freemarker that is on by default but we turn it off by default
            localizedLookup = false
        }
    }
}
