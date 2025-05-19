package com.d208.fitmily.domain.poke.mapper;

import com.d208.fitmily.domain.poke.entity.Poke;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PokeMapper {

    // 콕 찌르기 저장
    @Insert("""
        INSERT INTO poke (
            sender_id, target_id, sent_at, created_at
        ) VALUES (
            #{senderId}, #{targetId}, #{sentAt}, #{createdAt}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "pokeId")
    int insertPoke(Poke poke);

    // 사용자가 받은 콕 찌르기 목록 조회
    @Select("""
        SELECT 
            poke_id as pokeId,
            sender_id as senderId,
            target_id as targetId,
            sent_at as sentAt,
            created_at as createdAt
        FROM poke
        WHERE target_id = #{userId}
        ORDER BY sent_at DESC
        """)
    List<Poke> findPokesByTargetId(@Param("userId") int userId);

    // 콕 찌르기 ID로 조회
    @Select("""
        SELECT 
            poke_id as pokeId,
            sender_id as senderId,
            target_id as targetId,
            sent_at as sentAt,
            created_at as createdAt
        FROM poke
        WHERE poke_id = #{pokeId}
        """)
    Poke findPokeById(@Param("pokeId") int pokeId);
}