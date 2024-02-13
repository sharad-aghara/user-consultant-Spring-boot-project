package com.sharad.oc.repositories;

import com.sharad.oc.entity.Apointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<Apointment, Integer> {
}
