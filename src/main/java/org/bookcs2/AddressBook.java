package org.bookcs2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
public class AddressBook implements Serializable {
    private List<Contact> contacts;

    public AddressBook() {
        contacts = new ArrayList<>();
    }

    // 添加一个新的联系人，如果已存在同名的联系人，则返回false，否则返回true
    public boolean addContact(Contact contact) {
        for (Contact c : contacts) {
            if (c.getName().equals(contact.getName())) {
                return false;
            }
        }
        contacts.add(contact);
        return true;
    }

    // 删除一个指定姓名的联系人，如果存在则删除并返回true，否则返回false
    public boolean deleteContact(String name) {
        Iterator<Contact> it = contacts.iterator();
        while (it.hasNext()) {
            Contact c = it.next();
            if (c.getName().equals(name)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    // 修改一个指定姓名的联系人的信息，如果存在则修改并返回true，否则返回false
    public boolean updateContact(String name, Contact newContact) {
        for (Contact c : contacts) {
            if (c.getName().equals(name)) {
                c.setName(newContact.getName());
                c.setPhone(newContact.getPhone());
                c.setEmail(newContact.getEmail());
                return true;
            }
        }
        return false;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    // 查找一个指定姓名的联系人，如果存在则返回该联系人对象，否则返回null
    public Contact findContact(String name) {
        for (Contact c : contacts) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    // 返回所有联系人的列表
    public List<Contact> getAllContacts() {
        return contacts;
    }
}