package com.tinqinacademy.auth.api.operations.promote;

import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoteInput implements OperationInput {
    @NotNull(message = "userId is required")
    private String userId;
}
