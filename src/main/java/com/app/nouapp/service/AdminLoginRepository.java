package com.app.nouapp.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.nouapp.model.AdminLogin;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, String> {

}
