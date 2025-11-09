package com.julygt.ProyectoFinalUTEC.pagos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodo;                          // Validación automática de Enum

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0") // valida el monto minimo
    private BigDecimal monto;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9]{9}$",                     // Valida cadena de Texto
            message = "El número de teléfono debe tener exactamente 9 dígitos numéricos"
    )
    private String telefonoContacto;
}

