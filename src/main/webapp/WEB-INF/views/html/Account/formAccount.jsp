<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="<c:url value='/images/favicon/favicon.ico' />">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/images/favicon/favicon-16x16.png' />">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/images/favicon/favicon-32x32.png' />">
    <link rel="icon" type="image/png" sizes="192x192" href="<c:url value='/images/favicon/android-chrome-192x192.png' />">
    <link rel="icon" type="image/png" sizes="512x512" href="<c:url value='/images/favicon/android-chrome-512x512.png' />">
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/images/favicon/apple-touch-icon.png' />">
    <link rel="manifest" href="<c:url value='/images/favicon/site.webmanifest' />">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="<c:url value='/images/favicon/android-chrome-192x192.png' />">
    <meta name="theme-color" content="#ffffff">

    <title>Update Account</title>

    <!-- Custom fonts for this template-->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet"
          type="text/css">
    <link href="<c:url value="/fontawesome-free/css/all.min.css"/>" rel="stylesheet" type="text/css">
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="/css/sb-admin-2.min.css" />" rel="stylesheet">

</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../General/Sidebar.jsp"/>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="../General/Topbar.jsp"/>
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Update Account</h1>
                </div>

                <div class="row">

                    <div class="col-lg-12">

                        <!-- Basic Card Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Information</h6>
                            </div>
                            <div class="card-body">
                                <form:form role="form" action="${pageContext.request.contextPath}/accounts/save"
                                           method="POST" modelAttribute="account" cssClass="needs-validation">
                                    <div class="form-group">
                                        <label>Roles</label>
                                        <c:forEach items="${roles}" var="role">
                                            <c:if test="${role.name == 'Admin'}">
                                                <div class="form-check">
                                                    <c:if test="${fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleAdmin" checked>
                                                    </c:if>
                                                    <c:if test="${!fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleAdmin">
                                                    </c:if>
                                                    <label class="form-check-label" for="roleAdmin">Admin</label>
                                                </div>
                                            </c:if>
                                            <c:if test="${role.name == 'Customer'}">
                                                <div class="form-check">
                                                    <c:if test="${fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleCustomer" checked>
                                                    </c:if>
                                                    <c:if test="${!fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleCustomer">
                                                    </c:if>
                                                    <label class="form-check-label" for="roleCustomer">Customer</label>
                                                </div>
                                            </c:if>
                                            <c:if test="${role.name == 'Employee'}">
                                                <div class="form-check">
                                                    <c:if test="${fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleEmployee" checked>
                                                    </c:if>
                                                    <c:if test="${!fn:contains(accountRoles, role.id)}">
                                                        <input class="form-check-input" type="checkbox" name="roles"
                                                               value="${role.id}" id="roleEmployee">
                                                    </c:if>
                                                    <label class="form-check-label" for="roleEmployee">Employee</label>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>

                                    <div class="form-group">
                                        <label for="password">Password</label>
                                        <form:password path="password" cssClass="form-control"
                                                       id="password" placeholder="New password"/>
                                    </div>

                                    <%-- Hidden --%>
                                    <form:hidden path="id"/>
                                    <form:hidden path="email"/>

                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary align-self-center">Save</button>
                                    </div>
                                </form:form>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->
    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<jsp:include page="../General/LogoutModal.jsp"/>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/jquery/jquery.min.js" />"></script>
<script src="<c:url value="/bootstrap/js/bootstrap.bundle.min.js" />"></script>

<!-- Core plugin JavaScript-->
<script src="<c:url value="/jquery-easing/jquery.easing.min.js" />"></script>

<!-- Custom scripts for all pages-->
<script src="<c:url value="/js/sb-admin-2.min.js" />"></script>

</body>

</html>