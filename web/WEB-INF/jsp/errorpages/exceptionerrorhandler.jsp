<%@ page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport">
    <title>Error</title>
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Fredoka+One" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Titillium+Web:400,700,900" rel="stylesheet">
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link href="${pageContext.request.contextPath}/fonts/fontawesome-all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fonts/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fonts/line-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fonts/material-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fonts/fontawesome5-overrides.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/header-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/errorpage-style.css" rel="stylesheet">
</head>
<body>
<div id="container">
    <div class="errcontainer">
        <div class="errcode">
            <h1>ERROR</h1>
        </div>
        <h2>${pageContext.exception.cause}</h2>
        <p>${pageContext.exception.message}</p>
        <a href="${pageContext.request.contextPath}/index.html">Volver al inicio</a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>