<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h2>Welcome, <c:out value="${pageContext.request.userPrincipal.name}"/></h2>
<p>You are signed in using Azure AD IdP-initiated SSO.</p>
<p><a href="<c:url value='/logout'/>">Logout</a></p>
</body>
</html>
