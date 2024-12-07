package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PauseCommandTests {

    private PauseCommand pauseCommand;
    private Game mockGame;
    private StateMenu mockStateMenu;

    @BeforeEach
    void setup() {
        pauseCommand = new PauseCommand();
        mockGame = mock(Game.class);
    }

    @Test
    void testExecuteSetsGameStateToPause() {
        pauseCommand.execute(mockGame);
        verify(mockGame, times(1)).setStatePause();
    }

    @Test
    void testExecuteSetsPauseNullGame() {
        assertThrows(NullPointerException.class, () -> pauseCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}