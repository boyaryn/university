<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<sf:form method="post" modelAttribute="seminar">
<a href="<c:url value="/seminar/create" />">Create</a>&nbsp;|&nbsp;
<sf:label path="name">Name</sf:label><sf:input path="name" />
<input type="submit" value="Filter" />
</sf:form>