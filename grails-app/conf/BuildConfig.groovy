grails.project.dependency.resolution = {

    inherits('global') {
    }

    repositories {        
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    
    dependencies {
        runtime 'org.freemarker:freemarker:2.3.16'
    }
}