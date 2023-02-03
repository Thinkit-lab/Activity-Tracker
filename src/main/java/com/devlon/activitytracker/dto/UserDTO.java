package com.devlon.activitytracker.dto;

import com.devlon.activitytracker.entity.AuditEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO extends AuditEntity {
    private Long userId;
    @NotBlank(message = "Username cannot be blank")
    private String userName;
    @NotBlank
    @Length(max = 60, message = "Invalid Email")
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 16, message = "Invalid Password")
    private String password;
}
