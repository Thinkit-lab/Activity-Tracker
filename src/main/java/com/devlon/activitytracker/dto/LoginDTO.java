package com.devlon.activitytracker.dto;

import com.devlon.activitytracker.entity.AuditEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO extends AuditEntity {
    private String email;
    @NotBlank(message = "Username cannot be blank")
    private String userName;
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8, max = 16, message = "Invalid Password")
    private String password;
}
