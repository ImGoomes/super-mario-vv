package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KoopaTroopaTest {
    private KoopaTroopa koopaTroopa;
    private Camera camera;

    @BeforeEach
    void setUp() {
        koopaTroopa = new KoopaTroopa(10.0f, 15.0f, 3.0f, 3.0f);
        camera = mock(Camera.class);
    }

    @Test
    void testInitialization() {
        assertEquals(10.0f, koopaTroopa.getX());
        assertEquals(15.0f, koopaTroopa.getY());
        assertEquals(3.0f, koopaTroopa.getWidth());
        assertEquals(3.0f, koopaTroopa.getHeight());
        assertEquals(2, koopaTroopa.getLives());
        assertFalse(koopaTroopa.wasRevealed());
    }

    @Test
    void testMoveLeft_WhenLivesIs2() {
        koopaTroopa.setLives(2);
        koopaTroopa.moveLeft();

        assertEquals(-0.2f, koopaTroopa.getVx());
    }

    @Test
    void testMoveLeft_WhenLivesIs1() {
        koopaTroopa.setLives(1);
        koopaTroopa.moveLeft();

        assertEquals(-2.0f, koopaTroopa.getVx());
    }

    @Test
    void testMoveRight_WhenLivesIs2() {
        koopaTroopa.setLives(2);
        koopaTroopa.moveRight();

        assertEquals(0.2f, koopaTroopa.getVx());
    }

    @Test
    void testMoveRight_WhenLivesIs1() {
        koopaTroopa.setLives(1);
        koopaTroopa.moveRight();

        assertEquals(2.0f, koopaTroopa.getVx());
    }

    @Test
    void testGetVirtX() {
        when(camera.getLeftCamLimit()).thenReturn(5.0f);

        float virtX = koopaTroopa.getVirtX(camera);

        assertEquals(5.0f, virtX);
    }

    @Test
    void testGetVirtY_WhenLivesIs2() {
        koopaTroopa.setLives(2);

        float virtY = koopaTroopa.getVirtY();

        assertEquals(15.0f - 0.3125f, virtY, 0.0001f);
    }

    @Test
    void testGetVirtY_WhenLivesIs1() {
        koopaTroopa.setLives(1);

        float virtY = koopaTroopa.getVirtY();

        assertEquals(15.0f, virtY);
    }

    @Test
    void testGetImage_WhenLivesIs2() {
        koopaTroopa.setLives(2);

        String image = koopaTroopa.getImage();

        assertEquals("koopaTroopa.png", image);
    }

    @Test
    void testGetImage_WhenLivesIs1() {
        koopaTroopa.setLives(1);

        String image = koopaTroopa.getImage();

        assertEquals("koopaShell.png", image);
    }
}
