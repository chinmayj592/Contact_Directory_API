package com.example.contact_management_api.service;

import com.example.contact_management_api.dto.ContactRequestDTO;
import com.example.contact_management_api.dto.ContactResponseDTO;
import com.example.contact_management_api.models.Contact;
import com.example.contact_management_api.repository.ContactRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    //  Add new contact using DTO
    public ContactResponseDTO addContact(ContactRequestDTO dto) {
        Contact contact = new Contact();
        contact.setName(dto.getName());
        contact.setPhone(dto.getPhone());
        contact.setEmail(dto.getEmail());
        contact.setAddress(dto.getAddress());
        contact.setFavorite(false);
        contact.setDeleted(false);

        Contact savedContact = contactRepository.save(contact);
        return mapToResponseDTO(savedContact);
    }

    //  Get all contacts with pagination & sorting (DTO version)
    public List<ContactResponseDTO> getAllContacts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return contactRepository.findByDeletedFalse()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //  Get contact by ID
    public ContactResponseDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        if (contact.isDeleted()) {
            throw new RuntimeException("Contact is deleted");
        }
        return mapToResponseDTO(contact);
    }

    //  Search by phone
    public List<ContactResponseDTO> getContactByPhone(String phone) {
        return contactRepository.findByPhoneContainingAndDeletedFalse(phone)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //  Search by name
    public List<ContactResponseDTO> getContactByName(String name) {
        return contactRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //  Search by multiple fields
    public List<ContactResponseDTO> searchContacts(String name, String phone, String email) {
        return contactRepository.findByNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCaseAndDeletedFalse(
                        name != null ? name : "",
                        phone != null ? phone : "",
                        email != null ? email : ""
                )
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    //  Update contact using DTO
    public ContactResponseDTO updateContact(Long id, ContactRequestDTO dto) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setName(dto.getName());
        contact.setPhone(dto.getPhone());
        contact.setEmail(dto.getEmail());
        contact.setAddress(dto.getAddress());

        Contact updatedContact = contactRepository.save(contact);
        return mapToResponseDTO(updatedContact);
    }

    //  Soft delete contact
    public void softDeleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setDeleted(true);
        contactRepository.save(contact);
    }

    //  Restore soft-deleted contact
    public void restoreContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setDeleted(false);
        contactRepository.save(contact);
    }

    // Mark as favorite
    public void markAsFavorite(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setFavorite(true);
        contactRepository.save(contact);
    }

    //  Unmark favorite
    public void unmarkFavorite(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setFavorite(false);
        contactRepository.save(contact);
    }

    //  Export contacts to CSV
    public String exportContactsToCSV() throws IOException {
        String filePath = "contacts_export.csv";
        List<Contact> contacts = contactRepository.findByDeletedFalse();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,Phone,Email,Address,Favorite\n");
            for (Contact contact : contacts) {
                writer.append(contact.getId().toString()).append(",")
                        .append(contact.getName()).append(",")
                        .append(contact.getPhone()).append(",")
                        .append(contact.getEmail()).append(",")
                        .append(contact.getAddress() != null ? contact.getAddress() : "").append(",")
                        .append(String.valueOf(contact.isFavorite())).append("\n");
            }
        }
        return filePath;
    }

    //  Mapper method for Contact -> ContactResponseDTO
    private ContactResponseDTO mapToResponseDTO(Contact contact) {
        return new ContactResponseDTO(
                contact.getId(),
                contact.getName(),
                contact.getPhone(),
                contact.getEmail(),
                contact.getAddress(),
                contact.isFavorite()
        );
    }
}
