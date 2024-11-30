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

    <title>Edit Variant</title>
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
                <div class="d-flex align-items-center mb-4">
                    <a href="/products" class="btn btn-outline-primary btn-lg me-3">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <h1 class="h3 mb-0 text-gray-800">
                        Edit Variant: <span class="text-primary">${variant.name}</span>
                    </h1>
                </div>

                <!-- Edit Variant Form -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Edit Variant</h6>
                    </div>
                    <div class="card-body">
                        <form action="/products/variants/update/${variant.id}" method="post" enctype="multipart/form-data">
                            <!-- Name -->
                            <div class="form-group">
                                <label for="variantName">Name</label>
                                <input type="text" class="form-control" id="variantName" name="name" value="${variant.name}" required>
                            </div>

                            <!-- Description -->
                            <div class="form-group">
                                <label for="variantDescription">Description</label>
                                <textarea class="form-control" id="variantDescription" name="description" rows="3">${variant.description}</textarea>
                            </div>

                            <!-- Content -->
                            <div class="form-group">
                                <label for="variantContent">Content</label>
                                <textarea class="form-control" id="variantContent" name="content" rows="5">${variant.content}</textarea>
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
                                    <option value="true" <c:if test="${variant.featured}">selected</c:if>>Yes</option>
                                    <option value="false" <c:if test="${!variant.featured}">selected</c:if>>No</option>
                                </select>
                            </div>

                            <!-- Status -->
                            <div class="form-group">
                                <label for="variantStatus">Status</label>
                                <select class="form-control" id="variantStatus" name="status">
                                    <option value="AVAILABLE" <c:if test="${variant.status eq 'AVAILABLE'}">selected</c:if>>Available</option>
                                    <option value="OUT_OF_STOCK" <c:if test="${variant.status eq 'OUT_OF_STOCK'}">selected</c:if>>Out of Stock</option>
                                    <option value="DISCONTINUED" <c:if test="${variant.status eq 'DISCONTINUED'}">selected</c:if>>Discontinued</option>
                                </select>
                            </div>

<%--                            usageCategoryId--%>
                            <div class="form-group">
                                <label for="usageCategoryId">Usage Category</label>
                                <select class="form-control" id="usageCategoryId" name="usageCategoryId">
                                    <c:forEach items="${usageCategories}" var="usageCategory">
                                        <option value="${usageCategory.id}" <c:if test="${variant.usageCategory.id eq usageCategory.id}">selected</c:if>>${usageCategory.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-primary btn-block">Update Variant</button>
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
<script>
    $(document).ready(() => {
        const avatarFile = $("#variantAvatar");
        const orgImage = "${variant.avatar}";

        // Check if the avatar already exists and display it
        if (orgImage) {
            const urlImage = "http://localhost:8081/storage/"+orgImage;
            $("#avatarPreview").attr("src", urlImage).css("display", "block");
        }

        // Handle file selection for new image upload
        avatarFile.change((e) => {
            const imgURL = URL.createObjectURL(e.target.files[0]);
            $("#avatarPreview").attr("src", imgURL).css("display", "block");
        });
    });
</script>
</body>

</html>
