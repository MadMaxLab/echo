package io.github.madmaxlab.echocore.DTO;

import io.github.madmaxlab.echocore.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {
    private UUID id;

    private MessageType messageType;

    private String text;

    private String from;

    private String name;

    private Contact contact;

}
