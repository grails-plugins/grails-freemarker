package testing

class DemoController {

    def freeMarkerViewService

    //just a sanity check to make sure the basics still work
    def wtf(){
        render "they work, you are sane"
    }

    //passes through to index, sitemesh should be set here too
    def index(){
        render view: 'index.ftl', model: [name: 'Jake', state: 'Missouri']
    }

    //a simple normal gsp for a sanity check
    def normalGSP(){
        render view: 'normal', model:[name: 'Jake', state: 'Missouri']
    }

    //passes through to the ftl template
    //fmtemplate also has a number of freemarker includes
    def includes(){
        render view: 'fmtemplate.ftl', model:[name: 'Jake']
    }

    //passes through to the ftl template
    //fmtemplate also has a number of freemarker includes
    def subdir(){
        render view: 'subdir/subdir.ftl', model:[name: 'Jake']
    }

    //specify the full path to the view
    def fullPathToView(){
        render view: '/demo/fmtemplate.ftl', model: [name: 'Abe', state: 'Illinois']
    }

    //view outside of grails. the trick works
    def urlView(){
        render view: "/file:test/views/foo.ftl", model: [name: 'Abe', state: 'Illinois']
    }

    //test the fm:render taglib
    def testTaglib(){
        //render view: 'testTaglib', model:[firstName: 'Zack', middleName: 'Scott']
        [firstName: 'Zack', middleName: 'Scott']
    }

    //test that the flash param is passed through properly
    def testFlash(){
        flash.message = 'this message is in flash'
        redirect action: index
    }

    def pluginRoot(){
        render view: '/gobaby.ftl', model: [testvar: 'fly away'], plugin:'free-plugin'
    }

    def pluginDemoDir(){
        render view: 'bluesky.ftl', model: [testvar: 'fly away'], plugin:'free-plugin'
    }

    def pluginDemoDirGsp(){
        render view: 'bluesky.gsp', model: [testvar: 'fly away'], plugin:'free-plugin'
    }

    def pluginOverride(){
        render view: 'bluesky', model: [testvar: 'fly away'], plugin:'free-plugin'
    }

    def testGspPlugin(){
        //control test
        render template: 'testGspPlugin', model: [name: 'Abe', state: 'Illinois'], plugin:'freemarker-plugin-test'
    }

    def service(){
        response.setHeader("Content-Type","text/html;charset=ISO-8859-1")
        freeMarkerViewService.render('index.ftl', [name: 'Abe', state: 'Illinois'],response.writer)
        render false
    }

}
