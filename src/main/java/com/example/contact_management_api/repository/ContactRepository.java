package com.example.contact_management_api.repository;

import com.example.contact_management_api.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact> findByNameContainingIgnoreCase(String name);
    List<Contact> findByPhoneContaining(String phone);

}
