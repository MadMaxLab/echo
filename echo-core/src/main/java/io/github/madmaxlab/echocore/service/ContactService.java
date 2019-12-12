package io.github.madmaxlab.echocore.service;

import io.github.madmaxlab.echocore.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getContactList(String userLogin);
}
