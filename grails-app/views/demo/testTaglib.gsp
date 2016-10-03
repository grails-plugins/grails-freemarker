<html>
<head>
</head>
<body>
in gsp first name from controller ${firstName}
#before fmtemplate# 
<br/>
<fm:render template="fmtemplate" model="[name: firstName]"/> 
<br/>
#after fmtemplate#
<br/>
<br/>
#before snippet# 
<br/>

<fm:render template="/templates/freemarker/snippet" model="[name: middleName]"/> 
<br/>
#after snippet#

</body>
</html>