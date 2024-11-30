<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

    <title>Add Variant Detail</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
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
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <a href="/products/${productId}/variants/${variantId}/details" class="btn btn-outline-primary btn-lg me-3">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">Add Variant Detail</h1>
                </div>

                <!-- Add Variant Detail Form -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Add Details</h6>
                    </div>
                    <div class="card-body">
                        <form action="/products/${productId}/variants/${variantId}/details/add" method="post" enctype="multipart/form-data">
                            <!-- Memory -->
                            <div class="form-group">
                                <label for="memid">Memory</label>
                                <select class="form-control" id="memid" name="memid" required>
                                    <c:forEach var="memory" items="${memories}">
                                        <option value="${memory.id}">${memory.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Price -->
                            <div class="form-group">
                                <label for="price">Price</label>
                                <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" required>
                            </div>

                            <!-- Sale -->
                            <div class="form-group">
                                <label for="sale">Sale (%)</label>
                                <input type="number" class="form-control" id="sale" name="sale" step="0.01" min="0" max="100" required>
                            </div>

                            <!-- Colors -->
                            <div id="color-container">
                                <!-- Initial color group -->
                                <div class="color-group mt-4">
                                    <h5>Color 1</h5>
                                    <div class="form-group">
                                        <label for="colors[0].colorId">Color</label>
                                        <select class="form-control" id="colors[0].colorId" name="colors[0].colorId" required>
                                            <option value="">-- Select Color --</option>
                                            <c:forEach items="${availableColors}" var="color">
                                                <option value="${color.id}">${color.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="colors[0].quantity">Quantity</label>
                                        <input type="number" class="form-control" id="colors[0].quantity" name="colors[0].quantity" min="1" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="colors[0].images">Images</label>
                                        <input type="file" class="form-control-file" id="colors[0].images" name="colors[0].images" multiple>
                                    </div>
                                </div>
                            </div>

                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-success btn-block mt-4">Save Details</button>
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
