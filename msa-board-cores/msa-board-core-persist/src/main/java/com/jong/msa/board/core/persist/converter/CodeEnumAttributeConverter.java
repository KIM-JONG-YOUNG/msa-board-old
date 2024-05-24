package com.jong.msa.board.core.persist.converter;

import java.util.EnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.jong.msa.board.common.enums.CodeEnum;
import com.jong.msa.board.common.enums.CodeEnum.Gender;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CodeEnumAttributeConverter<E extends Enum<E> & CodeEnum<V>, V> implements AttributeConverter<E, V> {
 
	private final Class<E> enumType;
	
	@Override
	public V convertToDatabaseColumn(E attribute) {
		
		return (attribute == null) ? null : attribute.getCode();
	}
	
	@Override
	public E convertToEntityAttribute(V dbData) {

		return (dbData == null) ? null : EnumSet.allOf(enumType).stream()
				.filter(e -> e.getCode().equals(dbData)).findAny()
				.orElseThrow(() -> new EnumConstantNotPresentException(enumType, (String) dbData));
	}
	
	@Converter(autoApply = true)
	public static class GenderConverter extends CodeEnumAttributeConverter<Gender, Character> {

		protected GenderConverter() {
			super(Gender.class);
		}
	}

	@Converter(autoApply = true)
	public static class GroupConverter extends CodeEnumAttributeConverter<Group, Integer> {

		protected GroupConverter() {
			super(Group.class);
		}
	}

	@Converter(autoApply = true)
	public static class StateConverter extends CodeEnumAttributeConverter<State, Integer> {

		protected StateConverter() {
			super(State.class);
		}
	}

}
