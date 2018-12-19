<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:set var="names" value="A B C, D" scope="page" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:forEach items="${pageScope.names}" var="currentName" varStatus="status">
        Family member #<c:out value="${status.count}" /> is <c:out value="${currentName}" /> <br />
    </c:forEach>

</body>
</html>
