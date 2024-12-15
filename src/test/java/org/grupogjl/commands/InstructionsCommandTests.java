package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InstructionsCommandTests {

    private InstructionsCommand instructionsCommand;
    private Game mockGame;
    private StateMenu mockStateMenu;
    private MenuModel mockModel;

    @BeforeEach
    void setup() {
        instructionsCommand = new InstructionsCommand();
        mockGame = mock(Game.class);
        mockStateMenu = mock(StateMenu.class);
        mockModel = mock(MenuModel.class);

        when(mockGame.getStateMenu()).thenReturn(mockStateMenu);
        when(mockStateMenu.getModel()).thenReturn(mockModel);
    }

    @Test
    void testSet_Instructions_ToGameStateMenu() {
        InstructionsCommand command = new InstructionsCommand();
        String expectedText = """
                upper arrow to jump
                right arrow to move right
                left arrow to move left
                down arrow to enter pipe
                b to throw fireballs
                q to pause
                """;

        command.execute(mockGame);

        verify(mockModel).setSelectedOption(true);
        verify(mockModel).setTextOption(expectedText);
    }

    @Test
    void testSet_NullPointer_ToInstructionsCommand() {
        assertThrows(NullPointerException.class, () -> instructionsCommand.execute(null), "Executing with a null game should throw a NullPointerException.");
    }
}