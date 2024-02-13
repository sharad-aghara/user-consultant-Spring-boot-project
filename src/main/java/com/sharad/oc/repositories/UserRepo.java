package com.sharad.oc.repositories;

import com.sharad.oc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}
