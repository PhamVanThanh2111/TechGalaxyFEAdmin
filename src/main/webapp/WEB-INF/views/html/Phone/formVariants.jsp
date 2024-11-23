<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Variant</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css' />" rel="stylesheet">
    <style>
        #avatarPreview {
            max-width: 100%;
            max-height: 250px;
            border: 1px solid #ccc;
            margin-top: 10px;
            display: none; /* Default to hidden */
        }
    </style>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../layout/Sidebar.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="../layout/Topbar.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="d-flex align-items-center mb-4">
                    <a href="/products/${productId}/variants" class="btn btn-outline-primary btn-lg me-3">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">
                        Add New Variant
                    </h1>
                </div>

                <!-- Add Variant Form -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Add Variant</h6>
                    </div>
                    <div class="card-body">
                        <form action="/products/${productId}/variants/add" method="post" enctype="multipart/form-data">
                            <!-- Name -->
                            <div class="form-group">
                                <label for="variantName">Name</label>
                                <input type="text" class="form-control" id="variantName" name="name" placeholder="Enter variant name" required>
                            </div>

                            <!-- Description -->
                            <div class="form-group">
                                <label for="variantDescription">Description</label>
                                <textarea class="form-control" id="variantDescription" name="description" rows="3" placeholder="Enter variant description"></textarea>
                            </div>

                            <!-- Content -->
                            <div class="form-group">
                                <label for="variantContent">Content</label>
                                <textarea class="form-control" id="variantContent" name="content" rows="5" placeholder="Enter variant content"></textarea>
                            </div>

                            <!-- Avatar -->
                            <div class="form-group">
                                <label for="variantAvatar">Avatar</label>
                                <input type="file" class="form-control-file" id="variantAvatar" name="avatar" accept=".png, .jpg, .jpeg">
                                <img id="avatarPreview" alt="Avatar Preview" />
                            </div>

                            <!-- Featured -->
                            <div class="form-group">
                                <label for="variantFeatured">Featured</label>
                                <select class="form-control" id="variantFeatured" name="featured">
                                    <option value="true">Yes</option>
                                    <option value="false" selected>No</option>
                                </select>
                            </div>

                            <!-- Status -->
                            <div class="form-group">
                                <label for="variantStatus">Status</label>
                                <select class="form-control" id="variantStatus" name="status">
                                    <option value="AVAILABLE">Available</option>
                                    <option value="OUT_OF_STOCK">Out of Stock</option>
                                    <option value="DISCONTINUED">Discontinued</option>
                                </select>
                            </div>

                            <!-- Usage Category -->
                            <div class="form-group">
                                <label for="usageCategoryId">Usage Category</label>
                                <select class="form-control" id="usageCategoryId" name="usageCategoryId">
                                    <c:forEach items="${usageCategories}" var="usageCategory">
                                        <option value="${usageCategory.id}">${usageCategory.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-success btn-block">Add Variant</button>
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

<jsp:include page="../layout/LogoutModal.jsp" />

<!-- JavaScript -->
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>
<script>
    $(document).ready(() => {
        const avatarFile = $("#variantAvatar");

        // Handle file selection for new image upload
        avatarFile.change((e) => {
            const imgURL = URL.createObjectURL(e.target.files[0]);
            $("#avatarPreview").attr("src", imgURL).css("display", "block");
        });
    });
</script>
</body>

</html>
