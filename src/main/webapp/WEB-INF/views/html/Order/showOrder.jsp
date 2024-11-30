<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="iuh.fit.se.techgalaxy.frontend.admin.dto.response.OrderDetailResponse" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
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


    <title>Show Order</title>

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
                <h1 class="h3 mb-2 text-gray-800">Orders</h1>
                <p class="mb-4">This is an order management table, displaying user and products data such as name,
                    price, and
                    others.</p>

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Data Order</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Customer</th>
                                    <th>Employee</th>
                                    <th>Address</th>
                                    <th>Payment Status</th>
                                    <th>Order Status</th>
                                    <th>Edit Status</th>
                                </tr>
                                </thead>
                                <tfoot>
                                <tr>
                                    <th>Customer</th>
                                    <th>Employee</th>
                                    <th>Address</th>
                                    <th>Payment Status</th>
                                    <th>Order Status</th>
                                    <th>Edit Status</th>
                                </tr>
                                </tfoot>
                                <tbody>
                                <c:if test="${orders != null}">
                                    <c:forEach var="order" items="${orders}">
                                        <!-- Dòng chính của bảng -->
                                        <tr data-toggle="collapse" data-target="#collapse-${order.id}"
                                            aria-expanded="false"
                                            aria-controls="collapse-${order.id}" style="cursor: pointer;"
                                            class="card-header">
                                            <td>${order.customer.name}</td>
                                            <td>${order.systemUser.name}</td>
                                            <td>${order.address}</td>
                                            <td>${order.paymentStatus}</td>
                                            <td>${order.orderStatus}</td>
                                            <td>
                                                    <%--                                                <c:if test="${order.orderStatus == 'NEW'}">--%>
                                                        <a href="${pageContext.request.contextPath}/orders/update/${order.id}" class="btn btn-warning btn-sm">Update</a>
                                                        <a href="#" class="btn btn-success btn-sm" data-toggle="modal"
                                                   data-target="#confirmOrderModal_${order.id}">Edit</a>
                                                <!-- Confirm Order Modal-->
                                                <div class="modal fade" id="confirmOrderModal_${order.id}" tabindex="-1"
                                                     role="dialog" aria-labelledby="confirmOrderModalLabel"
                                                     aria-hidden="true">
                                                    <div class="modal-dialog" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="confirmOrderModalLabel">
                                                                    Update the status of the order!</h5>
                                                                <button class="close" type="button" data-dismiss="modal"
                                                                        aria-label="Close">
                                                                    <span aria-hidden="true">×</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">Select the statuses below if you are
                                                                sure that this order is in the appropriate status.
                                                            </div>
                                                            <div class="modal-footer">
                                                                    <%--                                                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>--%>
                                                                <div class="text-center align-middle">
                                                                    <a class="btn btn-success m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/NEW">NEW</a>
                                                                    <a class="btn btn-secondary m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/PROCESSING">PROCESSING</a>
                                                                    <a class="btn btn-info m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/SHIPPED">SHIPPED</a>
                                                                    <a class="btn btn-primary m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/DELIVERED">DELIVERED</a>
                                                                    <a class="btn btn-danger m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/CANCELLED">CANCELLED</a>
                                                                    <a class="btn btn-warning m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/RETURNED">RETURNED</a>
                                                                    <a class="btn btn-success m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/COMPLETED">COMPLETED</a>
                                                                    <a class="btn btn-danger m-1"
                                                                       href="${pageContext.request.contextPath}/orders/confirm/${order.id}/OUT_FOR_DELIVERY">OUT_FOR_DELIVERY</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                    <%--                                                </c:if>--%>
                                            </td>
                                        </tr>

                                        <!-- Dòng collapse hiển thị chi tiết -->
                                        <tr class="collapse" id="collapse-${order.id}">
                                            <td colspan="6">
                                                <div class="p-3">
                                                    <h6>Order Details:</h6>
                                                    <ul>
                                                        <c:if test="${order.orderDetails != null}">
                                                            <c:forEach var="detail" items="${order.orderDetails}">
                                                                <li>Name: ${detail.name}</li>
                                                                <li>Quantity: ${detail.quantity}</li>
                                                                <li class="mb-3">Price: <%
                                                                    double price = ((OrderDetailResponse) pageContext.getAttribute("detail")).getPrice();
                                                                    Locale vietnam = new Locale("vi", "VN");
                                                                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(vietnam);
                                                                    String formattedPrice = currencyFormat.format(price);
                                                                    out.print(formattedPrice);
                                                                %></li>
                                                            </c:forEach>
                                                        </c:if>
                                                    </ul>
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
<jsp:include page="../General/LogoutModal.jsp"/>

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