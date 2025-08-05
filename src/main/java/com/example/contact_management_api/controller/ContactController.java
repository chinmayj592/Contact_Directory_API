package com.example.contact_management_api.controller;


import com.example.contact_management_api.models.Contact;
import com.example.contact_management_api.service.ContactService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }



    @PostMapping
    public Contact addContact(@RequestBody Contact contact){
        return  contactService.addContact(contact);


    }
    @GetMapping
    public List<Contact> getAllContacts(){
        return contactService.getAllContacts();

    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable("id") Long id){
        return contactService.getContactById(id);
    }

    @GetMapping("/search/phone/{phone}")
    public List<Contact> getContactByPhone(@PathVariable("phone") String phone){
        return contactService.getContactByPhone(phone);
    }

    @GetMapping("/search/name/{name}")
    public List<Contact> getContactByName(@PathVariable("name") String name){
        return contactService.getContactByName(name);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable("id") Long id,@RequestBody Contact contact) {
        return contactService.updateContact(id, contact);
    }
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable("id")Long id){
        contactService.deleteContact(id);

    }

}
