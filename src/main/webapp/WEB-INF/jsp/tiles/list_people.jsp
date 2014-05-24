<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>${title}</h3>
<table border="1" style="border-collapse:collapse; border-spacing: 0px;" >
<tr><th>Name</th><th>Surname</th><th>Actions</th></tr> 
  <c:forEach items="${people}" var="person">
  <tr>
    <td>${person.name}</td>
    <td>${person.surname}</td>
    <td>
      <a href="<c:url value="/${person_type}/edit/${person.personId}" />">Edit</a>&nbsp;
      <a href="<c:url value="/${person_type}/delete/${person.personId}" />">Delete</a>
    </td>
  </tr>
  </c:forEach>
</table>