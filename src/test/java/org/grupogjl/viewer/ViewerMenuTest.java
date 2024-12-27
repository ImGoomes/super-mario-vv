package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static org.mockito.Mockito.*;

class ViewerMenuTest {

    private ViewerMenu viewerMenu;
    private GeneralGui mockGui;
    private StateMenu mockStateMenu;
    private StateMenu.Model mockModel;

    @BeforeEach
    void setup() {
        // Initialize ViewerMenu
        viewerMenu = new ViewerMenu();

        // Mock dependencies
        mockGui = mock(GeneralGui.class);
        mockStateMenu = mock(StateMenu.class);
        mockModel = mock(StateMenu.Model.class);

        // Mock StateMenu behavior
        when(mockStateMenu.getModel()).thenReturn(mockModel);
    }

    @Test
    void testDraw_SelectedOption() throws IOException {
        // Arrange
        when(mockModel.isSelectedOption()).thenReturn(true);
        when(mockModel.getTextOption()).thenReturn("Option 1\nOption 2");

        // Act
        viewerMenu.draw(mockStateMenu, mockGui);

        // Assert
        verify(mockGui).clear();
        verify(mockGui).drawMenuText(anyInt(), eq(12), eq("Option 1"), eq(""));
        verify(mockGui).drawMenuText(anyInt(), eq(24), eq("Option 2"), eq(""));
        verify(mockGui).drawMenuText(anyInt(), eq(36), eq("press enter to go back to menu"), eq(""));
        verify(mockGui).refresh();
    }

    @Test
    void testDraw_NoSelectedOption() throws IOException {
        // Arrange
        when(mockModel.isSelectedOption()).thenReturn(false);

        // Mock buttons
        Vector<StateMenu.Button> buttons = new Vector<>();
        buttons.add(new StateMenu.Button("Start"));
        buttons.add(new StateMenu.Button("Options"));
        buttons.add(new StateMenu.Button("Exit"));
        when(mockModel.getButtons()).thenReturn(buttons);
        when(mockModel.getSelectedButton()).thenReturn(1); // "Options" is selected

        // Act
        viewerMenu.draw(mockStateMenu, mockGui);

        // Assert
        verify(mockGui).clear();
        verify(mockGui).drawMenuText(anyInt(), eq(121), eq("Start"), eq(""));
        verify(mockGui).drawMenuText(anyInt(), eq(145), eq("Options"), eq("#ea9e22")); // Highlighted
        verify(mockGui).drawMenuText(anyInt(), eq(169), eq("Exit"), eq(""));
        verify(mockGui).drawMenuImage(121, 17, "MenuScreen.png");
        verify(mockGui).refresh();
    }
}
