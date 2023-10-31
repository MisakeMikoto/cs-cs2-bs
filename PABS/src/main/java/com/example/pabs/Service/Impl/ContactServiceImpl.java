package com.example.pabs.Service.Impl;

import com.example.pabs.Dao.ContactDAO;
import com.example.pabs.Service.ContactService;
import com.example.pabs.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactDAO contactDAO;

    public List<Contact> getAllContacts() {
        return contactDAO.selectList(null);
    }

    public Contact getContactById(Long id) {
        return contactDAO.selectById(id);
    }

    public void addContact(Contact contact) {
        contactDAO.insert(contact);
    }

    public void updateContact(Contact contact) {
        contactDAO.updateById(contact);
    }

    public void deleteContact(Long id) {
        contactDAO.deleteById(id);
    }
}
