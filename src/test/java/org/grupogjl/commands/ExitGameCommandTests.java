package org.grupogjl.commands;

import org.grupogjl.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ExitGameCommandTests {

    private ExitGameCommand exitGameCommand;
    private Game mockGame;

    @BeforeEach
    void setUp() {
        exitGameCommand = new ExitGameCommand();
        mockGame = mock(Game.class);
    }

    @Test
    void testSet_ExitGame_ToGameState() {
        exitGameCommand.execute(mockGame);
        verify(mockGame, times(1)).setStateNull();
    }

    @Test
    void testSet_NullPointer_ToExitGameCommand() {
        assertThrows(NullPointerException.class, () -> exitGameCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}