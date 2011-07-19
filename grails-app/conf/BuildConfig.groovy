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
		runtime 'org.freemarker:freemarker:2.3.18'
	}
	plugins {
		compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {
			exported = false
		}
		test(":spock:0.5-groovy-1.7") {
			exported = false
		}
	}
}

if (appName == "grails-freemarker") {
	grails.plugin.location.'freemarker-plugin-test' = "plugins/freemarker-plugin-test"
}
