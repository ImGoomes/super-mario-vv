package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoombaTest {
    private Goomba goomba;
    private Camera camera;

    @BeforeEach
    void setUp() {
        goomba = new Goomba(10.0f, 15.0f, 3.0f, 3.0f);
        camera = mock(Camera.class);
    }

    @Test
    void testInitialization() {
        assertEquals(10.0f, goomba.getX());
        assertEquals(15.0f, goomba.getY());
        assertEquals(3.0f, goomba.getWidth());
        assertEquals(3.0f, goomba.getHeight());
        assertEquals(1, goomba.getLives());
        assertFalse(goomba.wasRevealed());
    }

    @Test
    void testGetVirtX() {
        when(camera.getLeftCamLimit()).thenReturn(5.0f);

        float virtX = goomba.getVirtX(camera);

        assertEquals(5.0f, virtX);
    }

    @Test
    void testGetVirtY() {
        assertEquals(15.0f, goomba.getVirtY());
    }

    @Test
    void testGetImage() {
        assertEquals("goomba.png", goomba.getImage());
    }
}
