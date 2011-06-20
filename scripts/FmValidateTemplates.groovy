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

/**
 * @author Daniel Henrique Alves Lima
 */
import grails.util.BuildSettingsHolder
import grails.util.GrailsUtil

includeTargets << grailsScript('_GrailsBootstrap')

target(default: 'Parses all FreeMarker templates to validate their syntax') {
    depends checkVersion, configureProxy, bootstrap

    def springCtx = grailsApp.mainContext
    def viewResolver = springCtx.getBean('freemarkerViewResolver')
    def viewConfig = springCtx.getBean('freemarkerConfig')

    def suffix = viewResolver.suffix
    def templateLoaderPath = "${BuildSettingsHolder.settings.baseDir.absolutePath}/grails-app/views"
    // TODO: Use viewConfig to derivate the loader path
    
    def baseDir = new File(templateLoaderPath)

    def scanner = ant.fileScanner {
        fileset(dir: baseDir) {
            include(name: "**/*${suffix}")
        }
    }

    def configuration = viewConfig.configuration
    for (file in scanner) {
        def templateName = file.absolutePath.substring(baseDir.absolutePath.length())
        templateName = templateName.replace('\\', '/ ')
        println "Checking template ${templateName}"
        try {
            configuration.getTemplate(templateName)
        } catch (IOException e) {
            GrailsUtil.deepSanitize(e)
            e.printStackTrace()
        }
    }

}
