package org.skypro.recommendation_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.skypro.recommendation_service.model.dto.DynamicRuleRequest;
import org.skypro.recommendation_service.model.dto.DynamicRuleResponse;
import org.skypro.recommendation_service.model.entity.DynamicRule;

@Mapper(componentModel = "spring")
public interface DynamicRuleMapper {

    DynamicRuleMapper INSTANCE = Mappers.getMapper(DynamicRuleMapper.class);

    DynamicRuleResponse toResponse(DynamicRule entity);

    DynamicRule toEntity(DynamicRuleRequest request);
}
