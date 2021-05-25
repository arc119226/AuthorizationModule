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
			/ <small>我的服務</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i> /</a></li>
			<li class="active">我的服務</li>
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
		<h1>詳細資料</h1>
		<br />
		<div class="row">
			<label class="col-sm-2">ID</label>
			<div class="col-sm-10">${clientInfo.id}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Client Id</label>
			<div class="col-sm-10">${clientInfo.clientId}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Client Secret</label>
			<div class="col-sm-10">${clientInfo.clientSecret}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Redirect Uri</label>
			<div class="col-sm-10">${clientInfo.redirectUri}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Scope</label>
			<div class="col-sm-10">
				<c:forEach var="obj" items="${scopeList}">
					<label class="control-label">
					<input class="form-check-input multiCheckScope" 
						   type="checkbox" 
						   name="multiCheckScope[]" 
						   value="${obj.scopeValue }">${obj.note }
					</label>   
				</c:forEach>
			</div>
		</div>
		<div class="row">
			<label class="col-sm-2">當前狀態</label>
			<div class="col-sm-10">${currentClientState}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">Client端類型</label>
			<div class="col-sm-10">${currentClientType}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">用途描述</label>
			<div class="col-sm-10">${clientInfo.note}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">審核回應</label>
			<div class="col-sm-10">${clientInfo.reason}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">建立日期</label>
			<div class="col-sm-10">${clientInfo.dateCreated}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">最後更新日</label>
			<div class="col-sm-10">${clientInfo.lastUpdated}</div>
		</div>
		<div class="row">
			<label class="col-sm-2">最後更新人</label>
			<div class="col-sm-10">${clientInfo.lastUpdateUser}</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="row">
					<c:if test="${clientInfo.clientState == 'init' }">
						<!-- 只有第一個狀態才能夠進行編輯 -->
						<spring:url value="/myClientInfo/${clientInfo.id}/update" var="updateUrl" />
						<button type="submit" onclick="location.href='${updateUrl}'" class="btn-lg btn-primary pull-right">編輯</button>
					</c:if>
				</div>
			</div>
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->
<script>
$(function() {
	function initScope(){
		console.log('initScope')
		var scope='${clientInfo.scope}';
		$('.multiCheckScope').each(function(){
			if((parseInt($(this).val()) & parseInt(scope)) == parseInt($(this).val())){
				$(this).prop('checked','checked')
			}
		});
	}
	initScope();
});
</script>