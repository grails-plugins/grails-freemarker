package freemarker.tags.test

class MyTagLib {

    static namespace = "my"
    static returnObjectForTags = ['sum']
    
    def repeat = { attrs, body ->
        def var = attrs.var ? attrs.var : "num"
        attrs.times?.toInteger().times { num ->
            out << body((var):num)
        }
    }
    
    
    def sum = { attrs ->
        int sum = 0
        for (attValue in attrs.values()) {
            sum += attValue.toInteger()
        }
        return sum
    }
    
    
    def example = { attrs ->
        out << "example ${attrs.name}"
    }
    
    def anotherExample = {attrs ->
        out << my.example(name: 'another')
    }

    
    def specialForm = { attrs, body -> out << g.form() }
    
}
