<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html>
<head>
<title>University Seminars Subscription Website</title>
<style>
.error {
  color: #ff0000;
  font-weight: bold;
}
table.withBorders { border: 1px solid #BBB; }

</style>
</head>

<body>
<tiles:insertAttribute name="mainmenu" />
<tiles:insertAttribute name="submenu" ignore="true" />
<tiles:insertAttribute name="body" />
</body>

</html>