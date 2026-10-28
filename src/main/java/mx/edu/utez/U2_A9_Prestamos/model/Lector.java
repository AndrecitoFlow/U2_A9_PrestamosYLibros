package mx.edu.utez.U2_A9_Prestamos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lector")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Boolean Premium = false;
}
