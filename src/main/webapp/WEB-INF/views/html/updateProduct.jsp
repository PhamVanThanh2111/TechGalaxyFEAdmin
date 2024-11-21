<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Update Product">
    <meta name="author" content="Your Name">

    <title>Update Product</title>

    <!-- Custom fonts for this template-->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/fontawesome-free/css/all.min.css' />" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
</head>

<body id="page-top">
<div id="wrapper">
    <jsp:include page="./layout/sidebar.jsp" />
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="./layout/topbar.jsp" />
            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Update Product</h1>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Product Information</h6>
                            </div>
                            <div class="card-body">
                                <!-- Form -->
                                <form:form id="updateProductForm"
                                           action="${pageContext.request.contextPath}/products/update/${product.id}"
                                           method="POST"
                                           modelAttribute="product"
                                           enctype="multipart/form-data"
                                           cssClass="needs-validation">
                                    <div>
                                        <label for="name" class="form-label font-weight-bold">Product Name</label>
                                        <form:input path="name" id="name" class="form-control form-control-user" required="required"/>
                                    </div>
                                    <div class="mt-3">
                                        <label for="trademark" class="form-label font-weight-bold">Trademark</label>
                                        <form:select path="trademark.id" class="form-control" id="trademark">
                                            <form:options items="${trademarks}" itemValue="id" itemLabel="name" />
                                        </form:select>

                                    </div>

                                    <div class="mt-4">
                                        <button class="btn btn-primary btn-user btn-block" type="submit">Update</button>
                                    </div>
                                </form:form>
                                <!-- End Form -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/jquery-easing/jquery.easing.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
</body>

</html>
