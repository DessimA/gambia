package com.dessima.gambia.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Usuario(UUID id, String nome, String email, String senhaHash, Instant createdAt) {}
