package testing

import freemarker.template.Configuration

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
import freemarker.template.Template
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

/**
 * @author Daniel Henrique Alves Lima
 */
@Integration
class ConcurrencyTests extends Specification {

    def freeMarkerConfigurer
    private Exception threadException
    private ThreadGroup myThreadGroup = new ThreadGroup('x') {
        void uncaughtException(Thread t,Throwable e) {
            super.uncaughtException(t, e)
            threadException = e
        }
    }

    void testConcurrency() {
        when:
        def text = new StringBuilder('[#ftl/]')
        for (int i = 0; i < 100; i++) {
            text.append(i)
            text.append(': [@g.thread name=name /]\n')
        }
        Configuration cfg = freeMarkerConfigurer.configuration
        Template template = new Template('template', new StringReader(text.toString()), cfg)
        text = null

        Thread mainThread = Thread.currentThread()
        List threads = []
        for (int counter = 0; counter < 20; counter++) {
            Thread t = runInParallel {
                final int myCounter = counter
                StringWriter result = new StringWriter()
                template.process([name: myCounter + 1], result)
                List lines = new StringReader(result.toString()).readLines()
                result = null
                then:
                100 == lines.size()
                when:
                lines.eachWithIndex { line, index ->
                    if (Math.random() <= 0.1) {
                        //assertTrue "random ${index} ${myCounter+1}", false
                    }
                    then:
                    "${index}: Thread-${myCounter+1}".toString() == line
                }
            }
            threads << t
        }

        if (Thread.currentThread() == mainThread) {
            threads.eachWithIndex {thread, idx ->
                while (thread.isAlive() && threadException == null) {
                    Thread.sleep 1000
                }

                println "Exiting ${idx + 1}: ${thread} ${System.currentTimeMillis()}"
            }
            if (threadException) {
                throw threadException
            }
        }
        then:
        1==1
    }

    private runInParallel = { Closure c ->
        if (threadException) {
            throw threadException
        }
        def thread
        boolean executed = false
        Closure c1 = {
            try {
                c()
            }
            finally {
                executed = true
            }
        }

        thread = new Thread(myThreadGroup, c1 as Runnable) //thread.daemon = true
        thread.start()
        thread.yield()
        return thread
    }
}
