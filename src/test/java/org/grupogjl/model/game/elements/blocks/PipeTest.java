package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PipeTest {

    private Pipe pipe;
    private Camera mockCamera;

    @BeforeEach
    void setUp() {
        pipe = new Pipe(10.0f, 20.0f, 30.0f, 40.0f);
        mockCamera = mock(Camera.class);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, pipe.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, pipe.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(30.0f, pipe.getWidth(), "Width should be initialized correctly");
        assertEquals(40.0f, pipe.getHeight(), "Height should be initialized correctly");
        assertNull(pipe.getConection(), "Conection should be null by default");
    }

    @Test
    void testSetAndGet_Conection() {
        Pipe anotherPipe = new Pipe(15.0f, 25.0f, 35.0f, 45.0f);
        pipe.setConection(anotherPipe);
        assertEquals(anotherPipe, pipe.getConection(), "Conection should be set and retrieved correctly");
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, pipe.getVirtX(mockCamera), "getVirtX should return the correct virtual X-coordinate");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(-19.0f, pipe.getVirtY(), "getVirtY should return the correct virtual Y-coordinate");
    }

    @Test
    void testGet_Image() {
        assertEquals("pipe40.png", pipe.getImage(), "getImage should return the correct image file name based on height");
    }
}
