package iuh.fit.se.techgalaxy.frontend.admin.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accessToken") == null) {
            System.out.println("No access token found in session. Redirecting to /login.");
            response.sendRedirect("/login?error=true");
            return false;
        }

        System.out.println("Access Token in Interceptor: " + session.getAttribute("accessToken"));

        return true;
    }
}


