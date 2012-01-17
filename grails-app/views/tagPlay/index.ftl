<#ftl>
<html>
<head>
<link rel="stylesheet" href="${g.resource({'dir': 'css', 'file': 'test.css'})}" type="text/css">
</head>
<body>

<@g.createLink action='list' /><br/>
<a href="${g.createLink({'action':'list'})}">my link</a> <br/>
	<@g.formatNumber number=1999.9 type="currency" currencyCode="USD" /> <br/>
	<@g.message code="a.b.c" args=["goober"] default="dabba doo"/><br/>
	<@g.form controller="test">
	  <@g.actionSubmit value="Submit" action="success"/>
	${g.actionSubmit({'value':'submit','action':'success'})}
	</@g.form>
</body>
</html>