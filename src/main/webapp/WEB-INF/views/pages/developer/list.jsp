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
      <small>開發者管理</small>
    </h1>
    <ol class="breadcrumb">
      <li><a href="#"><i class="fa fa-dashboard"></i> /</a></li>
      <li class="active">開發者管理</li>
    </ol>
  </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">開發者列表</h3>
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
						<th>Username</th>
						<th>Email</th>
						<th>角色類型</th>
						<th>註冊時間</th>
						<th>最後更新時間</th>
						<th>註冊Client數</th>
						<th></th>
	                </tr>
                </thead>
                <tbody>
				<c:forEach var="developer" items="${objList.result}">
					<tr>
						<td>${developer.id}</td>
						<td>${developer.username}</td>
						<td>${developer.email}</td>
						<td>${developer.userType}</td>
						<td>${developer.dateCreated}</td>
						<td>${developer.lastUpdated}</td>
						<td>${fn:length(developer.clientInfos)}</td>
						<td><spring:url value="/developer/${developer.id}" var="userUrl" />
							<spring:url value="/developer/${developer.id}/update" var="updateUrl" />
							<button class="btn btn-info" onclick="location.href='${userUrl}'">詳細資料</button>
							<button class="btn btn-primary" onclick="location.href='${updateUrl}'">編輯資料</button>
						</td>
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
	                <tr>
						<th>#ID</th>
						<th>Username</th>
						<th>Email</th>
						<th>角色類型</th>
						<th>註冊時間</th>
						<th>最後更新時間</th>
						<th>註冊Client數</th>
						<th></th>
	                </tr>
                </tfoot>
              </table>

              <ul class="pagination">
               <c:forEach var="i" begin="1" end="${(objList.totalCount/objList.pageSize)+1}" step="1">
			   		<li><a href="${pageContext.request.contextPath}/developer/list?pageNo=${i }">${i }</a></li>
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


