package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// dto pros users dentro de user-profile
@Getter
@Setter
@Builder
public class UserProfileUserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
