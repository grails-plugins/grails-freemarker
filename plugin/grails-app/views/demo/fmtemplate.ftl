[#ftl]
The template at /demo/fmtemplate.ftl was rendered with Name from model: ${name}
<br/>
and first name from controller model ${firstName!}
<br/>
all off these includes should work similar to how one would expect in grails templates
<br/>
Relative link "included.ftl"<br/>
1-[#include "included.ftl"]
<br/>
absolute link "/demo/included.ftl"<br/>
2-[#include "/demo/included.ftl"]2
<br/>
absolute link to another view directory<br/>
3-[#include "/pluginTest/override.ftl"]<br/>
realtive link to "subdir/subdir.ftl"<br/>
4-[#include "subdir/subdir.ftl"]