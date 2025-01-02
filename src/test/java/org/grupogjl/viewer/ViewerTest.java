package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.State;
import org.grupogjl.state.StatePause;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Vector;

import static org.mockito.Mockito.*;

public class ViewerTest {

    @Mock
    private GeneralGui gui;

    @Mock
    private StatePause statePause;

    private Viewer viewer;

    @Mock
    private PauseModel pauseModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewer = new ViewerPause();
        when(statePause.getModel()).thenReturn(pauseModel);
    }

    @Test
    public void testDraw_WithValidState() throws IOException {
        Vector<Button> buttons = new Vector<>();
        Button button1 = mock(Button.class);
        Button button2 = mock(Button.class);

        when(button1.getText()).thenReturn("Resume");
        when(button2.getText()).thenReturn("Quit");

        buttons.add(button1);
        buttons.add(button2);

        when(statePause.getModel().getButtons()).thenReturn(buttons);
        doReturn((byte) 0).when(pauseModel).getSelectedButton();

        viewer.draw(statePause, gui);

        verify(gui).drawMenuText((416 - "Resume".length() * 8) / 2 + 1, 89, "Resume", "#ea9e22");
        verify(gui).drawMenuText((416 - "Quit".length() * 8) / 2 + 1, 113, "Quit", "");

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

    @Test(expected = ClassCastException.class)
    public void testDraw_WithInvalidState() throws IOException {
        State invalidState = mock(State.class);

        viewer.draw(invalidState, gui);
    }
}
