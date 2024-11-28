<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Show Customer</title>

    <!-- Custom fonts for this template -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet"
          type="text/css">
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<c:url value="/css/sb-admin-2.min.css" />" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="<c:url value="/datatables/dataTables.bootstrap4.min.css" />" rel="stylesheet">

</head>

<body>
<!-- Sidebar -->
<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

    <!-- Sidebar - Brand -->
    <a class="sidebar-brand d-flex align-items-center justify-content-center"
       href="${pageContext.request.contextPath}/">
        <div class="sidebar-brand-icon rotate-n-15">
            <i class="fas fa-laugh-wink"></i>
        </div>
        <div class="sidebar-brand-text mx-3">SP Admin <sup>2</sup></div>
    </a>

    <!-- Divider -->
    <hr class="sidebar-divider my-0">

    <!-- Nav Item - Dashboard -->
    <li class="nav-item active">
        <a class="nav-link" href="${pageContext.request.contextPath}/">
            <i class="fas fa-fw fa-tachometer-alt"></i>
            <span>Statistics</span></a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Heading -->
    <div class="sidebar-heading">
        Information
    </div>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#userSystemManage"
           aria-expanded="true" aria-controls="userManage">
            <i class="fa-solid fa-user"></i>
            <span>System User</span>
        </a>
        <div id="userSystemManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">System User Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/systemUsers">Show</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/systemUsers/add">Add</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#customerManage"
           aria-expanded="true" aria-controls="userManage">
            <i class='fas fa-users'></i>
            <span>Customer</span>
        </a>
        <div id="customerManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Customer Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/customers">Show</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/customers/add">Add</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#phoneManage"
           aria-expanded="true" aria-controls="phoneManage">
            <i class="fa-solid fa-mobile-button"></i>
            <span>Phone</span>
        </a>
        <div id="phoneManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Phone Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/products">Show</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/products/add">Add</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#orderManage"
           aria-expanded="true" aria-controls="orderManage">
            <i class="fa-solid fa-cart-shopping"></i>
            <span>Order</span>
        </a>
        <div id="orderManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Order Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/orders">Show</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/orders/add">Add</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#trademarkManage"
           aria-expanded="true" aria-controls="trademarkManage">
            <i class="fa-solid fa-trademark"></i>
            <span>Trademark</span>
        </a>
        <div id="trademarkManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Trademark Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/trademarks">Show</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/trademarks/add">Add</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#accountManage"
           aria-expanded="true" aria-controls="accountManage">
            <i class="fa-solid fa-universal-access"></i>
            <span>Account</span>
        </a>
        <div id="accountManage" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Account Manage</h6>
                <a class="collapse-item" href="${pageContext.request.contextPath}/accounts">Show</a>
            </div>
        </div>
    </li>

    <!-- Sidebar Toggler (Sidebar) -->
    <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
    </div>

</ul>
<!-- End of Sidebar -->
</body>