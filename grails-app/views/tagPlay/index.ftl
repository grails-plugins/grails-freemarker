<#ftl>
<html>
<head>
<meta name="layout" content="main"/>
</head>
<body>

${wtf!}

<div id='LinkText'><@g.createLink action='list' controller='tagPlay' absolute=true /></div>
<a href="${g.createLink({'controller':'tagPlay','action':'sanity'})}" id='link'>sanity</a>
<br/>
	<div id='numFormat'><@g.formatNumber number=9999.9 type="currency"  /></div> <br/>
	<div id='messageCode'><@g.message code="a.b.c" args=["goober"] default="dabba doo"/></div> <br/>
	
	<#--<@g.form controller="test" id="ViewResolver">
	  <@g.actionSubmit value="Submit" action="success"/>
	</@g.form>-->
	
	<div id='repeat'>
	<@g.repeat times=3>
        Repeat ${it}
    </@g.repeat>
    </div>
	
</body>
</html>