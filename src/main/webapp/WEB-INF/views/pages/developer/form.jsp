<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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

		<c:choose>
			<c:when test="${form['new']}">
				<div class="box-header">
					<h3 class="box-title">Add Developers</h3>
				</div>
			</c:when>
			<c:otherwise>
				<div class="box-header">
					<h3 class="box-title">編輯開發者資料</h3>
				</div>
			</c:otherwise>
		</c:choose>
		<br />
		<spring:url value="/developer/update" var="actionUrl" />
		<form:form class="form-horizontal" method="post" modelAttribute="form"
			action="${actionUrl}">

			<form:hidden path="id" />
			
			<form:hidden path="username" />
			

			<div class="form-group">
				<label class="col-sm-2 control-label">username</label>
				<div class="col-sm-10">
					<label class="control-label">${form.username }</label>
				</div>
			</div>


			<spring:bind path="email">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Email</label>
					<div class="col-sm-10">
						<form:input path="email" class="form-control" id="email"
							placeholder="Email" />
						<form:errors path="email" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="userType">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">角色類型</label>
					<div class="col-sm-10">
						<form:input path="userType" rows="5" class="form-control"
							id="userType" placeholder="userType...admin/dev" />
						<form:errors path="userType" class="control-label" />
					</div>
				</div>
			</spring:bind>
			

			<div class="form-group">
				<label class="col-sm-2 control-label">建立日期</label>
				<div class="col-sm-10">
					<label class="control-label">${form.dateCreated }</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">最後更新日</label>
				<div class="col-sm-10">
					<label class="control-label">${form.lastUpdated }</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">最後更新人</label>
				<div class="col-sm-10">
					<label class="control-label">${form.lastUpdateUser }</label>
				</div>
			</div>
			
			
			


			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${form['new']}">
							<button type="submit" class="btn-lg btn-primary pull-right">Add</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn-lg btn-primary pull-right">Update</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>


	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->