package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ControllerPauseTests {

    private ControllerPause controllerPause;
    private Game mockGame;
    private StatePause mockStatePause;
    private PauseModel mockModel;

    @BeforeEach
    void setup() {
        controllerPause = new ControllerPause();

        mockGame = mock(Game.class);
        mockStatePause = mock(StatePause.class);
        mockModel = mock(PauseModel.class);

        when(mockGame.getStatePause()).thenReturn(mockStatePause);
        when(mockStatePause.getModel()).thenReturn(mockModel);
    }

    @Test
    void testSet_ActionDown() {
        controllerPause.step(mockGame, GeneralGui.ACTION.DOWN, 100L);

        verify(mockModel, times(1)).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testSet_ActionUp() {
        controllerPause.step(mockGame, GeneralGui.ACTION.UP, 100L);

        verify(mockModel, times(1)).lastPosition();
        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testSet_ActionSelect() {
        controllerPause.step(mockGame, GeneralGui.ACTION.SELECT, 100L);

        verify(mockModel, times(1)).execute(mockGame);
        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
    }

    @Test
    void testSet_IrrelevantAction() {
        controllerPause.step(mockGame, GeneralGui.ACTION.NONE, 100L);

        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testSet_NullAction() {
        controllerPause.step(mockGame, null, 100L);

        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).execute(mockGame);
    }
}
