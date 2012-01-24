package gorm.loader
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class GormTemplate implements Serializable {

	String name
	String template
	Date lastUpdated
	
    static constraints = {
		lastUpdated nullable:true
    }

	//these need to be implemented so that the freemarker cache can properly compare if it already has processed it
	@Override
	boolean equals(final Object other) {
	    //the contract says never null so exit fast
	    if (other == null) return false
	    //same object instance, this is the same as this == other in java
		if(this.is(other)) return true 
		//use grails instanceOf to detect proxies
		if( !other.instanceOf(GormTemplate) ) return false
		// if ident is not null then run with it
        if (this.ident() != null ) {
            return this.ident().equals(other.ident());
        }else{
            //do the business key
            return this.name.equals(other.name)
        }
	}

	@Override
	public int hashCode() throws IllegalStateException {
	    //You cannot include id here. If you do, when an object is persisted, it's hashCode changes. 
	    //If the object is in a HashSet, you'll "never" find it again.
	    //hashCode can be the non-changing subset of properties from equals.
	    //you can limit this to just one field that will hardly ever change. 
	    //hasCode does not need to be unique just the same for the same execution
	    //also keep in mind that is this is a proxy we want to call the gets to force load
	    return new HashCodeBuilder(9, 21).append(this.name).toHashCode();
	}

}
