package com.webpc.fenhanvien.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webpc.fenhanvien.config.AppConfig;
import com.webpc.fenhanvien.model.auth.UserLoginResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public abstract class BaseServlet extends HttpServlet {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    protected void render(HttpServletRequest request, HttpServletResponse response, String viewPath)
        throws ServletException, IOException {
        transferFlash(request);
        request.setAttribute("backendApiBaseUrl", AppConfig.getInstance().getBackendApiBaseUrl());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
        request.getRequestDispatcher("/WEB-INF/views/" + viewPath).forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String target) throws IOException {
        response.sendRedirect(request.getContextPath() + target);
    }

    protected UserLoginResponse currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.USER);
        return value instanceof UserLoginResponse user ? user : null;
    }

    protected String currentToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.USER_TOKEN);
        return value == null ? null : value.toString();
    }

    protected Integer currentRoleId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.ROLE_ID);
        return value instanceof Integer id ? id : null;
    }

    protected String currentRoleName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.ROLE_NAME);
        return value == null ? null : value.toString();
    }

    protected boolean isAdminRole(Integer roleId, String roleName) {
        return roleId != null && (roleId == 1 || normalizedRoleName(roleName).equals("admin"));
    }

    protected boolean isSaleRole(Integer roleId, String roleName) {
        String normalized = normalizedRoleName(roleName);
        return roleId != null && (roleId == 2 || roleId == 5 || normalized.equals("sales") || normalized.equals("sale"));
    }

    protected boolean isTechRole(Integer roleId, String roleName) {
        String normalized = normalizedRoleName(roleName);
        return roleId != null && (roleId == 3 || roleId == 4 || normalized.equals("kho") || normalized.equals("tech"));
    }

    protected String resolveDashboardPath(Integer roleId, String roleName) {
        if (isAdminRole(roleId, roleName)) {
            return "/Admin/Dashboard";
        }
        if (isSaleRole(roleId, roleName)) {
            return "/Sale/Dashboard";
        }
        if (isTechRole(roleId, roleName)) {
            return "/Tech/Dashboard";
        }
        return null;
    }

    protected void flashSuccess(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(SessionKeys.FLASH_SUCCESS, message);
    }

    protected void flashError(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(SessionKeys.FLASH_ERROR, message);
    }

    protected void flashInfo(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(SessionKeys.FLASH_INFO, message);
    }

    private void transferFlash(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        moveFlash(session, request, SessionKeys.FLASH_SUCCESS, "flashSuccess");
        moveFlash(session, request, SessionKeys.FLASH_ERROR, "flashError");
        moveFlash(session, request, SessionKeys.FLASH_INFO, "flashInfo");
    }

    private void moveFlash(HttpSession session, HttpServletRequest request, String sessionKey, String requestKey) {
        Object value = session.getAttribute(sessionKey);
        if (value != null) {
            request.setAttribute(requestKey, value);
            session.removeAttribute(sessionKey);
        }
    }

    private String normalizedRoleName(String roleName) {
        return roleName == null ? "" : roleName.trim().toLowerCase(Locale.ROOT);
    }
}
