<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	  <meta http-equiv="X-UA-Compatible" content="IE=edge">
	  <title>Developer Platform | DP</title>
	  <!-- Tell the browser to be responsive to screen width -->
	  
	  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	  <link rel="stylesheet" href="<tiles:getAsString name="cdnbase"/>/assets/bootstrap/dist/css/bootstrap.min.css">
	  <!-- Font Awesome -->
	  <link rel="stylesheet" href="<tiles:getAsString name="cdnbase"/>/assets/font-awesome/css/font-awesome.min.css">
	  <!-- Ionicons -->
	  <link rel="stylesheet" href="<tiles:getAsString name="cdnbase"/>/assets/Ionicons/css/ionicons.min.css">
	  <!-- Theme style -->
	  <link rel="stylesheet" href="<tiles:getAsString name="cdnbase"/>/assets/css/AdminLTE.min.css">
	  <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
	        page. However, you can choose any other skin. Make sure you
	        apply the skin class to the body tag so the changes take effect. -->
	  <link rel="stylesheet" href="<tiles:getAsString name="cdnbase"/>/assets/css/skins/skin-blue.min.css">
	
	  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	  <!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	  <![endif]-->
	
	  <!-- Google Font -->
	  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
			<!-- REQUIRED JS SCRIPTS -->

		<!-- jQuery 3 -->
		<script src="<tiles:getAsString name="cdnbase"/>/assets/jquery/dist/jquery.min.js"></script>
		<!-- Bootstrap 3.3.7 -->
		<script src="<tiles:getAsString name="cdnbase"/>/assets/bootstrap/dist/js/bootstrap.min.js"></script>

		<!-- Optionally, you can add Slimscroll and FastClick plugins.
		     Both of these plugins are recommended to enhance the
		     user experience. -->
	</head>
	<body class="hold-transition skin-blue sidebar-mini"><div class="wrapper">

		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="menu" />
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="sidebar" />
		<tiles:insertAttribute name="footer" />

	</div></body>
	<!-- AdminLTE App -->
	<script src="<tiles:getAsString name="cdnbase"/>/assets/js/adminlte.min.js"></script>
	<spring:url value="/resources/core/js/platform.js" var="coreJs" />
	<script src="${coreJs}"></script>	
</html>