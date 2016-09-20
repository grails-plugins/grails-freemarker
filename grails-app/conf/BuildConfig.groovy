grails.useGrails3FolderLayout = true
grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: false,
    // configure settings for the run-app JVM
    run: false,
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]
//grails.project.work.dir = '.grails'
grails.project.dependency.resolver = "maven" // or ivy

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    def gebVersion = "0.13.1"
    def webdriverVersion = "2.53.1"

    dependencies {
        compile "org.freemarker:freemarker:2.3.25-incubating"
        compile 'commons-io:commons-io:2.5'

        //TESTING GEB
        //test("org.seleniumhq.selenium:selenium-support:$seleniumVersion"){ export = false }
        test "org.gebish:geb-spock:${gebVersion}"

        test("org.seleniumhq.selenium:selenium-support:${webdriverVersion}",
                "org.seleniumhq.selenium:selenium-chrome-driver:${webdriverVersion}",
                "org.seleniumhq.selenium:selenium-firefox-driver:${webdriverVersion}",
                "org.seleniumhq.selenium:selenium-ie-driver:${webdriverVersion}")
        {
            export = false
        }

        test("com.codeborne:phantomjsdriver:1.3.0") {
            // phantomjs driver pulls in a different selenium version amongs other stuff it seemed
            transitive = false
        }
        test "io.github.bonigarcia:webdrivermanager:1.4.8"

        //test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"

        // For downloading browser-specific drivers that browsers like Chrome and IE require


    }

    plugins {
        compile(":plugin-config:0.2.1")

        build(":tomcat:7.0.70"){
            export = false
        }
        compile(":hibernate4:4.3.10") {
            export = false
        }

        provided(":executor:0.3") {
            export = false
        }

        test (":geb:$gebVersion"){ export = false }

        build(":release:3.1.2", ":rest-client-builder:2.1.1") { export = false }

        compile(":view-tools:0.2-grails2")
    }
}

//grails.project.work.dir = '.grails'
//grails.plugin.location.'view-tools' = "/Users/basejump/source/nine/grails-view-tools"

if (appName == "freemarker") {
    //grails.plugin.location.'free-plugin' = "test-projects/free-plugin"
    grails.plugin.location.'freemarker-plugin-test' = "test-projects/freemarker-plugin-test"

    //grails.plugin.location.executor = "../../nine/executor"
}

