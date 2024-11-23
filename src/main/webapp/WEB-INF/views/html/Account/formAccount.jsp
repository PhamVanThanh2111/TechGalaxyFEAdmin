<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

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
                    <h1 class="h3 mb-0 text-gray-800">Update Trademark</h1>
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
                                        <div class="form-check">
                                            <c:if test="${account.rolesIds.contains('28886b1b-69bf-4331-8193-e1d86c824125')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="28886b1b-69bf-4331-8193-e1d86c824125" id="roleAdmin" checked>
                                            </c:if>
                                            <c:if test="${!account.rolesIds.contains('28886b1b-69bf-4331-8193-e1d86c824125')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="28886b1b-69bf-4331-8193-e1d86c824125" id="roleAdmin">
                                            </c:if>
                                            <label class="form-check-label" for="roleAdmin">Admin</label>
                                        </div>
                                        <div class="form-check">
                                            <c:if test="${account.rolesIds.contains('6822d329-0e58-4a7f-9ef8-e91295d15180')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="6822d329-0e58-4a7f-9ef8-e91295d15180" id="roleCustomer" checked>
                                            </c:if>
                                            <c:if test="${!account.rolesIds.contains('6822d329-0e58-4a7f-9ef8-e91295d15180')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="6822d329-0e58-4a7f-9ef8-e91295d15180" id="roleCustomer">
                                            </c:if>
                                            <label class="form-check-label" for="roleCustomer">Customer</label>
                                        </div>
                                        <div class="form-check">
                                            <c:if test="${account.rolesIds.contains('9823d890-0e58-4a7f-9ef8-e91295d15180')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="9823d890-0e58-4a7f-9ef8-e91295d15180" id="roleEmployee"
                                                       checked>
                                            </c:if>
                                            <c:if test="${!account.rolesIds.contains('9823d890-0e58-4a7f-9ef8-e91295d15180')}">
                                                <input class="form-check-input" type="checkbox" name="roles"
                                                       value="9823d890-0e58-4a7f-9ef8-e91295d15180" id="roleEmployee">
                                            </c:if>
                                            <label class="form-check-label" for="roleEmployee">Employee</label>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="password">Password</label>
                                        <form:password path="password" cssClass="form-control"
                                                       id="password" placeholder="New password" />
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
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="../login.html">Logout</a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/jquery/jquery.min.js" />"></script>
<script src="<c:url value="/bootstrap/js/bootstrap.bundle.min.js" />"></script>

<!-- Core plugin JavaScript-->
<script src="<c:url value="/jquery-easing/jquery.easing.min.js" />"></script>

<!-- Custom scripts for all pages-->
<script src="<c:url value="/js/sb-admin-2.min.js" />"></script>

</body>

</html>