package com.webpc.fe.service;

import com.webpc.fe.common.SessionKeys;
import com.webpc.fe.model.chat.ChatMessageViewModel;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatSessionService {

    public List<ChatMessageViewModel> getHistory(HttpSession session) {
        if (session == null) {
            return new ArrayList<>();
        }
        Object value = session.getAttribute(SessionKeys.CHAT_HISTORY);
        if (value instanceof List<?> raw) {
            List<ChatMessageViewModel> result = new ArrayList<>();
            for (Object row : raw) {
                if (row instanceof ChatMessageViewModel message) {
                    result.add(message);
                }
            }
            return result;
        }
        List<ChatMessageViewModel> init = new ArrayList<>();
        session.setAttribute(SessionKeys.CHAT_HISTORY, init);
        return init;
    }

    public ChatMessageViewModel addUserMessage(HttpSession session, String content) {
        ChatMessageViewModel msg = new ChatMessageViewModel();
        msg.setRole("user");
        msg.setContent(content);
        msg.setTime(LocalDateTime.now());
        persist(session, msg);
        return msg;
    }

    public ChatMessageViewModel addBotMessage(HttpSession session, String content) {
        ChatMessageViewModel msg = new ChatMessageViewModel();
        msg.setRole("model");
        msg.setContent(content);
        msg.setTime(LocalDateTime.now());
        persist(session, msg);
        return msg;
    }

    public String generateReply(String message) {
        if (message == null || message.isBlank()) {
            return "Bạn hãy nhập nội dung cần tư vấn, mình sẽ hỗ trợ chọn linh kiện phù hợp.";
        }

        String lower = message.toLowerCase(Locale.ROOT);
        if (lower.contains("cpu")) {
            return "Nếu bạn ưu tiên chơi game, hãy chọn CPU có xung cao và tối thiểu 6 nhân. Mình có thể gợi ý theo ngân sách cụ thể.";
        }
        if (lower.contains("vga") || lower.contains("card")) {
            return "VGA nên đi cùng nguồn đạt chuẩn và case thoáng. Bạn có thể cho mình biết độ phân giải màn hình để gợi ý chính xác hơn.";
        }
        if (lower.contains("ram")) {
            return "RAM nên tối thiểu 16GB cho nhu cầu hiện tại. Nếu làm đồ họa hoặc stream, nên cân nhắc 32GB.";
        }
        if (lower.contains("ssd")) {
            return "Bạn nên ưu tiên SSD NVMe cho tốc độ tốt. Mốc phổ biến là 500GB hoặc 1TB.";
        }
        if (lower.contains("khuyen mai") || lower.contains("voucher")) {
            return "Bạn mở mục Khuyến mãi để lưu mã trước khi thanh toán, hệ thống sẽ tự áp nếu đủ điều kiện đơn hàng.";
        }
        if (lower.contains("bao hanh")) {
            return "Chính sách bảo hành phụ thuộc theo từng linh kiện, bạn gửi mã sản phẩm để mình kiểm tra nhanh cho chính xác.";
        }

        return "Mình đã nhận câu hỏi. Bạn có thể nói rõ thêm ngân sách, mục đích dùng máy và linh kiện ưu tiên để mình tư vấn chi tiết.";
    }

    private void persist(HttpSession session, ChatMessageViewModel message) {
        List<ChatMessageViewModel> history = getHistory(session);
        history.add(message);
        session.setAttribute(SessionKeys.CHAT_HISTORY, history);
    }
}
