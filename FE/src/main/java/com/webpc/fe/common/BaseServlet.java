package com.webpc.fe.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webpc.fe.config.AppConfig;
import com.webpc.fe.model.auth.UserLoginResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class BaseServlet extends HttpServlet {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    protected void render(HttpServletRequest request, HttpServletResponse response, String viewPath)
        throws ServletException, IOException {
        transferFlash(request);
        exposeCommonFlags(request);
        request.setAttribute("backendApiBaseUrl", AppConfig.getInstance().getBackendApiBaseUrl());
        request.setAttribute("rawRequestPath", resolveCurrentPath(request));
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
        request.getRequestDispatcher("/WEB-INF/views/" + viewPath).forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String target) throws IOException {
        response.sendRedirect(request.getContextPath() + target);
    }

    protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String current = resolveCurrentPath(request);
        String encoded = URLEncoder.encode(current, StandardCharsets.UTF_8);
        redirect(request, response, "/Login?returnUrl=" + encoded);
    }

    protected String resolveCurrentPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String current = uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;
        if (request.getQueryString() != null && !request.getQueryString().isBlank()) {
            current += "?" + request.getQueryString();
        }
        return current;
    }

    protected UserLoginResponse currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.USER);
        if (value instanceof UserLoginResponse user) {
            return user;
        }
        return null;
    }

    protected String currentToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionKeys.USER_TOKEN);
        return value == null ? null : value.toString();
    }

    protected boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (currentUser(request) == null || currentToken(request) == null) {
            redirectToLogin(request, response);
            return false;
        }
        return true;
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

    protected void writeJson(HttpServletResponse response, Object payload) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        OBJECT_MAPPER.writeValue(response.getWriter(), payload);
    }

    protected int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected Integer parseNullableInt(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.parseInt(value);
        } catch (Exception ex) {
            return null;
        }
    }

    private void exposeCommonFlags(HttpServletRequest request) {
        AppConfig config = AppConfig.getInstance();
        request.setAttribute("featureCartEnabled", config.isCartEnabled());
        request.setAttribute("featurePromotionsEnabled", config.isPromotionsEnabled());
        request.setAttribute("featureOrdersEnabled", config.isOrdersEnabled());
        request.setAttribute("featurePaymentEnabled", config.isPaymentEnabled());
        request.setAttribute("featureChatEnabled", config.isChatEnabled());
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
}
