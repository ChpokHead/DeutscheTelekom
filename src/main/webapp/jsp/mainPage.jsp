<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <link rel="stylesheet" href="/css/mainPage.css">
    <title>Logiweb</title>
</head>
<body>
    <h1 class="mainTitle">Logiweb</h1>
    <form class="loginForm" action="/login" method="post">
        <input type="text" placeholder="Логин" id="username" required>
        <input type="password" placeholder="Пароль" id="password" required>
        <input type="submit" value="Войти">
    </form>
</body>
</html>
