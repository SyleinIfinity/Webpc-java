package com.webpc.fenhanvien.common.filter;

import com.webpc.fenhanvien.common.BaseServlet;
import com.webpc.fenhanvien.common.SessionKeys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/Admin/*", "/Sale/*", "/Tech/*"})
public class RoleGuardFilter extends BaseServlet implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        Integer roleId = session == null ? null : (Integer) session.getAttribute(SessionKeys.ROLE_ID);
        String roleName = session == null ? null : (String) session.getAttribute(SessionKeys.ROLE_NAME);

        if (roleId == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Account/Login");
            return;
        }

        String path = httpRequest.getServletPath();
        if (path.startsWith("/Admin/") && !isAdminRole(roleId, roleName)) {
            httpResponse.sendRedirect(resolveHome(httpRequest, roleId, roleName));
            return;
        }
        if (path.startsWith("/Sale/") && !isSaleRole(roleId, roleName)) {
            httpResponse.sendRedirect(resolveHome(httpRequest, roleId, roleName));
            return;
        }
        if (path.startsWith("/Tech/") && !isTechRole(roleId, roleName)) {
            httpResponse.sendRedirect(resolveHome(httpRequest, roleId, roleName));
            return;
        }

        chain.doFilter(request, response);
    }

    private String resolveHome(HttpServletRequest request, Integer roleId, String roleName) {
        String dashboardPath = resolveDashboardPath(roleId, roleName);
        if (dashboardPath != null) {
            return request.getContextPath() + dashboardPath;
        }
        return request.getContextPath() + "/Account/Login";
    }
}
