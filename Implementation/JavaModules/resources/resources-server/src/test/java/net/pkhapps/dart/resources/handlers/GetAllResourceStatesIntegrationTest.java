package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.database.enums.ResourceState;
import net.pkhapps.dart.resources.messages.AllResourceStates;
import net.pkhapps.dart.resources.messages.GetAllResourceStates;
import net.pkhapps.dart.resources.queries.ResourceStateDescriptorQuery;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Integration test for {@link GetAllResourceStatesHandler}.
 */
public class GetAllResourceStatesIntegrationTest {

    private GetAllResourceStatesHandler handler;

    @Before
    public void setUp() {
        handler = new GetAllResourceStatesHandler(DSLContextFactory.getInstance(),
                new ResourceStateDescriptorQuery(DataSourceProperties.getInstance()), Clock.systemDefaultZone());
    }

    @Test
    public void handleRequest_getAllResourceStates_allStatesReturned() {
        AllResourceStates response = handler.handleRequest(new GetAllResourceStates());
        assertTrue(response.getTimestamp().isPresent());
        assertFalse(response.getConversationId().isPresent());
        assertEquals(ResourceState.values().length, response.getStates().size());
        response.getStates().forEach(state -> {
            assertNotNull(state.getDescription());
            assertTrue(state.getColor().isPresent());
            ResourceState.valueOf(state.getState());
        });
    }
}
