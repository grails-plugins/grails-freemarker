package testing

class DemoController {

    def freemarkerViewService

    //just a sanity check to make sure the basics still work
    def wtf = {
        render "wtf"
    }

    //passes through to index, sitemesh should be set here too
    def index = {
        render view: 'index.ftl', model: [name: 'Jake', state: 'Missouri']
    }

    //a simple normal gsp for a sanity check
    def normal = {
        [name: 'Jake', state: 'Missouri']
    }

    //passes through to the ftl template
    //fmtemplate also has a number of freemarker includes
    def fmtemplate = {
        render view: 'fmtemplate.ftl', model:[name: 'Jake']
    }

    //let grails add the controller name prefix
    def justTheView = {
        render view: 'index.ftl', model: [name: 'Abe', state: 'Illinois']
    }

    //specify the full path to the view
    def fullPathToView = {
        render view: '/demo/fmtemplate.ftl', model: [name: 'Abe', state: 'Illinois']
    }

    //test the fm:render taglib
    def testTaglib = {
        //render view: 'testTaglib', model:[firstName: 'Zack', middleName: 'Scott']
        [firstName: 'Zack', middleName: 'Scott']
    }

    //test that the flash param is passed through properly
    def testFlash = {
        flash.message = 'this message is in flash'
        redirect action: index
    }

    def gobaby = {
        render view: '/gobaby', model: [testvar: 'fly away'], plugin:'freemarker-plugin-test'
    }

    def bluesky = {
        render view: 'bluesky', model: [testvar: 'fly away'], plugin:'freemarker-plugin-test'
    }

    def testGspPlugin = {
        //control test
        render template: 'testGspPlugin', model: [name: 'Abe', state: 'Illinois'], plugin:'freemarker-plugin-test'
    }

    def service = {
        response.setHeader("Content-Type","text/html;charset=ISO-8859-1")
        freemarkerViewService.render('index.ftl', [name: 'Abe', state: 'Illinois'],response.writer)
        render false
    }

    def db = {
        render view: 'gorm:db.ftl', model: [testvar: 'fly away'], plugin:'freemarker-plugin-test'
    }
}
