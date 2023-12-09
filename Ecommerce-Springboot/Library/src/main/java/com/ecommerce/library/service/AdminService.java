package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;

import java.util.List;

public interface AdminService {
    Admin save(AdminDto adminDto);

    Admin findByUsername(String username);

    List<Admin> findAll();
}
