package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoinTest {

    private Coin coin;

    @BeforeEach
    void setUp() {
        coin = new Coin(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, coin.getX(), "Initial x position should be 10.");
        assertEquals(20, coin.getY(), "Initial y position should be 20.");
        assertEquals(8, coin.getActivationTimer(), "Initial activation timer should be 8.");
    }

    @Test
    void testSet_ActivationTimer() {
        coin.setActivationTimer(5);
        assertEquals(5, coin.getActivationTimer(), "Activation timer should be updated to 5.");
    }

    @Test
    void testBorn() {
        coin.born();

        assertEquals(0.7f, coin.getVy(), "Vertical velocity should be set to 0.7 after born.");
        assertTrue(coin.isJumping(), "Coin should be in a jumping state after born.");
    }

    @Test
    void testActivate() {
        Mario mockMario = mock(Mario.class);
        when(mockMario.getCoins()).thenReturn(5);

        coin.activate(mockMario);

        verify(mockMario, times(1)).setCoins(6);
    }

    @Test
    void testHandle_Collision() {
        GameObject mockObject = mock(GameObject.class);

        coin.handleCollision(mockObject, ' ');

        assertEquals(0, coin.getVy(), "Vertical velocity should be set to 0 after collision.");
        assertEquals(0, coin.getVx(), "Horizontal velocity should be set to 0 after collision.");
        assertFalse(coin.isFalling(), "Coin should not be falling after collision.");
        assertFalse(coin.isJumping(), "Coin should not be jumping after collision.");
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera mockCamera = mock(Camera.class);
        when(mockCamera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, coin.getVirtX(mockCamera), "Coin's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, coin.getVirtY(), "Coin's virtual y position should remain unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("coin.png", coin.getImage(), "Coin's image should be 'coin.png'.");
    }
}
