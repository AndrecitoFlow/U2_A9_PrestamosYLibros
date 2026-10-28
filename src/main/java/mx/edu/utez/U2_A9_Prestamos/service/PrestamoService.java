package mx.edu.utez.U2_A9_Prestamos.service;

import mx.edu.utez.U2_A9_Prestamos.dto.PrestamoResponseDTO;
import mx.edu.utez.U2_A9_Prestamos.model.Lector;
import mx.edu.utez.U2_A9_Prestamos.model.Libro;
import mx.edu.utez.U2_A9_Prestamos.model.Prestamo;
import mx.edu.utez.U2_A9_Prestamos.enums.EstadoPrestamo;
import mx.edu.utez.U2_A9_Prestamos.repository.LectorRepository;
import mx.edu.utez.U2_A9_Prestamos.repository.LibroRepository;
import mx.edu.utez.U2_A9_Prestamos.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LectorRepository lectorRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Transactional
    public Prestamo solicitarPrestamo(Long lectorId, Long libroId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new RuntimeException("El lector no existe"));
        if (!lector.getActivo()) {
            throw new RuntimeException("El lector no está activo");
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("El libro no existe"));
        if (libro.getEjemplaresDisponibles() < 1) {
            throw new RuntimeException("No hay ejemplares disponibles para este libro");
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        List<Prestamo> prestamosActivos = prestamoRepository.findByLectorIdAndEstadoIn(lectorId, estadosActivos);

        int limitePrestamos = lector.getPremium() ? 5 : 3;
        if (prestamosActivos.size() >= limitePrestamos) {
            throw new RuntimeException("El lector ha alcanzado el límite máximo de préstamos activos (" + limitePrestamos + ")");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new RuntimeException("El préstamo ya ha sido devuelto anteriormente");
        }

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO || prestamo.getEstado() == EstadoPrestamo.VENCIDO) {
            throw new RuntimeException("No se puede renovar un préstamo en estado DEVUELTO o VENCIDO");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new RuntimeException("El lector no existe"));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new RuntimeException("El lector no existe");
        }

        List<EstadoPrestamo> estadosActivos = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        return prestamoRepository.findByLectorIdAndEstadoIn(lectorId, estadosActivos);
    }

    public PrestamoResponseDTO mapToDTO(Prestamo prestamo) {
        return PrestamoResponseDTO.builder()
                .id(prestamo.getId())
                .lectorId(prestamo.getLector().getId())
                .nombreLector(prestamo.getLector().getNombre())
                .libroId(prestamo.getLibro().getId())
                .tituloLibro(prestamo.getLibro().getTitulo())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima())
                .estado(prestamo.getEstado())
                .build();
    }
}