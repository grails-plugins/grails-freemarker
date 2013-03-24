grails {
    plugin {
        freemarker {

            /* A list of template loaders or strings - Strings will be used as beanNames. */
            preTemplateLoaders = null

            /* a list of any additional paths to search in */
            templateLoaderPaths = null

            /* A list of template loaders or strings - Strings will be used as beanNames. */
            postTemplateLoaders = null

            //tries to find templates by appending the local to the name.
            //an odd feature in freemarker that is on by default
            localizedLookup = false

            //when referencing a freemarker view name then require the .ftl suffix
            requireViewSuffix = false

            viewResolver {
                /*blow exception in resolver or swallow it and move on */
                hideException = true
            }

            tags {
                /* whether to enable the grails taglibs in the templates */
                enabled = true
                //list of tags to include
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
        }
    }
}
