package org.grupogjl.model.game.elements.pause;

import org.grupogjl.Game;
import org.grupogjl.commands.ExitToMenuCommand;
import org.grupogjl.commands.ResumeCommand;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PauseModelTest {

    private PauseModel pauseModel;
    private Game mockGame;
    @BeforeEach
    void setUp() {
        pauseModel = new PauseModel();
        mockGame = mock(Game.class);
    }

    @Test
    void testPauseModel_Initialization() {
        Vector<Button> buttons = pauseModel.getButtons();

        assertEquals(2, buttons.size(), "PauseModel should initialize with 2 buttons.");

        assertEquals("resume", buttons.get(0).getText(), "First button should be 'resume'.");
        assertEquals("exit to menu", buttons.get(1).getText(), "Second button should be 'exit to menu'.");
    }

    @Test
    void testPauseModel_Navigation() {
        assertEquals(Byte.valueOf((byte) 0), pauseModel.getSelectedButton(), "Initial selectedButton should be 0.");

        pauseModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 1), pauseModel.getSelectedButton(), "selectedButton should move to the next position.");

        pauseModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 1), pauseModel.getSelectedButton(), "selectedButton should not exceed the bounds of the button list.");

        pauseModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 0), pauseModel.getSelectedButton(), "selectedButton should move to the previous position.");

        pauseModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 0), pauseModel.getSelectedButton(), "selectedButton should not go below 0.");
    }

    @Test
    void testPauseModel_ExecuteResumeCommand() {
        StatePause mockStatePause = mock(StatePause.class);
        StateGame mockParentState = mock(StateGame.class);

        when(mockGame.getStatePause()).thenReturn(mockStatePause);
        when(mockStatePause.getParent()).thenReturn(mockParentState);

        pauseModel.execute(mockGame);

        verify(mockGame, times(1)).setStateGame(mockParentState);
    }

    @Test
    void testPauseModel_ExecuteExitToMenuCommand() {
        pauseModel.nextPosition();

        pauseModel.execute(mockGame);

        verify(mockGame, times(1)).setStateMenu();
    }
}
