<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Show Variants</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css'/>" rel="stylesheet">
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
                <div class="d-flex align-items-center mb-4 ">
                    <a href="/products" class="btn btn-outline-primary btn-lg me-3 mr-5">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">
                        Variants for Product: <span class="text-primary fw-bold">${productName}</span>
                    </h1>
                </div>



                <!-- Variants Table -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Variants</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Featured</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="variant" items="${variants}">
                                    <tr>
                                        <td>${variant.id}</td>
                                        <td>${variant.name}</td>
                                        <td>${variant.description}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${variant.featured}">
                                                    <span class="badge badge-success">Yes</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-secondary">No</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form action="/products/variants/${variant.id}/details" method="get" style="display:inline;">
                                                <button type="submit" class="btn btn-info btn-sm">View Details</button>
                                            </form>

                                            <form action="/products/variants/edit/${variant.id}" method="get" style="display:inline;">
                                                <button type="submit" class="btn btn-primary btn-sm">Edit</button>
                                            </form>

                                            <form action="/products/variants/delete/${variant.id}" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this variant?');">
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

<jsp:include page="../layout/LogoutModal.jsp" />

<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
</body>

</html>
