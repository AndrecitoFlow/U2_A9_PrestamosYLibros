package mx.edu.utez.U2_A9_Prestamos.controller;

import mx.edu.utez.U2_A9_Prestamos.model.Lector;
import mx.edu.utez.U2_A9_Prestamos.repository.LectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectores")
public class LectorController {

    @Autowired
    private LectorRepository lectorRepository;

    @GetMapping
    public ResponseEntity<List<Lector>> obtenerTodos() {
        return ResponseEntity.ok(lectorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Lector> crearLector(@RequestBody Lector lector) {
        return ResponseEntity.ok(lectorRepository.save(lector));
    }
}