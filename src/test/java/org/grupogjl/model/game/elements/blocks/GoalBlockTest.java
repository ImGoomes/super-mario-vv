package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GoalBlockTest {

    private GoalBlock goalBlock;
    private Camera mockCamera;

    @BeforeEach
    void setUp() {
        goalBlock = new GoalBlock(10.0f, 20.0f, 30.0f, 40.0f);
        mockCamera = mock(Camera.class);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, goalBlock.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, goalBlock.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(30.0f, goalBlock.getWidth(), "Width should be initialized correctly");
        assertEquals(40.0f, goalBlock.getHeight(), "Height should be initialized correctly");
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, goalBlock.getVirtX(mockCamera), "getVirtX should return the correct virtual X-coordinate");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(-19.0f, goalBlock.getVirtY(), "getVirtY should return the correct virtual Y-coordinate");
    }

    @Test
    void testGet_Image() {
        assertEquals("goalBlock.png", goalBlock.getImage(), "getImage should return the correct image path");
    }
}