package functional

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback

@Integration
@Rollback
class TagPlayControllerSpec extends GebSpec {

    def executorService

    void "grails taglibs"() {
        when:
            go "/tagPlay"

        then:
             def html = driver.pageSource //.replaceAll("[\n\r]", "")
             println html
             $('#link').firstElement().toString().trim() == '<a href="/tagPlay/sanity" id="link">'
             //$('#fromSiteMesh').text() == 'from sitemesh layout'
             $('#link').text() == 'sanity'
             $('#numFormat').text() == '$9,999.90'
             $('#messageCode').text() == 'dabba doo'
             $('#repeat').text() == 'Repeat 0 Repeat 1 Repeat 2'
    }

    void "test async background"() {
        when:
            go "/tagPlay/async"

        then:

             def html = driver.pageSource //.replaceAll("[\n\r]", "")
             println html

             //sitemesh will not work from service so this should be null now
             $('#fromSiteMesh').text() == null
             $('#link').text() == 'sanity'
             $('#link').firstElement().toString().trim() == '<a href="/tagPlay/sanity" id="link">'
             $('#numFormat').text() == '$9,999.90'
             $('#messageCode').text() == 'dabba doo'
             $('#repeat').text() == 'Repeat 0 Repeat 1 Repeat 2'
    }
}
