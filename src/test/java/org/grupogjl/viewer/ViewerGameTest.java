package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class ViewerGameTest {

    private ViewerGame viewerGame;
    private GeneralGui mockGui;
    private StateGame mockStateGame;
    private Mario mockMario;
    private Camera mockCamera;
    private GameObject mockObject1;
    private GameObject mockObject2;

    @BeforeEach
    void setup() {
        viewerGame = new ViewerGame();

        mockGui = mock(GeneralGui.class);
        mockStateGame = mock(StateGame.class);
        mockMario = mock(Mario.class);
        mockCamera = mock(Camera.class);
        mockObject1 = mock(GameObject.class);
        mockObject2 = mock(GameObject.class);
        Level mockLevel = mock(Level.class);

        when(mockStateGame.getModel()).thenReturn(mockLevel);
        when(mockLevel.getMario()).thenReturn(mockMario);
        when(mockLevel.getCamera()).thenReturn(mockCamera);
        when(mockLevel.getObjects()).thenReturn(List.of(mockObject1, mockObject2));
    }

    @Test
    void testDraw_GameOver() throws IOException {
        when(mockStateGame.isGameOver()).thenReturn(true);

        viewerGame.draw(mockStateGame, mockGui);

        verify(mockGui).clear();
        verify(mockGui).drawGameOver();
        verifyNoMoreInteractions(mockGui);
    }
}
