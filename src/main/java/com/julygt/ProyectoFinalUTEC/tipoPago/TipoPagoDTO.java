package com.julygt.ProyectoFinalUTEC.tipoPago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPagoDTO {
    private Long id_tipo_pago;
    private String nombre;
}

