package gorm.loader
import freemarker.cache.TemplateLoader

class GormLoaderService implements TemplateLoader {

	Object findTemplateSource(String templateName) throws IOException {
		log.debug "looking for $templateName"
	    return GormTemplate.findByName(templateName, [cache: true])
	}

	Reader getReader(Object templateSource, String encoding) throws IOException {
		log.debug "returning  for $templateSource.template"
		return new StringReader(templateSource.template) 
	}


	long getLastModified(Object templateSource) {
		log.debug "returning for $templateSource.lastUpdated"
		return templateSource.lastUpdated.getTime()
	}

	public void closeTemplateSource(Object templateSource) throws IOException {}
	
}





