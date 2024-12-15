package org.grupogjl.commands;

import org.grupogjl.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class GameCommandTests {

    // Create a concrete subclass for testing
    static class TestGameCommand extends GameCommand {
        @Override
        public void execute(Game game) {
            if (game != null) {
                game.setStateNull();
            } else {
                System.out.println("Game is null. No action taken.");
            }
        }
    }

    @Test
    void testSet_ValidGame_ToGameCommand() {
        Game mockGame = mock(Game.class);
        GameCommand command = new TestGameCommand();

        assertDoesNotThrow(() -> command.execute(mockGame), "The execute method should not throw an exception for a valid game.");
        verify(mockGame, times(1)).setStateNull();
    }

    @Test
    void testSet_NullGame_ToGameCommand() {
        GameCommand command = new TestGameCommand();
        assertDoesNotThrow(() -> command.execute(null), "The execute method should not throw an exception even if the game is null.");
    }
}