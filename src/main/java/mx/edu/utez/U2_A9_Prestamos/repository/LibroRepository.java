package mx.edu.utez.U2_A9_Prestamos.repository;

import mx.edu.utez.U2_A9_Prestamos.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByEjemplaresDisponiblesGreaterThan(int cantidad);
}
