package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameOverCommandTests {

    private GameOverCommand gameOverCommand;
    private Game mockGame;
    private StateGame mockStateGame;

    @BeforeEach
    void setUp() {
        gameOverCommand = new GameOverCommand();
        mockGame = mock(Game.class);
        mockStateGame = mock(StateGame.class);
        when(mockGame.getStateGame()).thenReturn(mockStateGame);
    }

    @Test
    void testSet_GameOver_ToGameState() {
        gameOverCommand.execute(mockGame);
        verify(mockStateGame, times(1)).setGameOver(true);
    }

    @Test
    void testSet_NullPointer_ToGameOverCommand() {
        assertThrows(NullPointerException.class, () -> gameOverCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}