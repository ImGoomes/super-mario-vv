package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
        var mockLevel = mock(org.grupogjl.model.game.elements.level.Level.class);

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

    @Test
    void testDraw_NormalGameplay() throws IOException {
        when(mockStateGame.isGameOver()).thenReturn(false);
        when(mockMario.getVirtX(mockCamera)).thenReturn(5.0f);
        when(mockMario.getVirtY()).thenReturn(10.0f);
        when(mockMario.getImage()).thenReturn("mario.png");
        when(mockMario.getCoins()).thenReturn(10);
        when(mockMario.getLives()).thenReturn(3);

        when(mockObject1.getVirtX(mockCamera)).thenReturn(1.0f);
        when(mockObject1.getVirtY()).thenReturn(2.0f);
        when(mockObject1.getImage()).thenReturn("object1.png");
        when(mockObject1.getX()).thenReturn(5f);

        when(mockObject2.getVirtX(mockCamera)).thenReturn(7.0f);
        when(mockObject2.getVirtY()).thenReturn(8.0f);
        when(mockObject2.getImage()).thenReturn("object2.png");
        when(mockObject2.getX()).thenReturn(5f); // Ensure within camera bounds

        when(mockCamera.getLeftCamLimit()).thenReturn(0f);
        when(mockCamera.getRightCamLimit()).thenReturn(10f);

        // Debugging statements
        System.out.println("Object2 X: " + mockObject2.getX());
        System.out.println("Camera Left Limit: " + mockCamera.getLeftCamLimit());
        System.out.println("Camera Right Limit: " + mockCamera.getRightCamLimit());

        viewerGame.draw(mockStateGame, mockGui);

        verify(mockGui).clear();
        verify(mockGui).drawImage(1.0f, 2.0f, "object1.png");
        verify(mockGui).drawImage(7.0f, 8.0f, "object2.png"); // Ensure this is invoked
        verify(mockGui).drawImage(5.0f, 10.0f, "mario.png");
        verify(mockGui).drawMenuText(1, 8, "coins: 10");
        verify(mockGui).drawMenuText(1, 24, "lives: 3");
    }
}
