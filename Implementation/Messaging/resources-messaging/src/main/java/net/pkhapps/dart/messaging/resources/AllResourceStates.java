package net.pkhapps.dart.messaging.resources;

import net.pkhapps.dart.messaging.common.messages.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface AllResourceStates extends Response {

    @NotNull
    Collection<ResourceState> getStates();

    interface ResourceState {
        @NotNull
        String getState();

        @NotNull
        Map<Locale, String> getDescription();

        boolean isLocationTrackingEnabled();

        @NotNull
        Optional<String> getColor();
    }
}
