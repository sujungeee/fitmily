package com.d208.fitmily.domain.health.mapper;

import com.d208.fitmily.domain.health.dto.HealthInsertDto;
import com.d208.fitmily.domain.health.dto.HealthResponseDto;
import com.d208.fitmily.domain.health.dto.UpdateHealthResponseDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HealthMapper {

    // 1. 건강상태 추가
    @Insert("""
        INSERT INTO health (
            user_id,
            health_bmi,
            health_height,
            health_weight,
            health_other_diseases,
            health_five_major_diseases,
            health_created_at,
            health_updated_at
        )
        VALUES (
            #{userId},
            #{bmi},
            #{height},
            #{weight},
            #{healthOtherDiseasesJson},
            #{healthFiveMajorDiseasesJson},
            NOW(),
            NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "healthId")
    void insertHealth(HealthInsertDto dto);

    // 2. 최신 건강상태 조회
    @Select("""
        SELECT
            health_id AS healthId,
            health_bmi AS bmi,
            health_height AS height,
            health_weight AS weight,
            health_other_diseases AS otherDiseases,
            health_five_major_diseases AS fiveMajorDiseases,
            health_created_at AS createdAt,
            health_updated_at AS updatedAt
        FROM health
        WHERE user_id = #{userId}
        ORDER BY health_created_at DESC
        LIMIT 1
        """)
    HealthResponseDto selectLatestByUserId(@Param("userId") Integer userId);

    // 3. 건강상태 수정
    @Update("""
        UPDATE health
        SET
            health_bmi = #{bmi},
            health_height = #{height},
            health_weight = #{weight},
            health_other_diseases = #{otherDiseases},
            health_five_major_diseases = #{fiveMajorDiseases},
            health_updated_at = NOW()
        WHERE user_id = #{userId}
        """)
    int updateHealth(UpdateHealthResponseDto dto);
}
