/*
* Copyright 2011 the original author or authors.
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
package grails.plugin.freemarker;


import org.codehaus.groovy.grails.commons.ArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler;
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter


/**
 * 
 * @author Daniel Henrique Alves Lima
 *
 */
class TagLibPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InstantiationAwareBeanPostProcessor {

    GrailsApplication grailsApplication
    
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName)
            throws BeansException {
        
        ArtefactHandler tagLibHandler = grailsApplication.getArtefactHandler(TagLibArtefactHandler.TYPE)
        if (tagLibHandler.isArtefact(bean.class) && beanName.endsWith('_fm')) {
            
            ThreadLocal<Deque> outStack = new ThreadLocal<Deque>()
            def _getX = {
                Deque<Object> d = outStack.get()
                if (d == null) {
                    d = new LinkedList<Object>()
                    outStack.set(d)
                }
                
                return d
            }
            
            
            MetaClass mc = bean.metaClass
            mc.getOut = {->
                Deque d = _getX()
                return (d.size() > 0)?d.last : null
            }
            
            mc.popOut = {->
                return _getX().removeLast()
            }
            
            mc.pushOut = {out ->
                _getX().addLast(out) 
            }
            
            
        }
                
        return true
    }

    

}
