package testing

class PlayController {



    def index ={ }


    def freemarker(){
        render view:"freemarker.ftl"
    }

    def notFound(){
        render view:"xxx.ftl"
    }

    def classpath(){
        render view:"/classpath-test.ftl"
    }

    //passes through to index, sitemesh should be set here too
    def demo(){
        render view: '/demo/index.ftl', model: [name: 'Jake', state: 'Missouri'], plugin:"freemarker"
    }

    def fullScan(){
        render view: '/pluginTest/override.ftl'
    }

    def tagPlay(){
        render view: '/tagPlay/index.ftl'
    }

    def plugingsp(){
        render(view:"/demo/simple", plugin:"freemarker")
    }

    def pluginftl(){
        render(view:"/demo/included", plugin:"freemarker")
    }

    //passes through to index, sitemesh should be set here too
    def demoGsp(){
        render view: '/demo/simple', model: [name: 'Jake', state: 'Missouri'], plugin:"freemarker"
    }



        //passes through to index, sitemesh should be set here too
    def specificPluginDir(){
        render view: '/plugins/freemarker/demo/index.ftl', model: [name: 'Jake', state: 'Missouri']
    }

    def pluginPlayDir(){
        render view: 'bluesky.ftl', model: [testvar: 'fly away'], plugin:'free-plugin'
    }

    def pluginPlayDirGsp(){
        render view: 'bluesky.gsp', model: [testvar: 'fly away'], plugin:'free-plugin'
    }


}
