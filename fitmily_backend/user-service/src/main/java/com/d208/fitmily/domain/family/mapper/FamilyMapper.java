package com.d208.fitmily.domain.family.mapper;

import com.d208.fitmily.domain.family.entity.Family;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FamilyMapper {
    void createFamily(Family family);
}