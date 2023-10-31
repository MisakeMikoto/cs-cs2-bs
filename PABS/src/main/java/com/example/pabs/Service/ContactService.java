package com.example.pabs.Service;

import com.example.pabs.entity.Contact;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
public interface ContactService {
    List<Contact> getAllContacts();
    Contact getContactById(Long id);
    void addContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(Long id);
}
