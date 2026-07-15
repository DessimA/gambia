package com.dessima.gambia.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
public class UsuarioEntity {

  @Id private UUID id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "senha_hash", nullable = false, length = 255)
  private String senhaHash;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Deprecated
  protected UsuarioEntity() {}

  public UsuarioEntity(UUID id, String nome, String email, String senhaHash) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senhaHash = senhaHash;
    this.createdAt = Instant.now();
  }

  public UUID getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getSenhaHash() {
    return senhaHash;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
