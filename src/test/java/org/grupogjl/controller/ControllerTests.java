package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class ControllerTests {

    private Controller controller;
    private Game mockGame;
    private GeneralGui mockGui;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        mockGame = mock(Game.class);
        mockGui = mock(GeneralGui.class);
    }

    @Test
    void testStep() throws IOException {
        GeneralGui.ACTION action = GeneralGui.ACTION.NONE;
        long time = System.currentTimeMillis();

        controller.step(mockGame, action, time);

        verify(controller, times(1)).step(mockGame, action, time);
    }

    @Test
    void testStepThrowsIOException() throws IOException {
        GeneralGui.ACTION action = GeneralGui.ACTION.NONE;
        long time = System.currentTimeMillis();

        doThrow(new IOException("Mock IOException")).when(controller).step(mockGame, action, time);

        assertThrows(IOException.class, () -> controller.step(mockGame, action, time));
    }
}