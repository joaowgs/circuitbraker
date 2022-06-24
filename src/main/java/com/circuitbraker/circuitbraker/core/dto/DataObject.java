package com.circuitbraker.circuitbraker.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataObject implements Serializable {

    BigDecimal valor;
    String cedente;
    String documento_cedente;
    String sacado;
    String sacado_documento;
    String agencia;
    String conta_corrente;
    Integer convenio;
    String carteira;
}
