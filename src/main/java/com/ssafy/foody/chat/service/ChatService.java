package com.ssafy.foody.chat.service;

import java.util.List;

import com.ssafy.foody.chat.domain.ChatMessage;
import com.ssafy.foody.chat.domain.ChatRoom;
import com.ssafy.foody.chat.dto.ChatRoomResponse;

public interface ChatService {
	public ChatRoom createOrGetChatRoom(String userId, Long reportId);

	public void saveMessage(ChatMessage message);

	public List<ChatMessage> getMessages(String roomId);

	public List<ChatRoomResponse> getChatRooms(String expertId);

	public void deleteChatRoom(String roomId);
}
