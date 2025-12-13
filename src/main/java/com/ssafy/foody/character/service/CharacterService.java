package com.ssafy.foody.character.service;

import java.util.List;

import com.ssafy.foody.character.dto.CharacterResponse;

public interface CharacterService {
	
	// 푸디 캐릭터 조회
	List<CharacterResponse> findAllCharacters();
	
}
