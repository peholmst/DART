package net.pkhapps.dart.common.location;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A location feature is used to enhance a {@link Location} by composition. Depending on the type of location, additional
 * information can be provided in a location feature. Conceptually, a location feature cannot exist without an
 * enclosing {@link Location} (even though it is technically possible).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Intersection.class, name = "intersection"),
        @JsonSubTypes.Type(value = StreetAddress.class, name = "streetAddress")})
public abstract class LocationFeature {
    // Marker class
}
