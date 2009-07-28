<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>
    <fm:render template="fmtemplate" model="[name: firstName]"/>
    <fm:render template="/templates/freemarker/snippet" model="[name: middleName]"/>
</body>
</html>