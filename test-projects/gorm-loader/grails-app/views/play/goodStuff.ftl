[#ftl]
[#import "/testMacro.ftl" as test /]
[#import "/gormMacro.ftl" as gorm /]
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
  [#include "/gormInclude.ftl" /]<br/>
  [@test.jump "how high?" /]<br>
  [@gorm.jump "gorm jumped" /]<br>
  
</body>
</html>

