class FreemarkerGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"
    def title = "FreeMarker Grails Plugin"
    def description = '''\
The Grails FreeMarker plugin provides support for rendering FreeMarker templates
as views.
'''

    def documentation = "http://grails.org/Freemarker+Plugin"

    def doWithSpring = {
        boolean developmentMode = !application.warDeployed

        freemarkerConfig(org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer) {
            if(developmentMode) {
                templateLoaderPath="file:${BuildSettingsHolder.settings.baseDir.absolutePath}/grails-app/views"
            } else {
                templateLoaderPath="/WEB-INF/grails-app/views"
            }
        }
        freemarkerViewResolver(org.springframework.grails.freemarker.GrailsFreeMarkerViewResolver) {
            prefix = ''
            suffix = '.ftl'
            order = 10
        }
    }
}
