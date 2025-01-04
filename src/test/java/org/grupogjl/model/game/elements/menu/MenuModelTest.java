package org.grupogjl.model.game.elements.menu;

import org.grupogjl.Game;
import org.grupogjl.commands.StartGameCommand;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuModelTest {

    private MenuModel menuModel;

    @BeforeEach
    void setUp() {
        menuModel = new MenuModel();
    }

    @Test
    void testMenuModel_Initialization() {
        Vector<Button> buttons = menuModel.getButtons();

        assertEquals(3, buttons.size(), "MenuModel should initialize with 3 buttons.");

        assertEquals("start game", buttons.get(0).getText());
        assertTrue(buttons.get(0).getText().equalsIgnoreCase("start game"));

        assertEquals("instructions screen", buttons.get(1).getText());
        assertTrue(buttons.get(1).getText().equalsIgnoreCase("instructions screen"));

        assertEquals("exit game", buttons.get(2).getText());
        assertTrue(buttons.get(2).getText().equalsIgnoreCase("exit game"));
    }

    @Test
    void testMenuModel_SelectedOption() {
        assertFalse(menuModel.isSelectedOption(), "selectedOption should default to false.");

        menuModel.setSelectedOption(true);
        assertTrue(menuModel.isSelectedOption(), "selectedOption should be true after setting it to true.");
    }

    @Test
    void testMenuModel_TextOption() {
        assertNull(menuModel.getTextOption(), "textOption should default to null.");

        String sampleText = "Sample Option";
        menuModel.setTextOption(sampleText);
        assertEquals(sampleText, menuModel.getTextOption(), "textOption should return the value it was set to.");
    }

    @Test
    void testMenuModel_Navigation() {
        assertEquals(Byte.valueOf((byte) 0), menuModel.getSelectedButton(), "Initial selectedButton should be 0.");

        menuModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 1), menuModel.getSelectedButton(), "selectedButton should move to the next position.");

        menuModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 2), menuModel.getSelectedButton(), "selectedButton should move to the next position.");

        menuModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 2), menuModel.getSelectedButton(), "selectedButton should not exceed the bounds of the button list.");

        menuModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 1), menuModel.getSelectedButton(), "selectedButton should move to the previous position.");

        menuModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 0), menuModel.getSelectedButton(), "selectedButton should move to the previous position.");

        menuModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 0), menuModel.getSelectedButton(), "selectedButton should not go below 0.");
    }

    @Test
    void testMenuModel_ExecuteWithStartGameCommand() throws IOException {
        Game mockGame = mock(Game.class);
        when(mockGame.getStateGame()).thenReturn(mock(StateGame.class));

        menuModel.execute(mockGame);

        verify(mockGame, times(1)).setStateGame();
        verify(mockGame.getStateGame(), times(1)).setGameOver(false);
    }

    @Test
    void testMenuModel_ExecuteThrowsRuntimeException() throws IOException {
        Game mockGame = mock(Game.class);
        doThrow(new IOException("IO Error")).when(mockGame).setStateGame();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuModel.execute(mockGame));
        assertEquals("java.io.IOException: IO Error", exception.getMessage());
    }
}
