package com.example.contact_management_api.controller;

import com.example.contact_management_api.dto.ContactRequestDTO;
import com.example.contact_management_api.dto.ContactResponseDTO;
import com.example.contact_management_api.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ✅ Add new contact
    @PostMapping
    public ResponseEntity<ContactResponseDTO> addContact(@RequestBody ContactRequestDTO contactRequest) {
        return ResponseEntity.ok(contactService.addContact(contactRequest));
    }

    // ✅ Get all contacts with pagination & sorting
    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(contactService.getAllContacts(page, size, sortBy));
    }

    // ✅ Get contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    // ✅ Search by phone
    @GetMapping("/search/phone/{phone}")
    public ResponseEntity<List<ContactResponseDTO>> getContactByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(contactService.getContactByPhone(phone));
    }

    // ✅ Search by name
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<ContactResponseDTO>> getContactByName(@PathVariable String name) {
        return ResponseEntity.ok(contactService.getContactByName(name));
    }

    // ✅ Search by multiple fields
    @GetMapping("/search")
    public ResponseEntity<List<ContactResponseDTO>> searchContacts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        return ResponseEntity.ok(contactService.searchContacts(name, phone, email));
    }

    // ✅ Update contact
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable Long id, @RequestBody ContactRequestDTO contactRequest) {
        return ResponseEntity.ok(contactService.updateContact(id, contactRequest));
    }

    // ✅ Soft delete contact
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteContact(@PathVariable Long id) {
        contactService.softDeleteContact(id);
        return ResponseEntity.ok("Contact soft deleted successfully");
    }

    // ✅ Restore contact
    @PutMapping("/{id}/restore")
    public ResponseEntity<String> restoreContact(@PathVariable Long id) {
        contactService.restoreContact(id);
        return ResponseEntity.ok("Contact restored successfully");
    }

    // ✅ Mark as favorite
    @PutMapping("/{id}/favorite")
    public ResponseEntity<String> markAsFavorite(@PathVariable Long id) {
        contactService.markAsFavorite(id);
        return ResponseEntity.ok("Contact marked as favorite");
    }

    // ✅ Unmark favorite
    @PutMapping("/{id}/unfavorite")
    public ResponseEntity<String> unmarkFavorite(@PathVariable Long id) {
        contactService.unmarkFavorite(id);
        return ResponseEntity.ok("Contact removed from favorites");
    }

    // ✅ Export contacts to CSV
    @GetMapping("/export")
    public ResponseEntity<String> exportContactsToCSV() throws IOException {
        String filePath = contactService.exportContactsToCSV();
        return ResponseEntity.ok("Contacts exported successfully to: " + filePath);
    }
}
