<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<h3>${title}</h3>
<sf:form method="post" modelAttribute="seminar" >
<table>
  <tr>
    <td><sf:label path="name">Name (2 to 30 characters)</sf:label></td>
    <td><sf:input path="name" /></td>
    <td><sf:errors path="name" cssClass="error" /></td>
  </tr>
  <tr>
    <td><sf:label path="cost">Cost (maximum 9 999)</sf:label></td>
    <td><sf:input path="cost" /></td>
    <td><sf:errors path="cost" cssClass="error" /></td>
  </tr>
  <tr>
    <td><sf:label path="active">Active (at least 1 student required)</sf:label></td>
    <td><sf:checkbox path="active" /></td>
    <td><sf:errors path="active" cssClass="error" /></td>
  </tr>
  <tr>
    <td colspan="3">
    Professor:<br />
    <sf:select path="professor">
      <sf:option value="0" label="--Select Please--" />
      <c:forEach items="${allProfessors}" var="p" varStatus="status">
        <c:choose>
            <c:when test="${p.personId eq seminar.professor.personId}">
                <option value="${p.personId}" selected="selected">${p.name} ${p.surname}</option>
            </c:when>
            <c:otherwise>
                <option value="${p.personId}">${p.name} ${p.surname}</option>
            </c:otherwise>
        </c:choose> 
    </c:forEach>
    </sf:select>
    </td>
  </tr>
  <tr>
  	<td colspan="3">
  	Students:<br />
    <sf:select path="students">
      <c:forEach items="${allStudents}" var="s_all" varStatus="status">
      	<c:remove var="selected" />
		<c:forEach items="${seminar.students}" var="s" varStatus="status">
			<c:if test="${s_all.personId eq s.personId}">
				<c:set var="selected" value="selected='true'" />
			</c:if>
		</c:forEach>
    	<option value="${s_all.personId}" ${selected} >${s_all.name} ${s_all.surname}</option>
    </c:forEach>
    </sf:select>
    </td>
  </tr>
  <tr>
    <td colspan="3"><input type="submit" /></td>
  </tr>
</table>
</sf:form>