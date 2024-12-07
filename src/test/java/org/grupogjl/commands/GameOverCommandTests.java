package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testExecuteSetsGameOver() {
        gameOverCommand.execute(mockGame);
        verify(mockStateGame, times(1)).setGameOver(true);
    }
}