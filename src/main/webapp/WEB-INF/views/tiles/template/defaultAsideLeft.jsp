<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

  <!-- sidebar: style can be found in sidebar.less -->
<section class="sidebar">

  <!-- Sidebar user panel (optional) -->
<div class="user-panel">
  <div class="pull-left image">
    
    <img src="//127.0.0.1/assets/img/user-default.png" class="img-circle" alt="User Image">
  </div>
  <div class="pull-left info">
    <p>${sessionScope['user']['username']}</p>
    <!-- Status -->
    <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
  </div>
</div>

<!-- search form (Optional) -->
<form action="#" method="get" class="sidebar-form">
  <div class="input-group">
    <input type="text" name="q" class="form-control" placeholder="Search...">
    <span class="input-group-btn">
        <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
        </button>
      </span>
  </div>
</form>
<!-- /.search form -->

<!-- Sidebar Menu -->
<ul class="sidebar-menu" data-widget="tree">
  <c:if test="${sessionScope['user']['userType']=='admin'}">
    <li class="header">管理區</li>
    <!-- Optionally, you can add icons to the links -->
  	<!-- admin function -->
  	<li <%-- class="active" --%>>
	  	<a href="${pageContext.request.contextPath}/developer/list"><i class="fa fa-link"></i>
	  		<span>開發者管理</span>
	  	</a>
  	</li>
  	<li>
  		<a href="${pageContext.request.contextPath}/clientInfo/initlist">
  		<i class="fa fa-link"></i> <span>編輯區</span></a>
  	</li>
  	 <li>
  		<a href="${pageContext.request.contextPath}/clientInfo/reviewinglist">
  		<i class="fa fa-link"></i> <span>待審核</span></a>
  	</li>
  	 <li>
  		<a href="${pageContext.request.contextPath}/clientInfo/readylist">
  		<i class="fa fa-link"></i> <span>審核通過</span></a>
  	</li>
  	 <li>
  		<a href="${pageContext.request.contextPath}/clientInfo/denylist">
  		<i class="fa fa-link"></i> <span>審核失敗</span></a>
  	</li>
  	 <li>
  		<a href="${pageContext.request.contextPath}/clientInfo/publishlist">
  		<i class="fa fa-link"></i> <span>已發佈</span></a>
  	</li>
  	<li>
  		<a href="${pageContext.request.contextPath}/clientInfo/stoplist">
  		<i class="fa fa-link"></i> <span>停用</span></a>
  	</li>
  </c:if>
  <c:if test="${sessionScope['user']['userType']=='dev' or sessionScope['user']['userType']=='admin'}">
     <li class="header">申請區</li>
  	 <li>
  		<a href="${pageContext.request.contextPath}/myClientInfo/add">
  		<i class="fa fa-link"></i> <span>我要註冊</span></a>
  	</li>
  	<li>
  		<a href="${pageContext.request.contextPath}/myClientInfo/list">
  		<i class="fa fa-link"></i> <span>我的服務</span></a>
  	</li>
  </c:if>
  <li class="header">其他資訊</li>
  <li class="treeview">
    <a href=""><i class="fa fa-link"></i> <span>平台資訊</span>
      <span class="pull-right-container">
          <i class="fa fa-angle-left pull-right"></i>
        </span>
    </a>
    <ul class="treeview-menu">
      <li><a href="${pageContext.request.contextPath}/products">產品</a></li>
      <li><a href="${pageContext.request.contextPath}/contactus">聯絡我們</a></li>
    </ul>
  </li>
  <li class="treeview">
    <a href=""><i class="fa fa-link"></i> <span>API 文件</span>
      <span class="pull-right-container">
          <i class="fa fa-angle-left pull-right"></i>
        </span>
    </a>
    <ul class="treeview-menu">
      <li><a href="${pageContext.request.contextPath}/">Scope</a></li>
      <li><a href="${pageContext.request.contextPath}/">Function</a></li>
    </ul>
  </li>
</ul>
<!-- /.sidebar-menu -->
</section>
<!-- /.sidebar -->
</aside>