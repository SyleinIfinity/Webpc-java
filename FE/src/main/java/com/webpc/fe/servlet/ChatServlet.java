package com.webpc.fe.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.webpc.fe.common.BaseServlet;
import com.webpc.fe.service.ChatSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {
    "/Chat/Send",
    "/Chat/History"
})
public class ChatServlet extends BaseServlet {

    private final ChatSessionService chatService = new ChatSessionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeJson(response, Map.of("items", chatService.getHistory(request.getSession())));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        JsonNode body = OBJECT_MAPPER.readTree(request.getInputStream());
        String message = body.path("message").asText("");

        if (message.isBlank()) {
            writeJson(response, Map.of("success", false, "message", "Noi dung chat khong duoc de trong."));
            return;
        }

        chatService.addUserMessage(request.getSession(), message);
        String reply = chatService.generateReply(message);
        var bot = chatService.addBotMessage(request.getSession(), reply);

        writeJson(response, Map.of(
            "success", true,
            "reply", bot
        ));
    }
}
