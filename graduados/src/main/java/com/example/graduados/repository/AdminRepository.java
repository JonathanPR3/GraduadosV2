package com.example.graduados.repository;

import com.example.graduados.models.admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<admin, Integer> {
    admin findByCurp(String curp); // Buscar admin por CURP

}