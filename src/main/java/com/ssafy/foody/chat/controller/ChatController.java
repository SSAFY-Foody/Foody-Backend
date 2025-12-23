package com.ssafy.foody.chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.chat.domain.ChatMessage;
import com.ssafy.foody.chat.domain.ChatRoom;
import com.ssafy.foody.chat.dto.ChatRoomRequest;
import com.ssafy.foody.chat.dto.ChatRoomResponse;
import com.ssafy.foody.chat.service.ChatService;
import com.ssafy.foody.report.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Chat", description = "채팅 API")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ReportService reportService;

    // 채팅방 만들기 (채팅 요청)
    @Operation(summary = "채팅방 생성", description = "새로운 1:1 채팅방을 생성합니다.")
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request, Principal principal) {
        String userId = principal.getName();

        ChatRoom room = chatService.createOrGetChatRoom(userId, request.getReportId());
        return ResponseEntity.ok(room);
    }

    // 메세지 불러오기
    @Operation(summary = "채팅 메시지 조회", description = "특정 채팅방의 이전 메시지 내역을 조회합니다.")
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    // 전문가의 채팅방 목록 조회
    @Operation(summary = "전문가 채팅방 목록 조회", description = "특정 전문가가 상담 요청이 온 채팅방 목록을 조회합니다.")
    @GetMapping("/expert/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatRoomResponse>> getExpertRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getChatRooms(userDetails.getUsername()));
    }

    // 메세지 보내기
    @Operation(summary = "메세지 보내기", description = "특정 채팅방에 메시지를 전송합니다.")
    @MessageMapping("/message")
    public void sendMessage(ChatMessage message) {
        chatService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        // 알림 전송 로직
        try {
            ChatRoom room = chatService.getChatRoom(message.getRoomId());
            if (room != null) {
                String recipientId;
                // 메시지 보낸 사람이 userId면 -> recipient는 expertId
                // 메시지 보낸 사람이 expertId면 -> recipient는 userId
                if (message.getSenderId().equals(room.getUserId())) {
                    recipientId = room.getExpertId();
                } else {
                    recipientId = room.getUserId();
                }

                // 알림 페이로드 구성
                Map<String, Object> notificationPayload = new HashMap<>();
                notificationPayload.put("message", message.getMessage());
                notificationPayload.put("senderId", message.getSenderId());
                notificationPayload.put("roomId", message.getRoomId());
                notificationPayload.put("sentAt", message.getSentAt());
                
                // 레포트 생성 날짜 추가
                if (room.getReportId() != null) {
                    try {
                        String reportDate = reportService.getReportCreatedAt(room.getReportId().intValue());
                        notificationPayload.put("reportDate", reportDate);
                    } catch (Exception e) {
                        log.error("Error: " + e.getMessage());
                    }
                } else {
                    log.error("Error: room.getReportId() is null for roomId: " + room.getId());
                }

                messagingTemplate.convertAndSend("/sub/notification/" + recipientId, notificationPayload);
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }
    }

    // 채팅방 삭제 (종료)
    @Operation(summary = "채팅방 삭제", description = "특정 채팅방을 종료합니다.")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId) {
        chatService.deleteChatRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
