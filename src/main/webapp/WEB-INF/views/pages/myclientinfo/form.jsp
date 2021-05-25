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
			/ <small>我的服務</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i> /</a></li>
			<li class="active">我的服務</li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<c:choose>
			<c:when test="${form['new']}">
				<div class="box-header">
					<h3 class="box-title">註冊 服務</h3>
				</div>
			</c:when>
			<c:otherwise>
				<div class="box-header">
					<h3 class="box-title">編輯 服務</h3>
				</div>
			</c:otherwise>
		</c:choose>
		<br />
		<spring:url value="/myClientInfo/update" var="actionUrl" />
		<form:form class="form-horizontal" method="post" modelAttribute="form" action="${actionUrl}">

			<form:hidden path="id" />
			<form:hidden path="clientId" />
			<form:hidden path="clientSecret" />
			<form:hidden path="scope" />
			<form:hidden path="clientState" />

			<div class="form-group">
				<label class="col-sm-2 control-label">#ID</label>
				<div class="col-sm-10">
					<label class="control-label">${form.id}</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">Client Id</label>
				<div class="col-sm-10">
					<label class="control-label">${form.clientId}</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">Client Secret</label>
				<div class="col-sm-10">
					<label class="control-label">${form.clientSecret}</label>
				</div>
			</div>

			<spring:bind path="redirectUri">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Redirect Uri</label>
					<div class="col-sm-10">
						<form:input path="redirectUri" type="text" class="form-control "
							id="redirectUri" placeholder="redirectUri" />
						<form:errors path="redirectUri" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<div class="form-group ">
				<label class="col-sm-2 control-label">Scope</label>
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
			
			<div class="form-group">
				<label class="col-sm-2 control-label">當前狀態</label>
				<div class="col-sm-10">
					<label class="control-label">編輯中</label>
				</div>
			</div>
			
			<spring:bind path="clientType">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Client端類型</label>
					<div class="col-sm-10">
					<form:select path="clientType" class="form-control">
						<form:options items="${clientTypeList}" />
					</form:select>
					</div>
				</div>
			</spring:bind>

			<spring:bind path="note">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">用途描述</label>
					<div class="col-sm-10">
						<form:textarea path="note" rows="5" class="form-control"
							id="note" placeholder="note" />
						<form:errors path="note" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">審核回應</label>
				<div class="col-sm-10">
					<label class="control-label">${form.reason}</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">建立日期</label>
				<div class="col-sm-10">
					<label class="control-label">${form.dateCreated}</label>
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-2 control-label">最後更新日</label>
				<div class="col-sm-10">
					<label class="control-label">${form.lastUpdated}</label>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">最後更新人</label>
				<div class="col-sm-10">
					<label class="control-label">${form.lastUpdateUser}</label>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${form['new']}">
							<button type="submit" class="btn-lg btn-primary pull-right">註冊</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn-lg btn-primary pull-right">修改</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->
<script>
$(function() {
	function initScope(){
		console.log('initScope')
		var scope=$('#scope').val();
		$('.multiCheckScope').each(function(){
			if((parseInt($(this).val()) & parseInt(scope)) == parseInt($(this).val())){
				$(this).prop('checked','checked')
			}
		});
	}
	
	function countScope(){
		var sum=0;
		console.log('changed');
		$('.multiCheckScope').each(function(){
			if( $(this).prop("checked") && $(this).val()==1 ){
				sum=1;
				return
			}
			if( $(this).prop("checked") ){
				if(sum%2!=1){
					sum+=parseInt($(this).val());
				}
			}
		})
		$('#scope').val(sum);
	}
	$('.multiCheckScope').change(function () {
	 	countScope()
	 	console.log($('#scope').val())
	 });
	
	initScope();
});
</script>




