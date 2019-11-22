/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package freemarker.tags.test

import java.text.SimpleDateFormat

import groovy.transform.CompileDynamic

@CompileDynamic
class SimpleTagLib {

    def emoticon = { attrs, body ->
        out << body() << (attrs.happy == 'true' ? " :-)" : " :-(")
    }

    def dateFormat = { attrs, body ->
        out << new SimpleDateFormat(attrs.format).format(attrs.date)
    }

    def isAdmin = { attrs, body ->
        def user = attrs['user']
        if (user?.admin) {
            out << body()
        }
    }

    def repeat = { attrs, body ->
        attrs.times?.toInteger().times { num ->
            out << body(num)
        }
    }

    def reverse = { attrs, body ->
        out << (body() as String).reverse()
    }

    def thread = { attrs ->
        out << "Thread-${attrs.name}"
    }
}
