<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <h1>
      /
      <small>編輯中</small>
    </h1>
    <ol class="breadcrumb">
      <li><a href="#"><i class="fa fa-dashboard"></i> /</a></li>
      <li class="active">編輯中的服務</li>
    </ol>
  </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">All Client in Edit state</h3>
            </div>
            <c:if test="${css!=null}">
                <div class="alert alert-success alert-dismissible">
	                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	                <h4><i class="icon fa fa-check"></i> ${css }!</h4>
	                ${msg }
            	</div>
            </c:if>
            <!-- /.box-header -->
            <div class="box-body">
              <table id="developerTable" class="table table-bordered table-hover">
                <thead>
	                <tr>
						<th>#ID</th>
						<th>Client Id</th>
						<th>Client secret</th>
						<th>Redirect uri</th>
						<th>Client state</th>
						<th>Client type</th>
						<th></th>
	                </tr>
                </thead>
                <tbody>
				<c:forEach var="clientInfo" items="${objList.result}">
					<tr>
						<td>${clientInfo.id}</td>
						<td>${clientInfo.clientId}</td>
						<td>${clientInfo.clientSecret}</td>
						<td>${clientInfo.redirectUri}</td>
						<td>${clientInfo.clientState}</td>
						<td>${clientInfo.clientType}</td>
						<td>
							<spring:url value="/clientInfo/${clientInfo.id}" var="userUrl" />
							<button class="btn btn-info" onclick="location.href='${userUrl}'">詳細資料</button>
							
							<c:if test="${clientInfo.clientState == 'init' }">
								<spring:url value="/clientInfo/${clientInfo.id}/update" var="updateUrl" />
								<button class="btn btn-primary" onclick="location.href='${updateUrl}'">編輯</button>
								<spring:url value="/clientInfo/${clientInfo.id}/reviewing" var="reviewingUrl" />
								<button class="btn btn-primary" onclick="this.disabled=true;post('${reviewingUrl}')">送出審核</button>
							</c:if>
						</td>
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
	                <tr>
						<th>#ID</th>
						<th>Client Id</th>
						<th>Client secret</th>
						<th>Redirect uri</th>
						<th>Client state</th>
						<th>Client type</th>
						<th></th>
	                </tr>
                </tfoot>
              </table>

              <ul class="pagination">
               <c:forEach var="i" begin="1" end="${(objList.totalCount/objList.pageSize)+1}" step="1">
			   		<li><a href="${pageContext.request.contextPath}/clientInfo/reviewlist?pageNo=${i }">${i }</a></li>
			   </c:forEach>
			  </ul>
			  <div>Showing ${objList.currentIndex+1} to ${(objList.currentIndex + objList.pageSize)>objList.totalCount?objList.totalCount:objList.currentIndex + objList.pageSize } of ${objList.totalCount} entries</div>
			    
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

