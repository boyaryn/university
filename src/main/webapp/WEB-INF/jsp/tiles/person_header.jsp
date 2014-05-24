<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<sf:form method="post" modelAttribute="${person_type}">
<a href="<c:url value="/${person_type}/create" />">Create</a>&nbsp;|&nbsp;
<sf:label path="name">Name</sf:label><sf:input path="name" />
<sf:label path="surname">Surname</sf:label><sf:input path="surname" />
<input type="submit" value="Filter" />
</sf:form>