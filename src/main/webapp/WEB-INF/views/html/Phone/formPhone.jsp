<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Add Product</title>

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
<div id="wrapper">
    <jsp:include page="../General/Sidebar.jsp" />
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <jsp:include page="../General/Topbar.jsp" />
            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Add Product</h1>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Product Information</h6>
                            </div>
                            <div class="card-body">
                                <form:form id="addProductForm" role="form" action="${pageContext.request.contextPath}/products/add"
                                           method="POST" modelAttribute="productFullRequest" cssClass="needs-validation" enctype="multipart/form-data">
                                    <!-- Thông tin cơ bản của sản phẩm -->
                                    <div class="form-group mb-3">
                                        <label for="name" class="form-label font-weight-bold">Tên Sản Phẩm</label>
                                        <form:input path="name" class="form-control form-control-user" id="name" placeholder="Nhập tên sản phẩm" required="true" />
                                        <form:errors path="name" cssClass="text-danger" />
                                    </div>

                                    <!-- Combobox Trademark -->
                                    <div class="form-group mb-3">
                                        <label for="trademark" class="form-label font-weight-bold">Trademark</label>
                                        <form:select path="trademarkId" class="form-control" id="trademark">
                                            <form:option value="" label="Chọn Trademark" />
                                            <form:options items="${trademarks}" itemValue="id" itemLabel="name" />
                                        </form:select>
                                        <form:errors path="trademarkId" cssClass="text-danger" />
                                    </div>

                                    <!-- Khu vực để thêm Variants -->
                                    <div id="variantsContainer" class="mb-3">
                                        <h4 class="font-weight-bold">Variants</h4>
                                    </div>
                                    <button type="button" class="btn btn-info mb-3" onclick="addVariant()">Thêm Variant</button>
                                    <button type="submit" class="btn btn-primary btn-user btn-block">Lưu Sản Phẩm</button>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Templates -->
<script id="usageCategoryTemplate" type="text/template">
    <option value="">Chọn Usage Category</option>
    <c:forEach items="${usageCategories}" var="category">
        <option value="${category.id}">${category.name}</option>
    </c:forEach>
</script>

<script id="memoryTemplate" type="text/template">
    <option value="">Chọn Memory</option>
    <c:forEach items="${memories}" var="memory">
        <option value="${memory.id}">${memory.name}</option>
    </c:forEach>
</script>

<script id="colorTemplate" type="text/template">
    <option value="">Chọn Color</option>
    <c:forEach items="${colors}" var="color">
        <option value="${color.id}">${color.name}</option>
    </c:forEach>
</script>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/jquery/jquery.min.js" />"></script>
<script src="<c:url value="/bootstrap/js/bootstrap.bundle.min.js" />"></script>
<script src="<c:url value="/js/add-product.js" />"></script>

<script src="<c:url value="/jquery-easing/jquery.easing.min.js" />"></script>
<script src="<c:url value="/js/sb-admin-2.min.js" />"></script>

</body>

</html>