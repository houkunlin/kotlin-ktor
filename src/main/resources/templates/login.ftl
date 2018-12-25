<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<#if error??>
    <p style="color: red;">${error}</p>
</#if>
<#if ok??>
    <p style="color: green;">${ok}</p>
</#if>
<form action="/login" method="post">
    <div>
        <input type="text" name="username" id="" placeholder="请输入用户名">
    </div>
    <div>
        <input type="text" name="password" id="" placeholder="请输入密码">
    </div>
    <div>
        <input type="submit" value="登录">
    </div>
</form>
</body>
</html>