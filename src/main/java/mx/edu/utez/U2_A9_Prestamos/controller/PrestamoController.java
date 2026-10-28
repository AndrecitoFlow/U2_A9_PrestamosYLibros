package mx.edu.utez.U2_A9_Prestamos.controller;

import mx.edu.utez.U2_A9_Prestamos.dto.PrestamoResponseDTO;
import mx.edu.utez.U2_A9_Prestamos.dto.SolicitudPrestamoDTO;
import mx.edu.utez.U2_A9_Prestamos.model.Lector;
import mx.edu.utez.U2_A9_Prestamos.model.Prestamo;
import mx.edu.utez.U2_A9_Prestamos.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping("/solicitar")
    public ResponseEntity<PrestamoResponseDTO> solicitar(@RequestBody SolicitudPrestamoDTO dto) {
        Prestamo prestamo = prestamoService.solicitarPrestamo(dto.getLectorId(), dto.getLibroId());
        return ResponseEntity.ok(prestamoService.mapToDTO(prestamo));
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<PrestamoResponseDTO> devolver(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.devolverLibro(id);
        return ResponseEntity.ok(prestamoService.mapToDTO(prestamo));
    }

    @PutMapping("/renovar/{id}")
    public ResponseEntity<PrestamoResponseDTO> renovar(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.renovarPrestamo(id);
        return ResponseEntity.ok(prestamoService.mapToDTO(prestamo));
    }

    @PutMapping("/rehabilitar-lector/{lectorId}")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.rehabilitarLector(lectorId));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> listarActivos(@PathVariable Long lectorId) {
        List<PrestamoResponseDTO> response = prestamoService.listarPrestamosActivosLector(lectorId)
                .stream()
                .map(prestamoService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}