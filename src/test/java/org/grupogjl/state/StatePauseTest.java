package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.controller.ControllerPause;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.viewer.ViewerPause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatePauseTest {

    private StatePause statePause;
    private StateGame mockParent;
    private Game mockGame;
    private LanternaGui mockGui;
    private ControllerPause mockController;
    private ViewerPause mockViewer;

    @BeforeEach
    void setUp() {
        mockParent = mock(StateGame.class);
        mockGame = mock(Game.class);
        mockGui = mock(LanternaGui.class);
        mockController = mock(ControllerPause.class);
        mockViewer = mock(ViewerPause.class);

        statePause = new StatePause(mockParent);
        statePause.controller = mockController;
        statePause.viewer = mockViewer;
    }

    @Test
    void testGetModel() {
        PauseModel model = statePause.getModel();
        assertEquals(statePause.getModel(), model, "StatePause should return its pause model");
    }

    @Test
    void testGetState() {
        assertEquals(3, statePause.getState(), "StatePause should return 3 as its state ID");
    }

    @Test
    void testGetParent() {
        assertEquals(mockParent, statePause.getParent(), "StatePause should return its parent StateGame");
    }

    @Test
    void testStep_Pause() throws IOException {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.DOWN;
        when(mockGui.getNextAction()).thenReturn(mockAction);

        long startTime = System.currentTimeMillis();

        statePause.step(mockGame, mockGui, startTime);

        verify(mockGui, times(1)).getNextAction();
        verify(mockController, times(1)).step(mockGame, mockAction, startTime);
        verify(mockParent, times(1)).draw(mockGui);
        verify(mockViewer, times(1)).draw(statePause, mockGui);
    }
}
