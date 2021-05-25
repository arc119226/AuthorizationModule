<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			/ <small>開發者管理</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i> /</a></li>
			<li class="active">開發者管理</li>
		</ol>
	</section>

	<!-- Main content -->
	<section class="content">
		<c:if test="${not empty msg}">
			<div class="alert alert-${css} alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<strong>${msg}</strong>
			</div>
		</c:if>
		<h1>Developer Detail</h1>
		<br />
		<div class="row">
			<label class="col-sm-2">ID</label>
			<div class="col-sm-10">${developer.id}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Username</label>
			<div class="col-sm-10">${developer.username}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Email</label>
			<div class="col-sm-10">${developer.email}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">角色類型</label>
			<div class="col-sm-10">${developer.userType}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">建立日期</label>
			<div class="col-sm-10">${developer.dateCreated}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">最後更新日</label>
			<div class="col-sm-10">${developer.lastUpdated}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">最後更新人</label>
			<div class="col-sm-10">${developer.lastUpdateUser}</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="row">
					<spring:url value="/developer/${developer.id}/update" var="updateUrl" />
					<button type="submit" onclick="location.href='${updateUrl}'" class="btn-lg btn-primary pull-right">Update</button>
				</div>
			</div>
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->