package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateMenu;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Vector;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ViewerMenuTest {
    @Mock
    private GeneralGui gui;

    @Mock
    private StateMenu stateMenu;

    @Mock
    private MenuModel menuModel;

    private ViewerMenu viewer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewer = new ViewerMenu();
        when(stateMenu.getModel()).thenReturn(menuModel);
    }

    @Test
    public void testDraw_SelectedOption() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(true);
        String optionText = "Line 1\nLine 2\nLine 3";
        when(menuModel.getTextOption()).thenReturn(optionText);

        viewer.draw(stateMenu, gui);

        verify(gui).clear();
        verify(gui).refresh();

        int x1 = (416 - "Line 1".length() * 8) / 2 + 1;
        int x2 = (416 - "Line 2".length() * 8) / 2 + 1;
        int x3 = (416 - "Line 3".length() * 8) / 2 + 1;
        String enterMsg = "press enter to go back to menu";
        int enterX = (416 - enterMsg.length() * 8) / 2 + 1;

        verify(gui).drawMenuText(x1, 12, "Line 1", "");
        verify(gui).drawMenuText(x2, 24, "Line 2", "");
        verify(gui).drawMenuText(x3, 36, "Line 3", "");
        verify(gui).drawMenuText(enterX, 60, enterMsg, "");
    }

    @Test
    public void testDraw_MenuButtons() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(false);
        Vector<Button> buttons = new Vector<>();
        Button button1 = mock(Button.class);
        Button button2 = mock(Button.class);
        Button button3 = mock(Button.class);

        when(button1.getText()).thenReturn("Start Game");
        when(button2.getText()).thenReturn("Instructions");
        when(button3.getText()).thenReturn("Exit");

        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);

        when(menuModel.getButtons()).thenReturn(buttons);
        doReturn((byte) 1).when(menuModel).getSelectedButton();

        viewer.draw(stateMenu, gui);

        int screenWidth = 416;
        int charWidth = 8;

        int startGameX = (screenWidth - "Start Game".length() * charWidth) / 2 + 1;
        int instructionsX = (screenWidth - "Instructions".length() * charWidth) / 2 + 1;
        int exitX = (screenWidth - "Exit".length() * charWidth) / 2 + 1;

        verify(gui).clear();
        verify(gui).refresh();
        verify(gui).drawMenuImage(121, 17, "MenuScreen.png");

        verify(gui).drawMenuText(startGameX, 121, "Start Game", "");
        verify(gui).drawMenuText(instructionsX, 145, "Instructions", "#ea9e22");
        verify(gui).drawMenuText(exitX, 169, "Exit", "");
    }

    @Test
    public void testDraw_EmptyMenu() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(false);
        when(menuModel.getButtons()).thenReturn(new Vector<>());
        doReturn((byte)0).when(menuModel).getSelectedButton();

        viewer.draw(stateMenu, gui);

        verify(gui).clear();
        verify(gui).drawMenuImage(121, 17, "MenuScreen.png");
        verify(gui).refresh();
        verify(gui, never()).drawMenuText(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    public void testDraw_SelectedOptionWithEmptyText() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(true);
        when(menuModel.getTextOption()).thenReturn("");

        viewer.draw(stateMenu, gui);

        String enterMsg = "press enter to go back to menu";
        int screenWidth = 416;
        int charWidth = 8;
        int expectedX = (screenWidth - enterMsg.length() * charWidth) / 2 + 1;

        verify(gui).clear();
        verify(gui).refresh();

        verify(gui).drawMenuText(expectedX, 24, enterMsg, "");
    }
}