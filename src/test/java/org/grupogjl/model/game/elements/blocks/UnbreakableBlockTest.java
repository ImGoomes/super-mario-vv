package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnbreakableBlockTest {

    private UnbreakableBlock unbreakableBlock;
    private Camera mockCamera;

    @BeforeEach
    void setUp() {
        unbreakableBlock = new UnbreakableBlock(10.0f, 20.0f, 30.0f, 40.0f);
        mockCamera = mock(Camera.class);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, unbreakableBlock.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, unbreakableBlock.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(30.0f, unbreakableBlock.getWidth(), "Width should be initialized correctly");
        assertEquals(40.0f, unbreakableBlock.getHeight(), "Height should be initialized correctly");
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, unbreakableBlock.getVirtX(mockCamera), "getVirtX should return the correct virtual X-coordinate");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(20.0f, unbreakableBlock.getVirtY(), "getVirtY should return the correct virtual Y-coordinate");
    }

    @Test
    void testGet_Image() {
        assertEquals("unbreakableBlock.png", unbreakableBlock.getImage(), "getImage should return the correct image file name");
    }
}
