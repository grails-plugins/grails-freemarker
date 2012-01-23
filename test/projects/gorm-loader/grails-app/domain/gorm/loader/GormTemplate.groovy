package gorm.loader

class GormTemplate {

	String name
	String template
	Date lastUpdated
	
    static constraints = {
		lastUpdated nullable:true
    }

	//these need to be implemented so that the freemarker cache can properly compare if it already has processed it
	@Override
	boolean equals(final Object that) {
		if(this.is(that)) return true
		if((that == null) || (that.getClass() != getClass())) return false
		if (this.id == null) return this.name.equals(that.name)
		return this.id.equals(that.id)
	}

	@Override
	public int hashCode() throws IllegalStateException {
		if (id == null) return this.name.hashCode()
		return this.id.hashCode()
	}
}
