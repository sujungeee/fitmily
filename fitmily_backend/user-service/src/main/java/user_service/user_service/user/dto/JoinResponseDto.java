package user_service.user_service.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinResponseDto {

    private String accessToken;
    private String refreshToken;
}
