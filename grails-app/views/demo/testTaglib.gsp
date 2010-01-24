<html>
<head>
</head>
<body>
    #before fmtemplate# <fm:render template="fmtemplate" model="[name: firstName]"/> #after fmtemplate#
    #before snippet# <fm:render template="/templates/freemarker/snippet" model="[name: middleName]"/> #after snippet#
</body>
</html>