<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Attribute Value</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
</head>

<body id="page-top">

<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../General/Sidebar.jsp" />
    <!-- End of Sidebar -->

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
                    <a href="/products/${productId}/variants/${variantId}/attributes" class="btn btn-outline-primary btn-lg">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">Create Attribute Value</h1>
                </div>

                <!-- Form -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Add Attribute Value</h6>
                    </div>
                    <div class="card-body">
                        <form action="/products/${productId}/variants/${variantId}/attributes/create" method="post">
                            <div class="form-group">
                                <label for="attributeId">Attribute</label>
                                <select class="form-control" name="attributeId" required>
                                    <option value="">-- Select Attribute --</option>
                                    <c:forEach var="attribute" items="${attributes}">
                                        <option value="${attribute.id}">${attribute.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="value">Value</label>
                                <input type="text" class="form-control" name="value" required>
                            </div>
                            <button type="submit" class="btn btn-success btn-block mt-4">Save Attribute Value</button>
                        </form>

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

<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
</body>

</html>
