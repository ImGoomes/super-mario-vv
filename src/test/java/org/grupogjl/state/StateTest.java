package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.controller.Controller;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.viewer.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    // Test class that extends State. Idk if is the best appproach to test an abstract class
    private static class TestState extends State<String> {
        @Override
        public String getModel() {
            return model;
        }

        @Override
        public int getState() { return 1; }

        @Override
        public void step(Game game, LanternaGui gui, long startTime) throws IOException {}
    }

    private TestState state;
    private Controller mockController;
    private Viewer mockViewer;

    @BeforeEach
    void setUp() {
        state = new TestState();
        mockController = Mockito.mock(Controller.class);
        mockViewer = Mockito.mock(Viewer.class);
    }

    @Test
    void testGetAndSetController() {
        state.setController(mockController);
        assertEquals(mockController, state.getController());
    }

    @Test
    void testGetAndSetViewer() {
        state.setViewer(mockViewer);
        assertEquals(mockViewer, state.getViewer());
    }

    @Test
    void testGetModel() {
        state.model = "TestModel";
        assertEquals("TestModel", state.getModel());
    }

    @Test
    void testGetState() {
        assertEquals(1, state.getState());
    }

    @Test
    void testStep() throws IOException {
        Game mockGame = Mockito.mock(Game.class);
        LanternaGui mockGui = Mockito.mock(LanternaGui.class);

        state.step(mockGame, mockGui, System.currentTimeMillis());
    }
}
