package com.softserve.teachua.dto.version;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@With
public class VersionDto {
    @NotNull
    @NotEmpty
    @NotBlank(message = "commitNumber cannot be empty")
    private String backendCommitNumber;

    @NotNull
    @NotEmpty
    @NotBlank(message = "commitDate cannot be empty")
    private String backendCommitDate;

    @NotNull
    @NotEmpty
    @NotBlank(message = "buildDate cannot be empty")
    private String buildDate;
}
