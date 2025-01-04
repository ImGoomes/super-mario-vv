package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Vector;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ViewerMenuTest {

    private ViewerMenu viewerMenu;
    private StateMenu mockStateMenu;
    private GeneralGui mockGui;
    private MenuModel mockMenuModel;

    @BeforeEach
    void setUp() {
        viewerMenu = new ViewerMenu();
        mockStateMenu = mock(StateMenu.class);
        mockGui = mock(GeneralGui.class);
        mockMenuModel = mock(MenuModel.class);

        when(mockStateMenu.getModel()).thenReturn(mockMenuModel);
    }

    @Test
    void testDraw_SelectedOption() throws IOException {
        when(mockMenuModel.isSelectedOption()).thenReturn(true);
        when(mockMenuModel.getTextOption()).thenReturn("Line 1\nLine 2\nLine 3");

        viewerMenu.draw(mockStateMenu, mockGui);

        verify(mockGui, times(1)).clear();
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(12), eq("Line 1"), eq(""));
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(24), eq("Line 2"), eq(""));
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(36), eq("Line 3"), eq(""));

        int yForPressEnter = 48 + 12;
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(yForPressEnter), eq("press enter to go back to menu"), eq(""));
        verify(mockGui, times(1)).refresh();
    }

    @Test
    void testDraw_ButtonOptions() throws IOException {
        Vector<Button> buttons = new Vector<>();
        buttons.add(new Button("start game", null));
        buttons.add(new Button("instructions screen", null));
        buttons.add(new Button("exit game", null));

        when(mockMenuModel.isSelectedOption()).thenReturn(false);
        when(mockMenuModel.getButtons()).thenReturn(buttons);
        when(mockMenuModel.getSelectedButton()).thenReturn((byte) 1);

        viewerMenu.draw(mockStateMenu, mockGui);

        verify(mockGui, times(1)).clear();
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(121), eq("start game"), eq(""));
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(145), eq("instructions screen"), eq("#ea9e22"));
        verify(mockGui, times(1)).drawMenuText(anyInt(), eq(169), eq("exit game"), eq(""));
        verify(mockGui, times(1)).drawMenuImage(eq(121), eq(17), eq("MenuScreen.png"));
        verify(mockGui, times(1)).refresh();
    }


    @Test
    void testDraw_WithNoButtons() throws IOException {
        when(mockMenuModel.isSelectedOption()).thenReturn(false);
        when(mockMenuModel.getButtons()).thenReturn(new Vector<>());

        viewerMenu.draw(mockStateMenu, mockGui);

        verify(mockGui, times(1)).clear();
        verify(mockGui, never()).drawMenuText(anyInt(), anyInt(), anyString(), anyString());
        verify(mockGui, times(1)).drawMenuImage(eq(121), eq(17), eq("MenuScreen.png"));
        verify(mockGui, times(1)).refresh();
    }
}
