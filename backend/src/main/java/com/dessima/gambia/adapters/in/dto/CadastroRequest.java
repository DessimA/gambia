package com.dessima.gambia.adapters.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroRequest(
    @NotBlank String nome, @NotBlank @Email String email, @NotBlank @Size(min = 6) String senha) {}
