grails.project.dependency.resolution = {
    def versions
    if ("$grailsVersion" > "1.3.7") {
        versions = [geb:"0.6.2", selenium:"2.16.1", spock:"0.6-SNAPSHOT",pluginConfig:"0.1.5"]      
    }else{
        versions = [geb:"0.6.0", selenium:"2.16.1", spock:"0.5-groovy-1.7",pluginConfig:"0.1.5"]
    }

    inherits('global') {
        excludes //"httpclient"//, "httpcore"
    }

    repositories {        
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
        //mavenLocal()
    }

    dependencies {
        runtime "org.freemarker:freemarker:2.3.18"

        test "org.codehaus.geb:geb-spock:${versions.geb}"
            
        test("org.seleniumhq.selenium:selenium-htmlunit-driver:${versions.selenium}") {
            excludes "xml-apis"
        }
        
        provided("org.codehaus.groovy.modules.http-builder:http-builder:0.5.2"){
            export = false
            excludes 'nekohtml', "httpclient", "httpcore","xml-apis","groovy"
        }
        provided('net.sourceforge.nekohtml:nekohtml:1.9.15') { 
            export = false
            excludes "xml-apis" 
        }
    }

    plugins {
        compile(":plugin-config:${versions.pluginConfig}"){}
        compile(":tomcat:$grailsVersion", ":hibernate:$grailsVersion") {
            export = false
        }
        
        provided(":executor:0.3"){
            export = false
        }

        test ":geb:${versions.geb}",":spock:${versions.spock}"
        
        build(':release:1.0.1') {
              excludes 'http-builder','nekohtml','svn'
              export = false
        }
        //this seems to prevent svn from being added to application properties
        build(":svn:1.0.2"){
            export = false
        }
    }
}
//grails.project.work.dir = '.grails'

if (appName == "freemarker") {
    grails.plugin.location.'freemarker-plugin-test' = "test-plugins/freemarker-plugin-test"
    //grails.plugin.location.executor = "../../nine/executor"
}
