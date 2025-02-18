package com.example.graduados.repository;

import com.example.graduados.models.Graduado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraduadoRepository extends JpaRepository<Graduado, Integer> {
    Graduado findByCurp(String curp); // Buscar graduado por CURP

}