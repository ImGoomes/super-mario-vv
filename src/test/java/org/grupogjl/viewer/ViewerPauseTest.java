package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.StatePause;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Vector;

import static org.mockito.Mockito.*;

public class ViewerPauseTest {

    @Mock
    private GeneralGui gui;

    @Mock
    private StatePause statePause;

    private ViewerPause viewer;

    @Mock
    private PauseModel pauseModel;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewer = new ViewerPause();
        when(statePause.getModel()).thenReturn(pauseModel);
    }

    @Test
    public void testDraw_WithMultipleButtons() throws IOException {
        Vector<Button> buttons = new Vector<>();
        Button button1 = mock(Button.class);
        Button button2 = mock(Button.class);
        Button button3 = mock(Button.class);

        when(button1.getText()).thenReturn("Resume");
        when(button2.getText()).thenReturn("Settings");
        when(button3.getText()).thenReturn("Quit");

        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);

        when(statePause.getModel().getButtons()).thenReturn(buttons);
        doReturn((byte) 1).when(pauseModel).getSelectedButton();

        viewer.draw(statePause, gui);

        verify(gui).drawMenuText((416 - "Resume".length() * 8) / 2 + 1, 89, "Resume", "");
        verify(gui).drawMenuText((416 - "Settings".length() * 8) / 2 + 1, 113, "Settings", "#ea9e22");
        verify(gui).drawMenuText((416 - "Quit".length() * 8) / 2 + 1, 137, "Quit", "");

        verify(gui).refresh();
    }

    @Test
    public void testDraw_WithNoButtons() throws IOException {
        when(statePause.getModel().getButtons()).thenReturn(new Vector<>());
        doReturn((byte) 0).when(pauseModel).getSelectedButton();

        viewer.draw(statePause, gui);

        verify(gui, never()).drawMenuText(anyInt(), anyInt(), anyString(), anyString());

        verify(gui).refresh();
    }

    @Test
    public void testDraw_WithSingleButton() throws IOException {
        Vector<Button> buttons = new Vector<>();
        Button button = mock(Button.class);

        when(button.getText()).thenReturn("Resume");
        buttons.add(button);

        when(statePause.getModel().getButtons()).thenReturn(buttons);
        doReturn((byte) 0).when(pauseModel).getSelectedButton();

        viewer.draw(statePause, gui);

        verify(gui).drawMenuText((416 - "Resume".length() * 8) / 2 + 1, 89, "Resume", "#ea9e22");

        verify(gui).refresh();
    }
}
