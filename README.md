## Table of Contents
<!-- this is using the Sublime MarkdownTOC plugin to auto update it -->
<!-- MarkdownTOC autolink="true" bracket="round" depth="0" style="unordered" indent="  " autoanchor="false" -->

- [Grails 2 vs 3 versions](#grails-2-vs-3-versions)
  - [Summary](#summary)
  - [Quick Simple Example](#quick-simple-example)
  - [Grails Taglib Example \(a very verbose one\)](#grails-taglib-example-a-very-verbose-one)
- [Usage](#usage)
  - [Syntax tips \(GSP vs. FTL\)](#syntax-tips-gsp-vs-ftl)
    - [Maps vs. Hashes](#maps-vs-hashes)
    - [GSP Tags vs. FTL Tags](#gsp-tags-vs-ftl-tags)
    - [Tags as method calls vs. Method calls](#tags-as-method-calls-vs-method-calls)
  - [Services](#services)
- [Miscellaneous](#miscellaneous)
  - [Configuration](#configuration)
  - [Logging](#logging)
  - [Reserved Words](#reserved-words)
- [History](#history)

<!-- /MarkdownTOC -->
## Grails 2 vs 3 versions

As of now, the master branch is currently 2.0.x and is for Grails2 and freemarker 2.3.x (currently 2.3.25-incubating as it becomes a full apache project). Grails 3 version is on the *grails3* branch

**Grails 2 install**

comptible releases end with grails2. for example

``` compile ":freemarker:2.0.2-grails2" ```

**Grails 3 install**

``` compile "org.grails.plugins:freemarker:3.0.0" ```


### Summary

The Grails FreeMarker plugin provides support for rendering FreeMarker templates as views. 

This plugin also allows the use of custom user added TagLibs and most of the 
[Grails Tag Libraries](http://grails.org/doc/latest/guide/single.html#6.3%20Tag%20Libraries) in
[FreeMarker](http://freemarker.sourceforge.net/) templates. No JSP tag libraries are involved at
all, the plugin connects FreeMarker to Grails TagLibs system as directly as possible.

> **Warning:**
Sitemesh does not work in version 2 of this. 


### Quick Simple Example

```groovy
class DemoController {
	def index = {
		render view: 'index.ftl', model: [name: 'Jeff Beck', instrument: 'Guitar']
	}
}
```

Then you could define a FreeMarker template "grails-app/views/demo/index.ftl" that looks like this:

```html
<html>
<body>
	Name: ${name} <br/>
	Instrument: ${instrument}<br/>
</body>
</html>
```

Use your browser to navigate to [http://localhost:8080/yourApp/demo]()

Done!

> **Note:**
In FreeMarker templates, you can use < > or \[ ]. 
For more information, please consult <a href="http://freemarker.sourceforge.net/docs/app_faq.html#faq_alternative_syntax">Freemarker FAQ</a> about alternative syntax.

> **Warning:**
> Be aware that while there are many simiilarities, FreeMarker syntax can differs from GSP syntax (they are very different beasts).

### Grails Taglib Example (a very verbose one)

Create your Grails application

Assuming you have a domain class as follows:

```groovy
class MyUser{
    String username
    String password
}
```

Create a controller *MyUserController.groovy*

	def login = {
		def myUserInstance = new MyUser()
		render(view: "login", model: [myUserInstance: myUserInstance])
	}

Create your *grails-app/views/myUser/login.ftl*
The example shows both of 
```html
<#ftl>
<#if flash.message?exists><div class="message">${flash.message}</div></#if>
<@g.hasErrors bean=myUserInstance>
    <div class="errors">
        <@g.renderErrors bean=myUserInstance _as="list" />
    </div>
</@g.hasErrors> 
<@g.form action="doLogin" method="post">
    <div class="dialog">
        <table>
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="username"><@g.message code="myUser.username.label" default="Username" /></label>
                </td>
                <td valign="top" class="value ${g.hasErrors({'bean': myUserInstance, 'field': 'username'}, 'errors')}">
                    <@g.textField name="username" value=myUserInstance.username />
                </td>
            </tr>	  
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="password"><@g.message code="myUser.password.label" default="Password" /></label>
                </td>
                <td valign="top" class="value ${g.hasErrors({'bean': myUserInstance, 'field': 'password'}, 'errors')}">
                    <@g.passwordField name="password" value=myUserInstance.password />
                </td>
            </tr>
        </table>
    </div>
    <div class="buttons">
        <span class="button"><@g.submitButton name="submit" value="${g.message({'code': 'default.button.login.label', 'default': 'Login'})}" /></span>
    </div>
</@g.form>
```

Use your browser to navigate to [http://localhost:8080/test-freemaker-prj/myUser/login]()

Done!

## Usage

The plugin will try to expose all the TagLib artefacts, registered at grailsApplication.tagLibsClasses, as
user-defined directives and functions (for implementation details, please refer to
[FreeMarker FAQ](http://freemarker.sourceforge.net/docs/app_faq.html#faq_implement_function_or_macro_in_java) ).
These directives and functions will be defined at runtime and exposed as shared variables, using the same namespace
of the source taglibs, in any FreeMarker template created further.

>Warning:
>Be aware that some templates can mistakenly/inadvertently "shadow" shared variables definitions.

### Syntax tips (GSP vs. FTL)

The following items contain useful information about some of the differences between Grails Server Pages (.gsp) and
FreeMarker Templates (.ftl). Please note this is not intended to be a complete list, it's just a starting point for
begginers.

#### Maps vs. Hashes

**GSP**
```groovy
[key1: 'abc', key2: 2]
${g.createLink(action:'list')}
```


**FTL**  -- *note that you must enclose the keys names with quotes just like JSON.*  
```groovy
{'key1': 'abc', 'key2': 2}
${g.createLink({'action':'list'})}
```

#### GSP Tags vs. FTL Tags

.**gsp**  
```html
<g:includeJs script="myscript" />
```

.**ftl** 
```html
<@g.includeJs script="myscript" />  
<@g.renderErrors bean=myUserInstance _as="list" /> (notice the _as vs as)  

or  

[@g.includeJs script="myscript" /]
```

Parameter values are (arbitrary complex) expressions that are not quoted. So assuming you want to pass an integer to
foo, <\@foo bar=1 /> is good, but <\@foo bar="1" /> is wrong as it passes in the value as a string (because "1" is a
string literal, just like in Groovy). For the same reason, <\@foo bar=x+1 /> is good, but <\@foo bar="${x+1}" />
does something else.

#### Tags as method calls vs. Method calls

.gsp  
```html
<span id="title" class="label ${hasErrors(bean:book,field:'title','errors')}">Title</span>
```
.ftl -- *note that the arguments are passed in as a hash (map) enclosed in {}, also remember to enclose your key names in
quotes*  
```html
<span id="title" class="label ${g.hasErrors({'bean':book,'field':'title'},'errors')}">Title</span>
```

>Note:
>The plugin will define functions using the same namespace of the corresponding directives.
All functions will receive two parameters, at most: a hash and an evaluated string. 

### Services

There are 2 services that you can inject to help render templates. The FreemarkerViewService and FreemarkerTemplateService

FreemarkerViewService

Methods

/ * this is the one you will primarily use * /  

*	Writer render(String viewName , Map model, Writer writer = new CharArrayWriter())
*	Writer render(View view , Map model, Writer writer = new CharArrayWriter())
*	View getView(String viewName, String pluginName = null) 

The methods are safe to use in a thread or quartz job. It checks to see that a Request is bound to the thread and if not is goes through the process of binding one so that the taglibs work properly for items like g.message and g.resource


## Miscellaneous

### Configuration

Here is an example config with explanations

```groovy
grails {
    plugin {
        freemarker {
            /**
             * when referencing a freemarker view name then require the .ftl suffix
             * if this is false then every single view lookup (render view:xxx, model:yyy)
             * goes through the look up for freemarker first
             */
            requireViewSuffix = true

            /* A list of bean names that implement freemarker.cache.TemplateLoader
            'freeMarkerGrailsTemplateLoader' bean is the deafult and probably only one you will ever need
            best to configure viewResourceLocator to look as then the GrailsFreeMarkerViewResolver has access too*/
            templateLoaders = ["freeMarkerGrailsTemplateLoader"]

            viewResourceLocator{
                // a list of location paths or URLS to add to the list for searching.
                // the default here is to allow a templates dir in conf for helpful macros
                searchLocations = ["classpath:templates/"] //consistent with spring boot defaults
            }
            viewResolver {
                /*blow exception in resolver or swallow it and move on */
                //hideException = true

                /*allow access with URLs for resourceLoader outside of sandbox with url locations (file:, http:, etc)*/
                //TODO implement this
                //enableUrlLocations = false
            }

            tags {
                /* whether to enable the grails taglibs in the templates */
                enabled = true
                //list of tags to include
                //TODO implement this
                //includeTaglibs = ['g.resource','g.ttt',etc...]
            }
            //extra settings to pass through to the Freemarker Configuration
            //see http://freemarker.sourceforge.net/docs/api/freemarker/template/Configuration.html#setSetting(java.lang.String, java.lang.String)
            // && http://freemarker.sourceforge.net/docs/api/freemarker/core/Configurable.html#setSetting(java.lang.String, java.lang.String)
            /*
            configSettings {
                //for example this will set it to be even more secure.
                new_builtin_class_resolver
            }
            */

            //tries to find templates by appending the local to the name.
            //an odd feature in freemarker that is on by default but we turn it off by default
            localizedLookup = false
        }
    }
}
```

### Logging

You can enable plugin activity logging using the namespace 'grails.plugin.freemarker'.

For more information about Grails logging, please consult:
[Grails Logging](http://grails.org/doc/latest/guide/3.%20Configuration.html#3.1.2%20Logging)

### Reserved Words

FreeMarker reserved words present in Grails Core TagLibs that can cause errors to FreeMarker's parser, will be
preceded by '_' (underscore).

.gsp  
```renderErrors bean="${myUserInstance}" as="list" />```

.ftl  
```@g.renderErrors bean=myUserInstance _as="list" />```

or  
```[@g.renderErrors bean=myUserInstance _as="list" /]```

## Known issues

For Grails 3 has issues with parsing of some grails taglibs, for example `@g.form`

## History

# History
*3.0.0
	* updated for work with grails 3
* 2.0.1
	* major refactoring to work with 2.5
	* prepping for grails 3 compatability
* 1.0.0  
	* merges freemarker tags and freemarker  
	* allow plugins to be specified for searching  
* 0.7.2  
	* Deprecated classes removed  
	* Preparing the plugin to be compatible with FreeMarker plugin 0.4  
	* Packages renamed to meet the standard  
* 0.7.1  
	* Preparing the plugin to be compatible with Grails 2.0.0  
* 0.7.0  
	* Major redesign to improve the performance (breaking changes)  
	* Directives and functions will always be exposed as shared variables  
	* GroovyPageOutputStack is no longer used  
	* autoImport, defineLegacyFunctions and asSharedVariables options are no longer supported  
	* Tags that are independent of specific Servlet objects or scopes can now be used without a thread-bound request  
	* Classes deprecated  
* 0.5.8  
	* Fixed GroovyPageOutputStack usage  
	* Functions names are no longer prefixed by "_"  
	* Added EXPERIMENTAL support to expose functions and directives as shared variables  
* 0.5.9  
	* Required version of Grails was reduced from 1.3.3 to 1.2.5  
	* Exceptions thrown during the execution of FreeMarkerViewResolver.loadView() are no longer hidden  
* 0.6.0  
	* Added specialized subclass of FreeMarkerConfigurer, increasing the range of use cases for the plugin  
* 0.6.1  
	* Modified atts subclass (LinkedHashMap changed to GroovyPageAttributes)  
	* Fixed calls to closures with an additional parameter (body)  
  
  
  
  
  
  