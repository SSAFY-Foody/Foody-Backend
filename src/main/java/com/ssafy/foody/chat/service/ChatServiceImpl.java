package com.ssafy.foody.chat.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ssafy.foody.chat.domain.ChatMessage;
import com.ssafy.foody.chat.domain.ChatRoom;
import com.ssafy.foody.chat.dto.ChatRoomResponse;
import com.ssafy.foody.chat.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMapper chatMapper;
    private final com.ssafy.foody.report.mapper.ReportMapper reportMapper;

    @Override
    @Transactional
    public ChatRoom createOrGetChatRoom(String userId, Long reportId) {
        ChatRoom room = chatMapper.findChatRoomByReportId(reportId);
        if (room == null) {
            com.ssafy.foody.report.domain.Report report = reportMapper.selectReportDetail(reportId.intValue());
            if (report == null) {
                throw new IllegalArgumentException("Report not found");
            }

            room = ChatRoom.builder()
                    .id(UUID.randomUUID().toString())
                    .reportId(reportId)
                    .userId(userId)
                    .expertId(report.getExpertId())
                    .build();
            chatMapper.createChatRoom(room);
        }
        return room;
    }

    @Override
    @Transactional
    public void saveMessage(ChatMessage message) {
        message.setSentAt(java.time.LocalDateTime.now());
        chatMapper.saveMessage(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(String roomId) {
        return chatMapper.findMessagesByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(String expertId) {
        return chatMapper.findChatRoomsByExpertId(expertId);
    }

    @Override
    @Transactional
    public void deleteChatRoom(String roomId) {
        chatMapper.deleteChatMessages(roomId);
        chatMapper.deleteChatRoom(roomId);
    }
}
