package com.dessima.gambia.adapters.in.web;

import com.dessima.gambia.adapters.in.dto.ErroResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> campos = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> campos.put(err.getField(), err.getDefaultMessage()));

    var body =
        new ErroResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Falha na validação dos campos de entrada",
            campos);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErroResponse> handleNotReadable(HttpMessageNotReadableException ex) {
    String mensagem = "Valor inválido enviado para um dos campos";
    Map<String, String> campos = new HashMap<>();

    var cause = ex.getCause();
    if (cause != null) {
      String causeMsg = cause.getMessage();
      if (causeMsg != null && causeMsg.contains("not one of the values accepted for Enum class")) {
        String field = causeMsg.contains("TipoImovel") ? "tipo_imovel" : "campo_desconhecido";
        campos.put(
            field,
            "Valor inválido. Os valores aceitos são: Casa, Apartamento, Comercio, Industria, Rural, Outro");
        mensagem = "Tipo de imóvel inválido";
      } else if (causeMsg != null && causeMsg.contains("Cannot deserialize value")) {
        String field = causeMsg.contains("consumo_kwh") ? "consumo_kwh" : "campo_desconhecido";
        campos.put(field, "Formato de número inválido");
        mensagem = "Formato de valor inválido";
      }
    }

    var body =
        new ErroResponse(
            Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", mensagem, campos);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErroResponse> handleIllegalArgument(IllegalArgumentException ex) {
    var body =
        new ErroResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            Map.of());

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
    var body =
        new ErroResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocorreu um erro interno no servidor",
            Map.of());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
