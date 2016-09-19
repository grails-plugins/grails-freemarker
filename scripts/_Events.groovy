import grails.util.Environment
import org.codehaus.groovy.grails.test.spock.GrailsSpecTestType

/**
 * Created by basejump on 9/16/16.
 */
String validForAppName = "freemarker"
grails3SrcDirs = ["$basedir/src/main/groovy"]

eventTestPhaseStart = { phase ->
    if (!buildConfig.grails.useGrails3FolderLayout) return
    //binding.variables.each { println it.key} 
    //binding.grailsSettings.testSourceDir = new File("/Users/basejump/source/grails/freemarker/testxxx")
    //println binding.grailsSettings.testSourceDir
    // println "grailsAppName : $grailsAppName"
    // buildConfig.each{
    //     println it
    // }
    // if (grailsAppName != validForAppName) return

    println "eventTestPhaseStart : $phase in $grailsAppName"
    String srcTestPath

    if("unit" == phase){
        copyUnitTests()
        srcTestPath = "$basedir/src/test/groovy"
    }
    else if("integration" == phase){
        copyIntTests()
        srcTestPath = "$basedir/src/integration-test/groovy"
    }
    else if("functional" == phase){
        copyFunctionalTests()
    }
}

eventTestPhaseEnd = { phase ->
    if (!buildConfig.grails.useGrails3FolderLayout) return
    
    if("unit" == phase){
        println "Clean up: DELETING $basedir/test/unit"
        ant.delete(dir:"$basedir/test/unit",failonerror:false)
    }
    else if("integration" == phase){
        println "Clean up: DELETING $basedir/test/integration"
        ant.delete(dir:"$basedir/test/integration",failonerror:false)
    }
    else if("functional" == phase){
        println "Clean up: DELETING $basedir/test/functional"
        ant.delete(dir:"$basedir/test/functional",failonerror:false)
    }
}

eventCompileStart = { x ->

     //for inline plugins add the source as its not moved from package
    for (pluginDir in projectCompiler.pluginSettings.getInlinePluginDirectories()) {
        println "getInlinePluginDirectories ${pluginDir}"
        def g3resource = new org.springframework.core.io.FileSystemResource("${pluginDir}/src/main/groovy")
        if (g3resource.exists()){
            projectCompiler.pluginSettings.compileScopePluginInfo.sourceDirectories  << g3resource
        }
    }

    //copy other resource files now too
    for (String path in grails3SrcDirs) {
        if (new File(path).exists()){
            projectCompiler.srcDirectories << path
            ant.copy(todir: buildSettings.resourcesDir, failonerror: false, preservelastmodified: true) {
                fileset(dir: path) {
                    exclude(name: '*.groovy')
                    exclude(name: '*.java')
                }
            }
        }
    }
}

eventCreatePluginArchiveStart = { stagingDir ->
    if (grailsAppName != "freemarker") return

    println "stagingDir $stagingDir"
    ant.copy(todir:"$stagingDir/src/groovy") {
        fileset(dir:"$stagingDir/src/main/groovy")
    }
    ant.delete(dir:"$stagingDir/src/main/groovy",failonerror:false)
    ant.delete(dir:"$stagingDir/src/test",failonerror:false)
    ant.delete(dir:"$stagingDir/src/integration-test",failonerror:false)
    //throw new RuntimeException("FUBAR")
   // update staging directory contents here
}

// eventCreateWarStart = { warName, stagingDir ->
//    copyResources "$stagingDir/WEB-INF/classes"
// }


private copyUnitTests(){
    println "copyUnitTests begin"
    fromG3TestDir = "$basedir/src/test/groovy"
    toG2TestDir = "$basedir/test/unit"
    copyDirClean(fromG3TestDir, toG2TestDir)
    println "copyUnitTests end"
}

private copyIntTests(){
    println "copyIntTests begin"
    fromDir = "$basedir/src/integration-test/groovy"
    toDir = "$basedir/test/integration"
    ant.delete(dir:toDir,failonerror:false) //make sure its cleared out
    ant.mkdir(dir:toDir)
    ant.copy(todir:toDir) {
        fileset(dir:fromDir){
            exclude(name:"functional/")
        }
    }
    println "copyIntTests end"
}

private copyFunctionalTests(){
    println "copyIntTests begin"
    g3TestDir = "$basedir/src/integration-test/groovy/functional"
    g2TestDir = "$basedir/test/functional/functional"
    copyDirClean(g3TestDir, g2TestDir)
    ant.copy(file:"$basedir/src/integration-test/groovy/GebConfig.groovy" ,todir:g2TestDir,failonerror:false)
    println "copyIntTests end"
}

private copyDirClean(String fromDir, String toDir){
    ant.delete(dir:toDir,failonerror:false)
    ant.mkdir(dir:toDir)
    ant.copy(todir:toDir) {
        fileset(dir:fromDir)
    }
}

//eventTestCompileStart = { type ->
//    println "Type :$type"
//    println projectTestCompiler.srcDirectories
//    //throw new RuntimeException("FUBAR")
//    for (String path in extraTestSrcDirs) {
//        projectTestCompiler.srcDirectories << path
//    }
//    println projectTestCompiler.srcDirectories
//    //copyResources buildSettings.resourcesDir
//}
//
//eventAllTestsStart = {
//    //throw new RuntimeException("FUBAR")
//    classLoader.addURL(new File("$basedir/src/test/groovy").toURL())
//}
//
////eventCreateWarStart = { warName, stagingDir ->
////    copyResources "$stagingDir/WEB-INF/classes"
////}
//
//private copyTestResources(destination) {
//    ant.copy(todir: destination,
//            failonerror: false,
//            preservelastmodified: true) {
//        for (String path in extraSrcDirs) {
//            fileset(dir: path) {
//                exclude(name: '*.groovy')
//                exclude(name: '*.java')
//            }
//        }
//    }
//}

// eventTestPhaseStart = { phase ->
//     println "eventTestPhaseStart : $phase"
//     String srcTestPath

//     if("unit" == phase){
//         //make sure directory exists
//         ant.mkdir(dir:"$basedir/test/unit")
//         def dest = new File("$basedir/test/unit")
//         copyUnitTests()
//         srcTestPath = "$basedir/src/test/groovy"
//     }
//     if("integration" == phase){
//         //make sure directory exists
//         ant.mkdir(dir:"$basedir/test/integration")
//         srcTestPath = "$basedir/src/integration-test/groovy"
//     }//copyIntTests()

//     File dest = new File(buildSettings.testClassesDir, phase)
//     File source = new File(srcTestPath)
//     if(source.exists()){
//         def type = new GrailsSpecTestType(phase,phase)
//         projectTestRunner.projectTestCompiler.compileTests(type, source, dest)
//     }
// }