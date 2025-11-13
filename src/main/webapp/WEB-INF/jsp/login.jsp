<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Legacy Azure AD Login</title>
</head>
<body>
<h2>Legacy Azure AD Login</h2>
<p>This application expects Azure AD to initiate SSO and post an ID token directly to <code>/login/azure/callback</code>.</p>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<p>Contact your administrator to start the IdP-initiated login from Azure.</p>
</body>
</html>
