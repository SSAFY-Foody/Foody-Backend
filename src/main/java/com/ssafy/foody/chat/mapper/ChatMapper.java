package com.ssafy.foody.chat.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.foody.chat.domain.ChatMessage;
import com.ssafy.foody.chat.domain.ChatRoom;
import com.ssafy.foody.chat.dto.ChatRoomResponse;

@Mapper
public interface ChatMapper {
    void createChatRoom(ChatRoom chatRoom); // 채팅방 생성(요청)

    ChatRoom findChatRoomByReportId(@Param("reportId") Long reportId); // 리포트 ID로 방 찾기

    ChatRoom findChatRoomById(@Param("roomId") String roomId); // room id로 방 찾기

    void saveMessage(ChatMessage message); // 메세지 저장(보내기)

    List<ChatMessage> findMessagesByRoomId(@Param("roomId") String roomId); // 채팅 기록 불러오기

    List<ChatRoomResponse> findChatRoomsByExpertId(@Param("expertId") String expertId); // 전문가의 채팅방 목록 조회

    void deleteChatMessages(@Param("roomId") String roomId); // 메세지 삭제

    void deleteChatRoom(@Param("roomId") String roomId); // 채팅방 삭제
}
