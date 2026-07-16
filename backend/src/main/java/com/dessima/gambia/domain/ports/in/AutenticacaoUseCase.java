package com.dessima.gambia.domain.ports.in;

import com.dessima.gambia.domain.model.Usuario;

public interface AutenticacaoUseCase {
  Usuario cadastrar(String nome, String email, String senha);

  Usuario login(String email, String senha);
}
