package com.webpc.fe.model.chat;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageViewModel {

    private String role;
    private String content;
    private LocalDateTime time;
}
