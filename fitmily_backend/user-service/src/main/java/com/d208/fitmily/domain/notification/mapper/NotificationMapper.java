package com.d208.fitmily.domain.notification.mapper;

import com.d208.fitmily.domain.notification.dto.NotificationResponseDTO;
import com.d208.fitmily.domain.notification.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface NotificationMapper {

    @Insert("""
        INSERT INTO notification (
            user_id,
            notification_type, 
            notification_sender_id, 
            notification_receiver_id, 
            notification_resource_id,
            notification_content, 
            notification_is_read,
            notification_created_at, 
            notification_updated_at
        ) VALUES (
            #{userId},
            #{notificationType}, 
            #{notificationSenderId}, 
            #{notificationReceiverId}, 
            #{notificationResourceId},
            #{notificationContent}, 
            #{notificationIsRead},
            #{notificationCreatedAt}, 
            #{notificationUpdatedAt}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "notificationId")
    int insertNotification(Notification notification);

    @Select("""
    SELECT 
        n.notification_id as notificationId,
        n.notification_type as type,
        n.notification_created_at as receivedAt,
        n.notification_is_read as isRead,
        n.notification_sender_id as senderId,
        CASE WHEN n.notification_sender_id IS NOT NULL 
             THEN u.user_nickname 
             ELSE NULL 
        END as senderNickname,
        n.notification_resource_id as resourceId,
        n.notification_content as content
    FROM notification n
    LEFT JOIN user u ON n.notification_sender_id = u.user_id
    WHERE n.notification_receiver_id = #{userId}
    ORDER BY n.notification_created_at DESC
    LIMIT #{limit} OFFSET #{offset}
""")
    @Results({
            @Result(property = "notificationId", column = "notificationId"),
            @Result(property = "type", column = "type"),
            @Result(property = "receivedAt", column = "receivedAt"),
            @Result(property = "isRead", column = "isRead", javaType = Boolean.class),
            @Result(property = "senderId", column = "senderId"),
            @Result(property = "senderNickname", column = "senderNickname"),
            @Result(property = "resourceId", column = "resourceId"),
    })
    List<NotificationResponseDTO> findNotificationsByUserId(@Param("userId") int userId,
                                                            @Param("limit") int limit,
                                                            @Param("offset") int offset);


    @Update("""
        UPDATE notification
        SET notification_is_read = 1,
            notification_updated_at = #{updatedAt}
        WHERE notification_id = #{notificationId}
        """)
    int markAsRead(@Param("notificationId") int notificationId, @Param("updatedAt") LocalDateTime updatedAt);

    @Select("""
        SELECT COUNT(*) 
        FROM notification 
        WHERE notification_receiver_id = #{userId} 
        AND notification_is_read = 0
        """)
    int countUnreadNotifications(@Param("userId") int userId);
}