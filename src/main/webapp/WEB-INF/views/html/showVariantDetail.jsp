<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Variant Details</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="./layout/sidebar.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="./layout/topbar.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Details for Variant : <span class="text-primary">${variant.name}</span></h1>
                </div>

                <!-- Error Message -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                            ${errorMessage}
                    </div>
                </c:if>

                <!-- Variant Details Table -->
                <c:if test="${not empty detail}">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">Details</h6>
                        </div>
                        <div class="card-body">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>Status</th>
                                    <th>Memory</th>
                                    <th>Color</th>
                                    <th>Views</th>
                                    <th>Price</th>
                                    <th>Sale</th>
                                    <th>Quantity</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="detailItem" items="${detail}">
                                    <c:forEach var="memoryEntry" items="${detailItem.memories}">
                                        <c:forEach var="color" items="${memoryEntry.value}">
                                            <tr>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${detailItem.status eq 'AVAILABLE'}">
                                                            <span class="badge badge-success">Available</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-danger">Unavailable</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <!-- Hiển thị tên Memory -->
                                                <td>${memoryMap[memoryEntry.key]}</td>

                                                <!-- Hiển thị tên Color -->
                                                <td>${colorMap[color.colorId]}</td>

                                                <td>${color.viewsCount}</td>
                                                <td>
                                                    <fmt:formatNumber value="${color.price}" pattern="#,##0.00" />
                                                </td>
                                                <td>${color.sale}%</td>
                                                <td>${color.quantity}</td>
                                                <td>
                                                    <form action="/products/variants/details/update/${detailItem.id}" method="get" style="display:inline;">
                                                        <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                                    </form>
                                                    <form action="/products/variants/details/${detailItem.id}" method="get" style="display:inline;">
                                                        <button type="submit" class="btn btn-info btn-sm">Detail</button>
                                                    </form>
                                                    <form action="/products/variants/details/delete/${detailItem.id}" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this detail?');">
                                                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:forEach>
                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <!-- Back Button -->
<%--                <a href="<c:url value='/products/${variant.product.id}/variants' />" class="btn btn-secondary">--%>
<%--                    <i class="fas fa-arrow-left"></i> Back to Variants--%>
<%--                </a>--%>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<jsp:include page="./layout/LogoutModal.jsp" />

<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
</body>

</html>
