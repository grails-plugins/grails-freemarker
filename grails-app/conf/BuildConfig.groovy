grails.project.dependency.resolution = {
	def versions = [geb:"0.6.0", selenium:"2.0rc3", spock:"0.5-groovy-1.7"]
	
	inherits('global') {
	}

	repositories {        
		grailsPlugins()
		grailsHome()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		runtime "org.freemarker:freemarker:2.3.18"
		

		test("org.codehaus.geb:geb-spock:0.6.0"){
			exported = false
		}
		//test "org.codehaus.geb:geb-junit4:$gebVersion" uncomment if you want to use the junit geb
		//the version should match what is used on the geb release being used
		test("org.seleniumhq.selenium:selenium-htmlunit-driver:2.0rc3") {
			exported = false
			exclude "xml-apis"
		}
		//test("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
		//test("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
	}
	
	plugins {
		compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {
			exported = false
		}
		test(":spock:0.5-groovy-1.7") {
			exported = false
		}
		test(":geb:0.6.0") {
			exported = false
		}
		build(':release:1.0.0.M3') {
			//nekohtml was conflicting with htmlunit
			excludes "svn", 'nekohtml'
		}
	}
}
grails.project.work.dir = '.grails'

if (appName == "freemarker-plugin") {
	grails.plugin.location.'freemarker-plugin-test' = "plugins/freemarker-plugin-test"
}
