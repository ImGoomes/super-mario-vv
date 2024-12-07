package org.grupogjl.commands;

import org.grupogjl.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ExitToMenuCommandTests {

    private ExitToMenuCommand exitToMenuCommand;
    private Game mockGame;

    @BeforeEach
    void setUp() {
        exitToMenuCommand = new ExitToMenuCommand();
        mockGame = mock(Game.class);
    }

    @Test
    void testExecuteSetsGameStateToMenu() {
        exitToMenuCommand.execute(mockGame);
        verify(mockGame, times(1)).setStateMenu();
    }

    @Test
    void testExecuteSetsExitToMenuNullGame() {
        assertThrows(NullPointerException.class, () -> exitToMenuCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}