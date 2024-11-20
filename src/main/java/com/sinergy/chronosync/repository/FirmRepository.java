package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.model.firm.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmRepository extends JpaRepository<Firm, Long> {
}