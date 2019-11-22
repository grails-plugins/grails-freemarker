/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import org.springframework.context.ApplicationContext

/**
 * @author Daniel Henrique Alves Lima
 */
@CompileStatic
class TagLibAwareConfigurer extends AbstractTagLibAwareConfigurer {

    @Override
    @CompileDynamic
    protected GroovyObject getTagLibInstance(ApplicationContext springContext, String className) {
        return springContext."${className}_fm"
    }
}
