package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Vector;

import static org.mockito.Mockito.*;

class ViewerPauseTest {

    private ViewerPause viewerPause;
    private StatePause mockStatePause;
    private GeneralGui mockGui;
    private PauseModel mockPauseModel;

    @BeforeEach
    void setUp() {
        viewerPause = new ViewerPause();
        mockStatePause = mock(StatePause.class);
        mockGui = mock(GeneralGui.class);
        mockPauseModel = mock(PauseModel.class);

        when(mockStatePause.getModel()).thenReturn(mockPauseModel);
    }

    @Test
    void testDraw_SelectedButton() throws IOException {
        Vector<Button> buttons = new Vector<>();
        buttons.add(new Button("resume", null));
        buttons.add(new Button("exit to menu", null));

        when(mockPauseModel.getButtons()).thenReturn(buttons);
        when(mockPauseModel.getSelectedButton()).thenReturn((byte) 0);

        viewerPause.draw(mockStatePause, mockGui);

        verify(mockGui, times(1)).drawMenuText(eq((416 - "resume".length() * 8) / 2 + 1), eq(89), eq("resume"), eq("#ea9e22"));
        verify(mockGui, times(1)).drawMenuText(eq((416 - "exit to menu".length() * 8) / 2 + 1), eq(113), eq("exit to menu"), eq(""));
        verify(mockGui, times(1)).refresh();
    }

    @Test
    void testDraw_NonSelectedButton() throws IOException {
        Vector<Button> buttons = new Vector<>();
        buttons.add(new Button("resume", null));
        buttons.add(new Button("exit to menu", null));

        when(mockPauseModel.getButtons()).thenReturn(buttons);
        when(mockPauseModel.getSelectedButton()).thenReturn((byte) 1);

        viewerPause.draw(mockStatePause, mockGui);

        verify(mockGui, times(1)).drawMenuText(eq((416 - "resume".length() * 8) / 2 + 1), eq(89), eq("resume"), eq(""));
        verify(mockGui, times(1)).drawMenuText(eq((416 - "exit to menu".length() * 8) / 2 + 1), eq(113), eq("exit to menu"), eq("#ea9e22"));
        verify(mockGui, times(1)).refresh();
    }

    @Test
    void testDraw_WithNoButtons() throws IOException {
        when(mockPauseModel.getButtons()).thenReturn(new Vector<>());

        viewerPause.draw(mockStatePause, mockGui);

        verify(mockGui, never()).drawMenuText(anyInt(), anyInt(), anyString(), anyString());
        verify(mockGui, times(1)).refresh();
    }
}
