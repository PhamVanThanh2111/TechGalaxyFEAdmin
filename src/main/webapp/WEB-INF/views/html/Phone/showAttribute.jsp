<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Show Attributes</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
    <style>
        .toast-container {
            z-index: 1055;
        }

        .toast {
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            border-radius: 0.375rem;
            overflow: hidden;
        }

    </style>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">
    <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1055;">
        <!-- Success Toast -->
        <c:if test="${not empty successMessage}">
            <div id="successToast" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                            ${successMessage}
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Error Toast -->
        <c:if test="${not empty errorMessage}">
            <div id="errorToast" class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                            ${errorMessage}
                    </div>
                </div>
            </div>
        </c:if>
    </div>
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
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <a href="/products/${productId}/variants" class="btn btn-outline-primary btn-lg me-3 mr-5">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">Attributes</h1>
                    <a href="/products/${productId}/variants/${variantId}/attributes/create" class="btn btn-success btn-lg">
                        <i class="fas fa-plus"></i> Add Attribute
                    </a>
                </div>

                <!-- Attributes Table -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Attributes List</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Value</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="attribute" items="${attributes_values}">
                                    <tr>
                                        <td>${attribute.id}</td>
                                        <td>${attribute.attributeName}</td>
                                        <td>${attribute.value}</td>
                                        <td>
                                            <form action="/attributes/edit/${attribute.id}" method="get" style="display:inline;">
                                                <button type="submit" class="btn btn-primary btn-sm">Edit</button>
                                            </form>
                                            <form action="/${productId}/variants/${variantId}/attributes/delete/${attribute.id}" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this attribute?');">
                                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
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

<jsp:include page="../General/LogoutModal.jsp" />
<script>
    // Wait until DOM is fully loaded
    document.addEventListener("DOMContentLoaded", function () {
        // Initialize and show success toast
        const successToastElement = document.getElementById('successToast');
        if (successToastElement) {
            const successToast = new bootstrap.Toast(successToastElement, { delay: 5000 }); // 5s delay
            successToast.show();
        }

        // Initialize and show error toast
        const errorToastElement = document.getElementById('errorToast');
        if (errorToastElement) {
            const errorToast = new bootstrap.Toast(errorToastElement, { delay: 5000 }); // 5s delay
            errorToast.show();
        }
    });
</script>
<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
</body>

</html>