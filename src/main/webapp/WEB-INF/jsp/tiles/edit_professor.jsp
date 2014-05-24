<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<h3>${title}</h3>
<sf:form method="post" modelAttribute="professor">
<table>
  <tr>
    <td><sf:label path="name">Name (2 to 15 characters)</sf:label></td>
    <td><sf:input path="name" /></td>
    <td><sf:errors path="name" cssClass="error" /></td>
  </tr>
  <tr>
    <td><sf:label path="surname">Surname (2 to 15 characters)</sf:label></td>
    <td><sf:input path="surname" /></td>
    <td><sf:errors path="surname" cssClass="error" /></td>
  </tr>
    <tr>
    <td colspan="3">
    Seminars:<br />
    <sf:select path="seminars" items="${allSeminars}" itemValue="id" itemLabel="name" multiple="true" >
    </sf:select>
    </td>
  </tr>
  <tr>
    <td colspan="3"><input type="submit" value="${submit_button}"/></td>
  </tr>
</table>
</sf:form>