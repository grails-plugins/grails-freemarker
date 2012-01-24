/*
 * Copyright 2010 Grails Plugin Collective
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.freemarker

import org.codehaus.groovy.grails.web.servlet.WrappedResponseHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.context.ApplicationContext

import grails.util.GrailsWebUtil

/**
 * based on the class of same name in grails-rendering and private class in grails-mail
 */
class RenderEnvironment {
	static log = org.apache.log4j.Logger.getLogger(RenderEnvironment.class) 
	
	final Writer out
	final Locale locale
	final ApplicationContext applicationContext

	private originalRequestAttributes = null
	private renderRequestAttributes

	RenderEnvironment(ApplicationContext applicationContext, Writer out, Locale locale = null) {
		this.out = out
		this.locale = locale
		this.applicationContext = applicationContext
	}
	
	private initCopy() {
		originalRequestAttributes = RequestContextHolder.getRequestAttributes()
		
		def renderLocale = locale
		if (!renderLocale && originalRequestAttributes) {
			renderLocale = RequestContextUtils.getLocale(originalRequestAttributes.request)
		}
		renderRequestAttributes = bindRequest(applicationContext,  out, renderLocale)
		
		if (originalRequestAttributes) {
			renderRequestAttributes.controllerName = originalRequestAttributes.controllerName
		}

	}

	private close() {
		RequestContextHolder.setRequestAttributes(originalRequestAttributes) // null ok
		WrappedResponseHolder.wrappedResponse = originalRequestAttributes?.currentResponse
	}

	/**
	 * Establish an environment inheriting the locale of the current request if there is one
	 */
	static withNew(ApplicationContext applicationContext, Writer out, Closure block) {
		with(applicationContext, out, null, block)
	}

	/**
	 * Establish an environment with a specific locale
	 */
	static withNew(ApplicationContext applicationContext, Writer out, Locale locale, Closure block) {
		def env = new RenderEnvironment(applicationContext, out, locale)
		env.initCopy()
		try {
			block(env)
		} finally {
			env.close()
		}
	}

	String getControllerName() {
		renderRequestAttributes.controllerName
	}
	
	static def bindRequestIfNull(ApplicationContext appCtx, Writer out, Locale preferredLocale = null) {
		def grailsWebRequest = RequestContextHolder.getRequestAttributes()
		if(grailsWebRequest){
			//TODO unbindRequest = false
			log.debug("grailsWebRequest exists")
			return grailsWebRequest
		}else{
			return bindRequest( appCtx,  out,  preferredLocale ) 
		}

	}
	
	static def bindRequest(ApplicationContext appCtx, Writer wout, Locale preferredLocale = null) {
		//TODO unbindRequest = true
		log.debug("a mock grailsWebRequest is being bound")
		def grailsWebRequest = GrailsWebUtil.bindMockWebRequest(appCtx)
		//setup locale on request and in LocaleContextHolder
		LocaleContextHolder.setLocaleContext(new LocaleContext() {
			public Locale getLocale() {
				return appCtx.localeResolver.resolveLocale(grailsWebRequest.request);
			}
		});
		//LOCALE_RESOLVER_ATTRIBUTE(request attr) LOCALE_RESOLVER_BEAN_NAME(localResolver) LOCALE_SESSION_ATTRIBUTE_NAME() 
		grailsWebRequest.request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, appCtx.localeResolver)
		grailsWebRequest.request.addPreferredLocale(preferredLocale?:Locale.default)
		//setup contextPath so tags like resouce and linkTo work
		grailsWebRequest.request.contextPath = appCtx.servletContext.contextPath
		//setup the default out
		grailsWebRequest.setOut(wout)
		//wrap the reponse
		WrappedResponseHolder.wrappedResponse = grailsWebRequest.currentResponse
		return grailsWebRequest
	}
	
}