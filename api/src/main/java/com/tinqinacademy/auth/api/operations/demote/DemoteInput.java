package com.tinqinacademy.auth.api.operations.demote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.auth.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemoteInput implements OperationInput {
    @NotNull(message = "userId is required")
    private String userId;
    @JsonIgnore
    private String jwt;
}
