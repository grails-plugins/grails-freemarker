<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
  <#if (flash.message)?exists>
  <div style="border: 1px solid #b2d1ff; color: #006dba; margin: 10px 0 5px 0; padding 5px; background: #f3f8fc">${flash.message}</div>
  </#if>
    Name: ${name}<br/>
    State: ${state}<br/>
</body>
</html>