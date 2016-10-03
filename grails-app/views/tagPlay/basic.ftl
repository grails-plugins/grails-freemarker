<html>
<head>
	<meta name="layout" content="main"/>
	<link rel="stylesheet" href="${g.resource({'dir': 'css', 'file': 'test.css'})}" type="text/css">
</head>
<body>
  	<a href="${g.createLink({'action':'list'})}">my link</a> <br/>
	<@g.formatNumber number=1999.9 type="currency" currencyCode="USD" /> <br/>
	<@g.message code="a.b.c" args="goober" default="dabba doo"/><br/>
  Name: ${my.example({'name':'basejump'})}<br/>
  State: ${state}<br/>

</body>
</html>