package com.ssafy.foody.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅방 만들기 (채팅 요청)
    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request, Principal principal) {
        String userId = principal.getName();

        ChatRoom room = chatService.createOrGetChatRoom(userId, request.getReportId());
        return ResponseEntity.ok(room);
    }

    // 메세지 불러오기
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    // 전문가의 채팅방 목록 조회
    @GetMapping("/expert/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getExpertRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getChatRooms(userDetails.getUsername()));
    }

    // 메세지 보내기
    @MessageMapping("/message")
    public void sendMessage(ChatMessage message) {
        chatService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    // 채팅방 삭제 (종료)
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable String roomId) {
        chatService.deleteChatRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
