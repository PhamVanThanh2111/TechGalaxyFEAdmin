<%@ page import="iuh.fit.se.techgalaxy.frontend.admin.dto.response.CustomerResponse" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
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

    <title>Customer</title>

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

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../General/Sidebar.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="../General/Topbar.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Customers</h1>
                <p class="mb-4">This is a customers management table, displaying user data such as name, phone, gender, birth and so on.</p>

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Data Customer</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>User Status</th>
                                    <th>Phone</th>
                                    <th>Gender</th>
                                    <th>Date of birth</th>
                                    <th>Avatar</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <tfoot>
                                <tr>
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>User Status</th>
                                    <th>Phone</th>
                                    <th>Gender</th>
                                    <th>Date of birth</th>
                                    <th>Avatar</th>
                                    <th>Action</th>
                                </tr>
                                </tfoot>
                                <tbody>
                                <c:if test="${customers != null}">
                                    <c:forEach items="${customers}" var="customer">
                                        <tr>
                                            <td>${customer.id}</td>
                                            <td>${customer.name}</td>
                                            <td>${customer.userStatus}</td>
                                            <td>${customer.phone}</td>
                                            <td>${customer.gender}</td>
                                            <td>
                                                <%
                                                    Object obj = pageContext.findAttribute("customer");
                                                    CustomerResponse customerResponse = (CustomerResponse) obj;
                                                    LocalDate dateOfBirth = customerResponse.getDateOfBirth();
                                                    if (dateOfBirth != null) {
                                                        String formatDate = dateOfBirth.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                        out.print(formatDate);
                                                    }
                                                %>
                                            </td>
                                            <td><img src="<c:url value="/img/${customer.avatar}"/>" alt="avatar"
                                                     width="55" height="55"></td>
                                            <td style="width: 18%">
                                                <a
                                                    href="${pageContext.request.contextPath}/customers/update/${customer.id}"
                                                    class="btn btn-warning btn-sm">Update</a>
                                                <a href="${pageContext.request.contextPath}/customers/delete/${customer.id}" class="btn btn-danger btn-sm" data-toggle="modal"
                                                   data-target="#deleteUserModal_${customer.id}">Delete</a>
                                                <a href="${pageContext.request.contextPath}/customers/detail/${customer.id}"
                                                   class="btn btn-info btn-sm">Detail</a>

                                                <!-- Delete User Modal-->
                                                <div class="modal fade" id="deleteUserModal_${customer.id}" tabindex="-1" role="dialog" aria-labelledby="deleteLabel"
                                                     aria-hidden="true">
                                                    <div class="modal-dialog" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="deleteLabel">Ready to delete?</h5>
                                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">×</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">Select "Yes" below if you are ready to delete this user.</div>
                                                            <div class="modal-footer">
                                                                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                                                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers/delete/${customer.id}">Yes</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                </tbody>
                            </table>
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

<!-- Page level plugins -->
<script src="<c:url value="/datatables/jquery.dataTables.min.js" />"></script>
<script src="<c:url value="/datatables/dataTables.bootstrap4.min.js" />"></script>

<!-- Page level custom scripts -->
<script src="<c:url value="/js/demo/datatables-demo.js" />"></script>

</body>

</html>