grails.project.dependency.resolution = {

	inherits('global') {
	}

	repositories {        
		grailsPlugins()
		grailsHome()
        grailsCentral()
		mavenCentral()
		//mavenLocal()
	}

	dependencies {

	}

	plugins {
		compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {}
	}
}
//grails.project.work.dir = '.grails'
grails.plugin.location.freemarker = "../../../"

