package com.example.JavaCodingExam.Repository;

import com.example.JavaCodingExam.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
