/*
* Copyright 2019 Yak.Works - Licensed under the Apache License, Version 2.0 (the "License")
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*/
package grails.plugin.freemarker

import groovy.transform.CompileDynamic

import org.grails.core.artefact.TagLibArtefactHandler
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter

import grails.core.ArtefactHandler
import grails.core.GrailsApplication

/**
 * @author Daniel Henrique Alves Lima
 */
@CompileDynamic
class TagLibPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    GrailsApplication grailsApplication

    @Override
    @SuppressWarnings(['ExplicitLinkedListInstantiation'])
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        ArtefactHandler tagLibHandler = grailsApplication.getArtefactHandler(TagLibArtefactHandler.TYPE)
        if (tagLibHandler.isArtefact(bean.class) && beanName.endsWith('_fm')) {

            ThreadLocal<Deque> outStack = new ThreadLocal<Deque>()
            def getX = {
                Deque<Object> d = outStack.get()
                if (d == null) {
                    d = new LinkedList<Object>()
                    outStack.set(d)
                }

                return d
            }

            MetaClass mc = bean.metaClass
            mc.getOut = {->
                Deque d = getX()
                return (d.size() > 0)?d.last : null
            }

            mc.popOut = {->
                return getX().removeLast()
            }

            mc.pushOut = {out ->
                getX().addLast(out)
            }
        }

        return true
    }
}
