package com.ssafy.foody.character.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.character.dto.CharacterResponse;
import com.ssafy.foody.character.mapper.CharacterMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

	private final CharacterMapper characterMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<CharacterResponse> findAllCharacters() {
		return characterMapper.findAllCharacters().stream()
				.map(CharacterResponse::new)
				.toList();
	}

}
