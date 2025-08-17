package org.sopt.controller.test.dto.jwt;

import jakarta.validation.constraints.NotBlank;

public record TestSecurity(
        @NotBlank String name
) { }