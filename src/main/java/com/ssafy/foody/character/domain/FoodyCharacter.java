package com.ssafy.foody.character.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodyCharacter {
	private int id;
	private String name;
	private String img;
	private String description;
	private String aiLearningComment;
}
