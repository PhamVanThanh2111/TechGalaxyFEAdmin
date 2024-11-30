<%@ page import="iuh.fit.se.techgalaxy.frontend.admin.dto.response.SystemUserResponseDTO" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Profile</title>

    <!-- Custom fonts for this template-->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet"
          type="text/css">
    <link href="<c:url value="/fontawesome-free/css/all.min.css"/>" rel="stylesheet" type="text/css">
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="/css/sb-admin-2.min.css"/>" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <link href="<c:url value="/css/toast.css"/> " rel="stylesheet">

</head>

<body id="page-top">
<c:if test="${not empty successMessage}">
    <div class="toast-container position-fixed top-0 end-0 p-3">
        <div id="successToast" class="toast success-toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Success!</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                    ${successMessage}
            </div>
        </div>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="toast-container position-fixed top-0 end-0 p-3">
        <div id="errorToast" class="toast error-toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Unsuccessful!</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                    ${errorMessage}
            </div>
        </div>
    </div>
</c:if>

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
                    <h1 class="h3 mb-0 text-gray-800">Profile</h1>
                </div>

                <div class="row">

                    <div class="col-lg-12">

                        <!-- Basic Card Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Information</h6>
                            </div>
                            <div class="card-body">

                                <!-- Admin Profile Picture -->
                                <div class="mb-3 text-center">
                                    <c:if test="${systemUser.avatar != null}">
                                        <img src="<c:url value="http://localhost:8081/storage/systemUser/avatar/${systemUser.avatar}"/>"
                                             alt="avatar"
                                             class="img-thumbnail"
                                             style="width: 200px; height: 200px; border-width: 0; border-radius: 50%;">
                                    </c:if>
                                    <c:if test="${systemUser.avatar == null}">
                                        <img src="<c:url value='${systemUser.gender == "FEMALE" ? "/img/undraw_profile_1.svg" : "/img/undraw_profile.svg"}' />"
                                             alt="avatar"
                                             class="img-thumbnail"
                                             style="width: 200px; height: 200px; border-width: 0; border-radius: 50%;">
                                    </c:if>
                                </div>

                                <!-- Admin General Information -->
                                <div class="mb-3">
                                    <h6 class="font-weight-bold text-primary">Personal Information</h6>
                                    <ul class="list-group">
                                        <li class="list-group-item"><strong>Name:</strong> ${systemUser.name}</li>
                                        <li class="list-group-item"><strong>Email:</strong> ${systemUser.account.email}
                                        </li>
                                        <li class="list-group-item"><strong>Phone Number:</strong> ${systemUser.phone}
                                        </li>
                                        <li class="list-group-item"><strong>Address:</strong> ${systemUser.address}</li>
                                        <li class="list-group-item"><strong>Gender:</strong> ${systemUser.gender}</li>
                                    </ul>
                                </div>

                                <!-- Additional Admin Details -->
                                <div class="mb-3">
                                    <h6 class="font-weight-bold text-primary">Additional Information</h6>
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>
                                            <th>Attribute</th>
                                            <th>Details</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>Level</td>
                                            <td>${systemUser.level}</td>
                                        </tr>
                                        <tr>
                                            <td>Role</td>
                                            <td><%
                                                Object obj = pageContext.findAttribute("systemUser");
                                                SystemUserResponseDTO systemUser = (SystemUserResponseDTO) obj;
                                                String roleDisplay = systemUser.getAccount().getRoles().stream().map(role -> role.getName()).collect(Collectors.joining(", "));
                                                out.print(roleDisplay);
                                            %></td>
                                        </tr>
                                        <tr>
                                            <td>Create at</td>
                                            <td><%
                                                LocalDateTime createAt = systemUser.getCreatedAt();
                                                if (createAt != null) {
                                                    String formatDate = createAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                                                    out.print(formatDate);
                                                }
                                            %></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Account Settings -->
                                <div class="mb-3">
                                    <h6 class="font-weight-bold text-primary">Account Settings</h6>
                                    <ul class="list-group">
                                        <li class="list-group-item">
                                            <strong>Status:</strong> ${systemUser.systemUserStatus}</li>
                                    </ul>
                                </div>

                                <!-- Action Buttons -->
                                <div class="mb-3 text-center">
                                    <a href="#" class="btn btn-secondary" data-toggle="modal"
                                       data-target="#changePasswordModal">Change Password</a>

                                    <!-- Change password User Modal-->
                                    <div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog"
                                         aria-labelledby="deleteLabel"
                                         aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <form:form role="form" cssClass="needs-validation" method="post"
                                                           action="${pageContext.request.contextPath}/profile/save"
                                                           modelAttribute="account">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="deleteLabel">Change password?</h5>
                                                        <button class="close" type="button" data-dismiss="modal"
                                                                aria-label="Close">
                                                            <span aria-hidden="true">×</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <label for="password">Input password below</label>
                                                        <form:hidden path="id"/>
                                                        <form:hidden path="email"/>
                                                        <form:hidden path="rolesIds"/>
                                                        <form:input type="password" class="form-control"
                                                                    placeholder="Password" id="password" path="password"
                                                                    required="required"/>
                                                        <script>
                                                            document.getElementById("password").value = "";
                                                        </script>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button class="btn btn-secondary" type="button"
                                                                data-dismiss="modal">Cancel
                                                        </button>
                                                        <button type="submit" class="btn btn-primary">Yes</button>
                                                    </div>
                                                </form:form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<!-- Thêm script để hiển thị Toast -->
<script>
    var successToast = new bootstrap.Toast(document.getElementById('successToast'));
    var errorToast = new bootstrap.Toast(document.getElementById('errorToast'));

    // Hiển thị các toast
    <c:if test="${not empty successMessage}">
    successToast.show();
    </c:if>
    <c:if test="${not empty errorMessage}">
    errorToast.show();
    </c:if>
</script>

</body>

</html>