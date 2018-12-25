<#-- @ftlvariable name="data" type="cn.goour.model.IndexData" -->
<html>
<head>
    <link rel="stylesheet" href="/static/style.css">
</head>
<body>
<ul>
    <#list data.items as item>
        <li>${item}</li>
    </#list>
</ul>
</body>
</html>