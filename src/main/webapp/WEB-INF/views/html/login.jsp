<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content="User Login">
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

    <title>Login</title>

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="<c:url value='/css/sb-admin-2.min.css'/>" rel="stylesheet">    <style>
        .toast-container {
            z-index: 1055;
        }

        .toast {
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            border-radius: 0.375rem;
            overflow: hidden;
        }
        .alert {
            border-radius: 10px; /* Bo góc */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Đổ bóng */
        }

        .fas {
            animation: shake 0.5s ease-in-out; /* Hiệu ứng rung */
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            50% { transform: translateX(5px); }
            75% { transform: translateX(-5px); }
        }

</style>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1055;">
        <!-- Success Toast -->
        <c:if test="${not empty successMessage}">
            <div id="successToast" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                            ${successMessage}
                    </div>
                </div>
            </div>
        </c:if>


        <c:if test="${param.error == 'true'}">
            <div id="errorToast" class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                        Invalid username or password. Please try again.
                    </div>
                </div>
            </div>

        </c:if>

    </div>
    <!-- Outer Row -->
    <div class="row justify-content-center">
        <div class="col-xl-10 col-lg-12 col-md-9">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <c:if test="${param.error == 'true'}">
                    <div class="alert alert-danger p-3">
                        <div class="row align-items-center">
                            <!-- Icon -->
                            <div class="col-md-4 text-center">
                                <i class="fas fa-exclamation-triangle fa-3x text-danger"></i>
                            </div>
                            <!-- Error Message -->
                            <div class="col-md-8">
                                <div class="text-center text-md-start">
                                    <strong>Error:</strong> Invalid username or password.<br>Please try again.
                                </div>
                            </div>
                        </div>
                    </div>

                </c:if>
                <div class="card-body p-0">
                    <!-- Nested Row within Card Body -->
                    <div class="row">
                        <!-- Left Column (with Background and Error Message) -->
                        <div class="col-lg-6 d-none d-lg-block bg-login-image position-relative">
                            <img src="<c:url value='/images/logo.jpg'/>"
                                 class="img-fluid text-center translate-middle w-100 h-100"
                                 alt="Login Logo">
                        </div>

                        <!-- Right Column (Login Form) -->
                        <div class="col-lg-6">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                                </div>
                                <!-- Form Login -->
                                <form class="user" method="post" action="/login">
                                    <div class="form-group">
                                        <label for="username" class="form-label">Username</label>
                                        <input type="text" name="username" class="form-control form-control-user"
                                               id="username" placeholder="Enter Username..." required>
                                    </div>
                                    <div class="form-group">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" name="password" class="form-control form-control-user"
                                               id="password" placeholder="Enter Password..." required>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-user btn-block">
                                        Login
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<script>
    // Wait until DOM is fully loaded
    document.addEventListener("DOMContentLoaded", function () {
        // Initialize and show success toast
        const successToastElement = document.getElementById('successToast');
        if (successToastElement) {
            const successToast = new bootstrap.Toast(successToastElement, { delay: 5000 }); // 5s delay
            successToast.show();
        }

        // Initialize and show error toast
        const errorToastElement = document.getElementById('errorToast');
        if (errorToastElement) {
            const errorToast = new bootstrap.Toast(errorToastElement, { delay: 5000 }); // 5s delay
            errorToast.show();
        }
    });
</script>
<!-- Custom scripts for all pages-->
<script src="<c:url value="/js/sb-admin-2.min.js" />"></script>
<script src="<c:url value='/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/js/sb-admin-2.min.js' />"></script>

</body>
</html>
