package io.github.madmaxlab.echocore.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {

    private UUID id;

    MessageType messageType;

    String text;

}
