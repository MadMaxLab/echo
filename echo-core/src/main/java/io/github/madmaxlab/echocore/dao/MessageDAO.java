package io.github.madmaxlab.echocore.dao;

import io.github.madmaxlab.echocore.entity.Message;
import io.github.madmaxlab.echocore.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MessageDAO extends CrudRepository<Message, UUID> {
    @Query("from Message  where sender = :user or receiver = :user")
    List<Message> getUserMessages(User user);
}
