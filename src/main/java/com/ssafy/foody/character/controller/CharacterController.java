package com.ssafy.foody.character.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.character.dto.CharacterResponse;
import com.ssafy.foody.character.service.CharacterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/character")
@RequiredArgsConstructor
@Tag(name = "Character", description = "캐릭터 API")
public class CharacterController {
	private final CharacterService characterService;

	@Operation(summary = "전체 캐릭터 조회", description = "모든 캐릭터 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<List<CharacterResponse>> findAllCharacters() {
		List<CharacterResponse> characterList = characterService.findAllCharacters();
		log.debug("조회된 캐릭터 목록 : {}", characterList);
		// 데이터가 없으면 204
		if (characterList == null || characterList.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(characterList);
	}
}
