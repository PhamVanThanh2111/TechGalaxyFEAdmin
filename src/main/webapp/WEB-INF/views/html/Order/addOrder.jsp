<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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

    <title>Add Order</title>

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
                    <h1 class="h3 mb-0 text-gray-800">Add Order</h1>
                </div>

                <div class="row">

                    <div class="col-lg-12">

                        <!-- Basic Card Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Order Information</h6>
                            </div>
                            <div class="card-body">
                                <form:form role="form" action="${pageContext.request.contextPath}/orders/save"
                                           method="POST" modelAttribute="order" cssClass="needs-validation">
                                    <input type="hidden" id="productCount" name="productCount" value="0">
                                    <!-- Collapsable Card Customer -->
                                    <div class="card shadow mb-4">
                                        <a href="#collapseCardCustomer" class="d-block card-header py-3"
                                           data-toggle="collapse"
                                           role="button" aria-expanded="true" aria-controls="collapseCardCustomer">
                                            <h6 class="m-0 font-weight-bold text-primary">Customer Information</h6>
                                        </a>

                                        <div class="collapse show" id="collapseCardCustomer">
                                            <div class="card-body">
                                                <div class="form-group">
                                                    <form:label path="customer.account.email"
                                                                for="email">Email</form:label>
                                                    <form:input path="customer.account.email" type="email"
                                                                placeholder="E-mail" id="email" name="email"
                                                                class="form-control" required="required"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Collapsable Card Order -->
                                    <div class="card shadow mb-4">
                                        <a href="#collapseCardOrder" class="d-block card-header py-3"
                                           data-toggle="collapse"
                                           role="button" aria-expanded="true" aria-controls="collapseCardOrder">
                                            <h6 class="m-0 font-weight-bold text-primary">Order Information</h6>
                                        </a>

                                        <div class="collapse show" id="collapseCardOrder">
                                            <div class="card-body">
                                                <a href="#" class="btn btn-primary btn-icon-split" id="addProduct">
                                                    <span class="icon text-white-50">
                                                        <i class="fa-solid fa-plus"></i>
                                                    </span>
                                                    <span class="text">Add product</span>
                                                </a>

                                                <div id="productCards" class="row"></div>

                                                <div class="form-group mt-2">
                                                    <form:label path="address"
                                                                for="address">Delivery address</form:label>
                                                    <form:input path="address" name="address" class="form-control"
                                                                id="address"
                                                                placeholder="Delivery address" required="required"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

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
<jsp:include page="../General/LogoutModal.jsp"/>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/jquery/jquery.min.js" />"></script>
<script src="<c:url value="/bootstrap/js/bootstrap.bundle.min.js" />"></script>

<!-- Core plugin JavaScript-->
<script src="<c:url value="/jquery-easing/jquery.easing.min.js" />"></script>

<!-- Custom scripts for all pages-->
<script src="<c:url value="/js/sb-admin-2.min.js" />"></script>

<script>
    const productVariants = ${productVariants};
    const memories = ${memories};
    const colors = ${colors};
</script>
<script src="<c:url value="/js/addProduct_Order.js" />"></script>

</body>

</html>