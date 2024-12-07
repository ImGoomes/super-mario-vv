package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResumeCommandTests {

    private ResumeCommand resumeCommand;
    private Game mockGame;
    private StatePause mockStatePause;

    @BeforeEach
    void setup() {
        resumeCommand = new ResumeCommand();
        mockGame = mock(Game.class);
        mockStatePause = mock(StatePause.class);

        when(mockGame.getStatePause()).thenReturn(mockStatePause);
    }

    @Test
    void testExecuteSetsGameStateToResume() {
        resumeCommand.execute(mockGame);
        verify(mockGame, times(1)).setStateGame(mockStatePause.getParent());
    }

    @Test
    void testExecuteSetsReus() {
        assertThrows(NullPointerException.class, () -> resumeCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}