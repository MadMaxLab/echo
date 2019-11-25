package io.github.madmaxlab.echocore.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDTO {

    private UUID id;

    String messageType;

    String text;



}
