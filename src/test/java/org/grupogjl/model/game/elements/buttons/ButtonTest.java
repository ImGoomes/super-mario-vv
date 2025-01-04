package org.grupogjl.model.game.elements.buttons;

import org.grupogjl.Game;
import org.grupogjl.commands.GameCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ButtonTest {

    private Button button;
    private GameCommand mockCommand;
    private Game mockGame;

    @BeforeEach
    void setUp() {
        mockCommand = mock(GameCommand.class);
        mockGame = mock(Game.class);
        button = new Button("Start Game", mockCommand);
    }

    @Test
    void testConstructor() {
        assertEquals("Start Game", button.getText(), "Text should be initialized correctly");
    }

    @Test
    void testGet_Text() {
        assertEquals("Start Game", button.getText(), "getText should return the correct text");
    }

    @Test
    void testOn_Pressed() {
        button.OnPressed(mockGame);

        verify(mockCommand, times(1)).execute(mockGame);
    }
}
