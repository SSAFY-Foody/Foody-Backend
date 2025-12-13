package com.ssafy.foody.character.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.foody.character.domain.FoodyCharacter;
import com.ssafy.foody.character.dto.CharacterResponse;

@Mapper
public interface CharacterMapper {
	
	// 푸디 캐릭터 조회
	List<FoodyCharacter> findAllCharacters();
}
