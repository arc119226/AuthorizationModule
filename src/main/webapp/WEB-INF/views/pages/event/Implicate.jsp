<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>third party grant code</title>
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
</head>
<body>
	<div id="step1">
		<div>step 1</div>
		<div>1.登入認證中心 取得 oAuth 授權碼</div> 
		<div>
			<a href="http://127.0.0.1:8080/ArcareAuthorizationModule/authorize/auth?response_type=token&client_id=45749883-c371-4e76-a64c-3318d3ee7640&redirect_uri=http://127.0.0.1:8080/ArcareAuthorizationModule/clientService/webServer
 ">登入oAuth取得grantCode</a>
		</div>
	</div>
	<div id="step2">
		<div>step 2</div>
		<div>2.提供您的oAuth授權碼 給我們</div>
		<form action="${pageContext.request.contextPath}/clientService/webServer" method="post">
			<label>${code}</label>
			<input type="hidden" name="code" value="${code}">
			<button type="submit">提交授權碼</button>
		</form>
	</div>
	<div id="step3">
		<div>step 3</div>
		<div>
			<a href="#">顯示取得的RESOURCE</a>
		</div>
	</div>
</body>