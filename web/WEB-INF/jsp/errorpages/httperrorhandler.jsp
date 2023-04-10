<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport">
    <title>Error ${pageContext.response.status}</title>
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
            <h1>${pageContext.response.status}</h1>
        </div>
        <h2>
            <c:choose>
                <c:when test="${pageContext.response.status == 400}">Mala petición</c:when>
                <c:when test="${pageContext.response.status == 401}">No autorizado</c:when>
                <c:when test="${pageContext.response.status == 403}">Prohibido</c:when>
                <c:when test="${pageContext.response.status == 404}">
                    Oops! La página o el recurso que buscas no pudo ser encontrado
                </c:when>
                <c:when test="${pageContext.response.status == 405}">Método no permitido</c:when>
                <c:when test="${pageContext.response.status == 500}">Error interno del servidor</c:when>
                <c:otherwise>Algo fue mal</c:otherwise>
            </c:choose>
        </h2>
        <p>
            <c:choose>
                <c:when test="${pageContext.response.status == 400}">
                    Realizó una petición mal formada. Revise la sintaxis de la petición y pruebe de nuevo
                </c:when><c:when test="${pageContext.response.status == 401}">
                Su usuario no tiene permiso para realizar la operación solicitada. Pruebe a iniciar sesión con una
                cuenta distinta
            </c:when>
                <c:when test="${pageContext.response.status == 403}">Operación prohibida</c:when>
                <c:when test="${pageContext.response.status == 404}">
                    Lo sentimos, la página que buscas no existe, ha sido eliminada, cambió de nombre o no está disponible
                    temporalmente
                </c:when>
                <c:when test="${pageContext.response.status == 405}">
                    Método HTTP incompatible con el recurso solicitado
                </c:when>
                <c:when test="${pageContext.response.status == 500}">Algo fue mal</c:when>
                <c:otherwise>Pruebe más tarde</c:otherwise>
            </c:choose>
        </p>
        <a href="${pageContext.request.contextPath}/index.html">Volver al inicio</a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>