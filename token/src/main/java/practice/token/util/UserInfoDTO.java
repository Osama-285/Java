package practice.token.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    private String name;
    private String username;
    private String email;
    private String password;
    private String authorities;
}
