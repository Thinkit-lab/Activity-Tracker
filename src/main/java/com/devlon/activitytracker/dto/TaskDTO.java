package com.devlon.activitytracker.dto;

import com.devlon.activitytracker.entity.AuditEntity;
import com.devlon.activitytracker.entity.User;
import com.devlon.activitytracker.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskDTO extends AuditEntity {
    private Long taskId;
    @NotBlank(message = "Enter a title")
    @Length(min = 5, max = 255, message = "Length cannot be less than 5")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    @Length(min = 5, max = 4000, message = "Length of description cannot be less than 5")
    private String description;
    private Status status;
    private LocalDateTime completedAt;
    private UserDTO userDTO;
}
