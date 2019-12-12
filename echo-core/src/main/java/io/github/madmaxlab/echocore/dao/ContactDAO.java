package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.Contact;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ContactDAO  extends Repository<Contact, UUID> {
    @Query("FROM Contact WHERE initiator = :id or accepter = :id")
    List<Contact> getContacts (@Param("id") User user);
}
