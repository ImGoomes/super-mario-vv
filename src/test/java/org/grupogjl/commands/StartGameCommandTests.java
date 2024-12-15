package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StartGameCommandTests {

    private StartGameCommand startGameCommand;
    private Game mockGame;
    private StateGame mockStateGame;

    @BeforeEach
    void setup() {
        startGameCommand = new StartGameCommand();
        mockGame = mock(Game.class);
        mockStateGame = mock(StateGame.class);

        when(mockGame.getStateGame()).thenReturn(mockStateGame);
    }

    @Test
    void testSet_Start_ToGameState() throws IOException{
        startGameCommand.execute(mockGame);
        verify(mockGame, times(1)).setStateGame();
        verify(mockStateGame, times(1)).setGameOver(false);
    }

    @Test
    void test_ThrowsIOException__ToStartGameCommand() throws IOException {
        doThrow(new IOException("Mock IOException")).when(mockGame).setStateGame();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> startGameCommand.execute(mockGame),
                "The execute method should throw a RuntimeException when setStateGame throws an IOException.");
        assertEquals("java.io.IOException: Mock IOException", exception.getMessage());
    }

    @Test
    void testSet_NullPointer_ToStartGameCommand() {
        assertThrows(NullPointerException.class, () -> startGameCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}