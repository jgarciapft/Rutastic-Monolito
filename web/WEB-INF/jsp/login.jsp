<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport">
    <title>Rutastic - Iniciar Sesion</title>
    <link href="${contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Fredoka+One" rel="stylesheet">
    <link rel="icon" href="${contextPath}/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${contextPath}/favicon.ico" type="image/x-icon">
    <link href="${contextPath}/fonts/fontawesome-all.min.css" rel="stylesheet">
    <link href="${contextPath}/fonts/font-awesome.min.css" rel="stylesheet">
    <link href="${contextPath}/fonts/line-awesome.min.css" rel="stylesheet">
    <link href="${contextPath}/fonts/material-icons.min.css" rel="stylesheet">
    <link href="${contextPath}/fonts/fontawesome5-overrides.min.css" rel="stylesheet">
    <link href="${contextPath}/css/footer-style.css" rel="stylesheet">
    <link href="${contextPath}/css/form-clean.css" rel="stylesheet">
    <link href="${contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
<div id="wrap">
    <div class="clear-top" id="main">
        <div class="container my-5 py-5">
            <div class="row d-xl-flex justify-content-xl-center">
                <div class="col-5 login-clean">
                    <form id="form-login" class="needs-validation"
                          method="post" action="${contextPath}/Login.do" novalidate>
                        <h1 class="text-center pt-5 pb-3"><span class="rutastic-brand">Rutastic</span></h1>
                        <div class="d-xl-flex justify-content-xl-center mb-5">
                            <img alt="Rutastic logo"
                                 class="align-items-xl-center"
                                 src="${contextPath}/img/Rutastic-logo-150px.png">
                        </div>
                        <c:if test="${requestScope.loginError != null}">
                            <div class="alert alert-danger mb-5 mx-5" role="alert">
                                <span class="font-weight-bold">
                                    Combinación de usuario y contraseña incorrecta o el usuario no existe
                                </span>
                            </div>
                        </c:if>
                        <div class="form-group px-5 mb-n1">
                            <input aria-label="Nombre de usuario"
                                   class="form-control"
                                   id="usuario-in"
                                   name="usuario"
                                   placeholder="Nombre de usuario"
                                   type="text"
                                   required>
                            <div class="invalid-feedback">
                                Introduzca un nombre de usuario
                            </div>
                        </div>
                        <div class="form-group px-5 mt-1">
                            <input aria-label="Contraseña"
                                   class="form-control"
                                   id="password-in"
                                   name="password"
                                   placeholder="Contraseña"
                                   type="password"
                                   required>
                            <div class="invalid-feedback">
                                Proporcione una contraseña
                            </div>
                        </div>
                        <div class="form-group px-5">
                            <button class="btn btn-primary btn-block font-weight-bold" type="submit">INICIAR SESIÓN
                            </button>
                        </div>
                        <p class="text-center mt-5">¿Aún no eres miembro?
                            <a class="font-weight-bold" href="${contextPath}/#!/Registro">Regístrate</a>
                        </p>
                        <a class="d-block text-center font-weight-bold pb-5" href="${contextPath}/index.html">
                            Volver al inicio
                        </a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="includes/footer.jsp"/>
<script src="${contextPath}/js/jquery.min.js"></script>
<script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>