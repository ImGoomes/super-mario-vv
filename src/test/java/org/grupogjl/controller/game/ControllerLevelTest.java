package org.grupogjl.controller.game;

import org.grupogjl.controller.game.blocks.ControllerBlocks;
import org.grupogjl.controller.game.physicalobjects.ControllerFireBall;
import org.grupogjl.controller.game.physicalobjects.ControllerMario;
import org.grupogjl.controller.game.physicalobjects.ControllerEnemy;
import org.grupogjl.controller.game.surprises.ControllerSurprises;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.blocks.BuildingBlock;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.PhysicalObject;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ControllerLevelTest {

    private ControllerLevel controllerLevel;

    @Mock
    private Level mockLevel;

    @Mock
    private Mario mockMario;

    @Mock
    private Camera mockCamera;

    @Mock
    private ControllerMario mockControllerMario;

    @Mock
    private ControllerEnemy mockControllerEnemy;

    @Mock
    private ControllerBlocks mockControllerBlocks;

    @Mock
    private ControllerSurprises mockControllerSurprises;

    @Mock
    private ControllerFireBall mockControllerFireBall;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controllerLevel = spy(new ControllerLevel());

        doReturn(mockControllerMario).when(controllerLevel).getControllerMario();
        doReturn(mockControllerEnemy).when(controllerLevel).getControllerEnemy();
        doReturn(mockControllerBlocks).when(controllerLevel).getControllerBlocks();
        doReturn(mockControllerSurprises).when(controllerLevel).getControllerSurprises();
        doReturn(mockControllerFireBall).when(controllerLevel).getControllerFireBall();

        when(mockLevel.getMario()).thenReturn(mockMario);
        when(mockLevel.getCamera()).thenReturn(mockCamera);
        when(mockLevel.getObjects()).thenReturn(new ArrayList<>());
    }

    @Test
    public void test_AllControllers() {
        long time = 100L;
        GeneralGui.ACTION action = GeneralGui.ACTION.NONE;

        controllerLevel.step(mockLevel, action, time);

        verify(mockControllerFireBall).step(mockLevel, action, time);
        verify(mockControllerMario).step(mockLevel, action, time);
        verify(mockControllerEnemy).step(mockLevel, time);
        verify(mockControllerSurprises).step(mockLevel, action, time);
        verify(mockControllerBlocks).step(mockLevel, action, time);

        verify(mockLevel, atLeastOnce()).getMario();
        verify(mockLevel, atLeastOnce()).getCamera();
        verify(mockLevel, atLeastOnce()).getObjects();
    }

    @Test
    public void test_CheckCollisionsWithWall() {
        PhysicalObject testObject = mock(PhysicalObject.class);
        when(testObject.getX()).thenReturn(0f);
        when(testObject.getVx()).thenReturn(-1f);
        when(mockCamera.getLeftCamLimit()).thenReturn(0f);
        List<GameObject> objects = new ArrayList<>();

        controllerLevel.CheckPhysicalCollisionsX(testObject, objects, mockCamera);

        verify(testObject).handleWallColision(0f);
    }

    @Test
    public void test_VerticalCollisionWithBlock() {
        PhysicalObject testObject = mock(PhysicalObject.class);
        BuildingBlock block = mock(BuildingBlock.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(block);

        when(testObject.getX()).thenReturn(10f);
        when(testObject.getY()).thenReturn(30f);
        when(testObject.getWidth()).thenReturn(10f);
        when(block.getX()).thenReturn(50f);
        when(block.getY()).thenReturn(60f);
        when(block.getWidth()).thenReturn(10f);
        when(block.getHeight()).thenReturn(20f);
        when(testObject.isJumping()).thenReturn(false);
        when(testObject.getVy()).thenReturn(5f);

        controllerLevel.CheckPhysicalCollisionsY(testObject, objects);

        verify(testObject).setFalling(true);
    }


    @Test
    public void test_HorizontalCollisionBetweenPhysicalObjects() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject otherObject = mock(PhysicalObject.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(otherObject);

        when(mainObject.getVx()).thenReturn(5f);
        when(mainObject.collidesWith(otherObject)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, mockCamera);

        verify(mainObject).handleCollision(otherObject, 'R');
    }

    @Test
    public void test_ControllerGetters() {
        assertNotNull(controllerLevel.getControllerMario());
        assertNotNull(controllerLevel.getControllerEnemy());
        assertNotNull(controllerLevel.getControllerBlocks());
        assertNotNull(controllerLevel.getControllerSurprises());
        assertNotNull(controllerLevel.getControllerFireBall());
    }

    @Test
    public void test_FallingCollision() {
        PhysicalObject testObject = mock(PhysicalObject.class);
        GameObject ground = mock(GameObject.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(ground);

        when(testObject.isJumping()).thenReturn(false);
        when(testObject.getVy()).thenReturn(15f);
        when(testObject.getY()).thenReturn(100f);

        when(testObject.collidesWith(any(GameObject.class))).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsY(testObject, objects);

        verify(testObject, atLeastOnce()).collidesWith(any(GameObject.class));
        verify(testObject).handleCollision(any(GameObject.class), eq('D'));
    }
}