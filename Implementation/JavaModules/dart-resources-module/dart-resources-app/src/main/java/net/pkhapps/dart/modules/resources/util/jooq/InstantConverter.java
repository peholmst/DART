package net.pkhapps.dart.modules.resources.util.jooq;

import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * A converter that converts between {@link Instant}s and {@link Timestamp}s.
 */
public class InstantConverter implements Converter<Timestamp, Instant> {

    @Override
    public Instant from(Timestamp databaseObject) {
        return databaseObject == null ? null : databaseObject.toInstant();
    }

    @Override
    public Timestamp to(Instant userObject) {
        return userObject == null ? null : Timestamp.from(userObject);
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<Instant> toType() {
        return Instant.class;
    }
}
