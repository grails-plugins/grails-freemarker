/*
* Copyright 2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package freemarker.tags.test

class SimpleTagLib {

    def emoticon = { attrs, body ->
        out << body() << (attrs.happy == 'true' ? " :-)" : " :-(")
    }

    def dateFormat = { attrs, body ->
        out << new java.text.SimpleDateFormat(attrs.format).format(attrs.date)
    }
    
    def isAdmin = { attrs, body ->
        def user = attrs['user']
        if(user != null && user.admin) {
              out << body()
        }
   }
    
    def repeat = { attrs, body ->
        attrs.times?.toInteger().times { num ->
            out << body(num)
        }
    }
    
    def thread = { attrs ->
        out << "Thread-${attrs.name}"
    }
    
}