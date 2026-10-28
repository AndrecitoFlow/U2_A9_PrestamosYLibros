package mx.edu.utez.U2_A9_Prestamos.dto;

import lombok.Builder;
import lombok.Data;
import mx.edu.utez.U2_A9_Prestamos.enums.EstadoPrestamo;

import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponseDTO {
    private Long id;
    private Long lectorId;
    private String nombreLector;
    private Long libroId;
    private String tituloLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionMaxima;
    private EstadoPrestamo estado;
}