package io.github.madmaxlab.echocore.entity;

public enum MessageType {
    // WARNING!!! This enum is used to persist messages as JPA ordered enum.
    // Changing an order of this enum values may occur an error in db.
    // If you need a new values add them to the end of enum.
    TEXT,
    FILE
    // <--- Add new values here!
}
