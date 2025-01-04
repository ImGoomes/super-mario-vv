package org.grupogjl.controller.game;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.PhysicalObject;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerLevelTest {

    private ControllerLevel controllerLevel;
    private Level mockLevel;
    private Mario mockMario;
    private Camera mockCamera;
    private List<GameObject> mockObjects;

    @BeforeEach
    void setUp() {
        controllerLevel = new ControllerLevel();
        mockLevel = mock(Level.class);
        mockMario = mock(Mario.class);
        mockCamera = mock(Camera.class);
        mockObjects = new ArrayList<>();

        when(mockLevel.getMario()).thenReturn(mockMario);
        when(mockLevel.getCamera()).thenReturn(mockCamera);
        when(mockLevel.getObjects()).thenReturn(mockObjects);
    }

    @Test
    void testStepWithValidInputs() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockLevel, atLeastOnce()).getMario();
        verify(mockLevel, atLeastOnce()).getCamera();
        verify(mockLevel, atLeastOnce()).getObjects();
    }

    @Test
    void testCheckCollisionsMarioWithObjects() {
        // Arrange: Create a mock PhysicalObject to avoid casting issues
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        when(mockPhysicalObject.getX()).thenReturn(10.0f);
        when(mockPhysicalObject.getY()).thenReturn(5.0f);
        when(mockPhysicalObject.getWidth()).thenReturn(2.0f);
        when(mockPhysicalObject.getHeight()).thenReturn(2.0f);
        when(mockPhysicalObject.collidesWith(any())).thenReturn(true);

        // Add the mock PhysicalObject to the objects list
        mockObjects.add(mockPhysicalObject);

        // Act: Call the method under test
        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        // Assert: Verify that the collision logic interacted with the physical object
        verify(mockPhysicalObject, atLeastOnce()).getX();
        verify(mockPhysicalObject, atLeastOnce()).getVx();
        verify(mockMario, atLeastOnce()).setFalling(true);
    }


    @Test
    void testCheckPhysicalCollisionsYWithFallingObject() {
        GameObject mockBlock = mock(GameObject.class);
        when(mockBlock.getX()).thenReturn(5.0f);
        when(mockBlock.getY()).thenReturn(5.0f);

        when(mockMario.getX()).thenReturn(5.0f);
        when(mockMario.getY()).thenReturn(4.0f);
        when(mockMario.getVy()).thenReturn(-1.0f);

        mockObjects.add(mockBlock);

        controllerLevel.CheckPhysicalCollisionsY(mockMario, mockObjects);

        verify(mockMario, atLeastOnce()).getVy();
        verify(mockMario, atLeastOnce()).isJumping();
    }

    @Test
    void testCheckPhysicalCollisionsXWithMovingObject() {
        GameObject mockObject = mock(GameObject.class);
        when(mockObject.getX()).thenReturn(10.0f);

        when(mockMario.getX()).thenReturn(9.0f);
        when(mockMario.getVx()).thenReturn(1.0f);

        mockObjects.add(mockObject);

        controllerLevel.CheckPhysicalCollisionsX(mockMario, mockObjects, mockCamera);

        verify(mockMario, atLeastOnce()).getVx();
        verify(mockMario, atLeastOnce()).setX(anyFloat());
    }

    @Test
    void testMarioJumpingAndFalling() {
        when(mockMario.isJumping()).thenReturn(true);
        when(mockMario.isFalling()).thenReturn(false);

        controllerLevel.CheckPhysicalCollisionsY(mockMario, mockObjects);

        verify(mockMario, atLeastOnce()).isJumping();
    }

    @Test
    void testCameraInteractionDuringCollision() {
        when(mockCamera.getLeftCamLimit()).thenReturn(0.0f);

        controllerLevel.CheckPhysicalCollisionsX(mockMario, mockObjects, mockCamera);

        verify(mockCamera, atLeastOnce()).getLeftCamLimit();
        verify(mockMario, atLeastOnce()).getX();
    }
}
