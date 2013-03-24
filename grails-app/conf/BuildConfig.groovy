grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    String gebVersion = '0.7.0'
    String seleniumVersion = '2.21.0'

    dependencies {
        compile "org.freemarker:freemarker:2.3.18"
        compile 'commons-io:commons-io:2.1'

        test "org.codehaus.geb:geb-spock:$gebVersion"

        test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            exclude "xml-apis"
            export = false
        }

        provided("org.codehaus.groovy.modules.http-builder:http-builder:0.5.2") {
            export = false
            excludes 'nekohtml', "httpclient", "httpcore","xml-apis","groovy"
        }
        provided('net.sourceforge.nekohtml:nekohtml:1.9.15') {
            export = false
            excludes "xml-apis"
        }
    }

    plugins {
        compile(":plugin-config:0.1.5")
        compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {
            export = false
        }

        provided(":executor:0.3") {
            export = false
        }

        test ":geb:$gebVersion", ":spock:0.6", {
            export = false
        }

        build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}

//grails.project.work.dir = '.grails'

if (appName == "freemarker") {
    grails.plugin.location.'freemarker-plugin-test' = "test-plugins/freemarker-plugin-test"
    //grails.plugin.location.executor = "../../nine/executor"
}
