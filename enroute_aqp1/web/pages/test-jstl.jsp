<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test JSTL</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .success { color: green; font-weight: bold; }
        .test-item { margin: 10px 0; padding: 10px; border: 1px solid #ddd; }
    </style>
</head>
<body>
    <h1>🧪 Prueba de JSTL</h1>
    
    <div class="test-item">
        <h3>1. Test básico de c:out</h3>
        <p>Resultado: <span class="success"><c:out value="¡JSTL funciona correctamente!" /></span></p>
    </div>
    
    <div class="test-item">
        <h3>2. Test de c:set y variables</h3>
        <c:set var="testVar" value="Variable creada con JSTL" />
        <p>Variable: <span class="success"><c:out value="${testVar}" /></span></p>
    </div>
    
    <div class="test-item">
        <h3>3. Test de c:if</h3>
        <c:set var="showMessage" value="true" />
        <c:if test="${showMessage}">
            <p class="success">✅ Condicional JSTL funcionando</p>
        </c:if>
    </div>
    
    <div class="test-item">
        <h3>4. Test de c:forEach</h3>
        <c:set var="items" value="Item1,Item2,Item3" />
        <ul>
            <c:forEach var="item" items="${items.split(',')}" varStatus="status">
                <li class="success">Elemento ${status.index + 1}: <c:out value="${item}" /></li>
            </c:forEach>
        </ul>
    </div>
    
    <div class="test-item">
        <h3>5. Test de Expression Language (EL)</h3>
        <p>Fecha actual: <span class="success">${pageContext.request.serverName}:${pageContext.request.serverPort}</span></p>
        <p>Context Path: <span class="success">${pageContext.request.contextPath}</span></p>
    </div>
    
    <hr>
    <p><strong>Si ves todos los elementos en verde, JSTL está funcionando perfectamente.</strong></p>
    <p><a href="${pageContext.request.contextPath}/pages/login.jsp">← Volver al Login</a></p>
</body>
</html>