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

    <c:if test="${customerRequest.getId() == null || customerRequest.getId().isEmpty()}">
        <title>Add Customer</title>
    </c:if>
    <c:if test="${customerRequest.getId() != null && !customerRequest.getId().isEmpty()}">
        <title>Update Customer</title>
    </c:if>

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
                    <c:if test="${customerRequest.getId() == null || customerRequest.getId().isEmpty()}">
                        <h1 class="h3 mb-0 text-gray-800">Add Customer</h1>
                    </c:if>
                    <c:if test="${customerRequest.getId() != null && !customerRequest.getId().isEmpty()}">
                        <h1 class="h3 mb-0 text-gray-800">Update Customer</h1>
                    </c:if>
                </div>

                <div class="row">

                    <div class="col-lg-12">

                        <!-- Basic Card Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Information</h6>
                            </div>
                            <div class="card-body">
                                <form:form role="form" action="${pageContext.request.contextPath}/customers/save"
                                           method="POST" modelAttribute="customerRequest" cssClass="needs-validation"
                                           enctype="multipart/form-data">
                                    <div class="form-group">
                                        <form:label path="name" for="name">Name</form:label>
                                        <form:input path="name" type="text" cssClass="form-control" id="name"
                                                    placeholder="Name" required="required"/>
                                        <form:errors path="name" cssClass="text-danger"/>
                                    </div>

                                    <div class="form-group">
                                        <form:label path="account.email" for="email">Email</form:label>
                                        <form:input path="account.email" type="email" cssClass="form-control" id="email"
                                                    placeholder="E-mail" required="required"/>
                                        <form:errors path="account.email" cssClass="text-danger"/>
                                    </div>

                                    <div class="form-group">
                                        <form:label path="dateOfBirth" for="birthDate">Birthday</form:label>
                                        <form:input path="dateOfBirth" type="date" cssClass="form-control"
                                                    id="birthDate" value="${dateOfBirth}"/>
                                        <form:errors path="dateOfBirth" cssClass="text-danger"/>
                                    </div>

                                    <div class="form-group">
                                        <form:label path="gender" for="gender">Gender</form:label>
                                        <form:select path="gender" cssClass="form-control" id="gender">
                                            <form:option value="">Choose gender</form:option>
                                            <form:option value="MALE">Male</form:option>
                                            <form:option value="FEMALE">Female</form:option>
                                            <form:option value="OTHER">Other</form:option>
                                        </form:select>
                                    </div>

                                    <div class="form-group">
                                        <form:label path="phone" for="phoneNumber">Phone number</form:label>
                                        <form:input path="phone" type="tel" cssClass="form-control" id="phoneNumber"
                                                    placeholder="Type the phone number"/>
                                        <form:errors path="phone" cssClass="text-danger"/>
                                    </div>

                                    <div class="form-group">
                                        <form:label path="userStatus" for="status">Status</form:label>
                                        <form:select path="userStatus" cssClass="form-control" id="status"
                                                     required="required">
                                            <form:option value="">Choose status</form:option>
                                            <form:option value="ACTIVE">Active</form:option>
                                            <form:option value="INACTIVE">Inactive</form:option>
                                            <form:option value="SUSPENDED">Suspended</form:option>
                                        </form:select>
                                    </div>

                                    <div class="form-group">
                                        <label for="avatar">Avatar</label>
                                        <input name="avatar" type="file" class="form-control" id="avatar"
                                                    accept="image/png, image/jpeg"/>
                                    </div>

                                    <div class="text-center">
                                        <button type="submit" class="btn btn-primary align-self-center">Save</button>
                                    </div>
                                    <form:input path="id" type="hidden"/>
                                    <form:input path="account.id" type="hidden" value="${customerRequest.account.id}"/>
                                    <form:input path="account.password" type="hidden"
                                                value="${customerRequest.account.password}"/>
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