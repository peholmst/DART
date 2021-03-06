package net.pkhapps.dart.messaging.messages;

import java.time.Instant;
import java.util.Objects;

public abstract class Response extends Message {

    public Response(Instant timestamp, String conversationId) {
        super(Objects.requireNonNull(timestamp), conversationId);
    }
}
