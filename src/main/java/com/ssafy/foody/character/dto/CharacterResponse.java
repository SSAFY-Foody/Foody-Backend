package com.ssafy.foody.character.dto;

import com.ssafy.foody.character.domain.FoodyCharacter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponse {
	private int id;
	private String name;
	private String img;
	private String description;
	
	public CharacterResponse(FoodyCharacter character) {
		this.id = character.getId();
		this.name = character.getName();
		this.img = character.getImg();
		this.description = character.getDescription();
	}
}
