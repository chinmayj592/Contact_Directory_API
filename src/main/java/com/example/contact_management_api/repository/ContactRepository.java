package com.example.contact_management_api.repository;

import com.example.contact_management_api.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // 🔍 Search by name (case-insensitive)
    List<Contact> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    // 🔍 Search by phone
    List<Contact> findByPhoneContainingAndDeletedFalse(String phone);

    // 🔍 Search by email
    List<Contact> findByEmailContainingIgnoreCaseAndDeletedFalse(String email);

    // 🔍 Find all favorite contacts
    List<Contact> findByFavoriteTrueAndDeletedFalse();

    // 🔍 Search by multiple fields (name, phone, email)
    List<Contact> findByNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCaseAndDeletedFalse(
            String name, String phone, String email
    );

    // ✅ Find all non-deleted contacts
    List<Contact> findByDeletedFalse();
}
