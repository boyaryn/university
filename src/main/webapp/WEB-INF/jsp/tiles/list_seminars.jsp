<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>${title}</h3>
<table border="1" style="border-collapse:collapse" cellspacing="0" >
<tr><th>Name</th><th>Cost</th><th>Active</th><th>Actions</th></tr> 
  <c:forEach items="${seminars}" var="seminar">
  <tr>
    <td>${seminar.name}</td>
    <td>${seminar.cost}</td>
    <td>${seminar.active ? 'yes': 'no'}</td>
    <td>
      <a href="<c:url value="/seminar/edit/${seminar.id}" />">Edit</a>&nbsp;
      <a href="<c:url value="/seminar/delete/${seminar.id}" />">Delete</a>
    </td>
  </tr>
  </c:forEach>
</table>