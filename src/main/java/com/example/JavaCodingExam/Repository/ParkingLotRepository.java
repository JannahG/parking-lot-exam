package com.example.JavaCodingExam.Repository;

import com.example.JavaCodingExam.Entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {
}
