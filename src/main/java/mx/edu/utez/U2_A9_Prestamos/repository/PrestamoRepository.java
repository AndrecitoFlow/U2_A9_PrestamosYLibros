package mx.edu.utez.U2_A9_Prestamos.repository;

import mx.edu.utez.U2_A9_Prestamos.enums.EstadoPrestamo;
import mx.edu.utez.U2_A9_Prestamos.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByLectorId(Long lectorId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, Collection<EstadoPrestamo> estados);
}
