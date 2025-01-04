package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Mushroom1UPTest {

    private Mushroom1UP mushroom1UP;

    @BeforeEach
    void setUp() {
        mushroom1UP = new Mushroom1UP(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, mushroom1UP.getX(), "Initial x position should be 10.");
        assertEquals(20, mushroom1UP.getY(), "Initial y position should be 20.");
    }

    @Test
    void testActivate() {
        Mario mockMario = mock(Mario.class);
        when(mockMario.getLives()).thenReturn(3);

        mushroom1UP.activate(mockMario);

        verify(mockMario, times(1)).setLives(4);
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera mockCamera = mock(Camera.class);
        when(mockCamera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, mushroom1UP.getVirtX(mockCamera), "Mushroom1UP's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, mushroom1UP.getVirtY(), "Mushroom1UP's virtual y position should remain unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("lifeMushroom.png", mushroom1UP.getImage(), "Mushroom1UP's image should be 'lifeMushroom.png'.");
    }
}
