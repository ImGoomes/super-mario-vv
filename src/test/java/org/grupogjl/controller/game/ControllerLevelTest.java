package org.grupogjl.controller.game;

import org.grupogjl.controller.game.blocks.ControllerBlocks;
import org.grupogjl.controller.game.physicalobjects.ControllerEnemy;
import org.grupogjl.controller.game.physicalobjects.ControllerFireBall;
import org.grupogjl.controller.game.physicalobjects.ControllerMario;
import org.grupogjl.controller.game.surprises.ControllerSurprises;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.BuildingBlock;
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
    private PhysicalObject mockPhysicalObject;
    private Level mockLevel;
    private Mario mockMario;
    private Camera mockCamera;
    private List<GameObject> mockObjects;

    @BeforeEach
    void setUp() {
        controllerLevel = new ControllerLevel();
        mockPhysicalObject = mock(PhysicalObject.class);
        mockLevel = mock(Level.class);
        mockMario = mock(Mario.class);
        mockCamera = mock(Camera.class);
        mockObjects = new ArrayList<>();

        when(mockLevel.getMario()).thenReturn(mockMario);
        when(mockLevel.getCamera()).thenReturn(mockCamera);
        when(mockLevel.getObjects()).thenReturn(mockObjects);
    }

    @Test
    void testStep_WithValidInputs() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockLevel, atLeastOnce()).getMario();
        verify(mockLevel, atLeastOnce()).getCamera();
        verify(mockLevel, atLeastOnce()).getObjects();
    }

    @Test
    void testCheck_CollisionsMarioWithObjects() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        when(mockPhysicalObject.getX()).thenReturn(10.0f);
        when(mockPhysicalObject.getY()).thenReturn(5.0f);
        when(mockPhysicalObject.getWidth()).thenReturn(2.0f);
        when(mockPhysicalObject.getHeight()).thenReturn(2.0f);
        when(mockPhysicalObject.collidesWith(any())).thenReturn(true);

        mockObjects.add(mockPhysicalObject);

        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        verify(mockPhysicalObject, atLeastOnce()).getX();
        verify(mockPhysicalObject, atLeastOnce()).getVx();
        verify(mockMario, atLeastOnce()).setFalling(true);
    }


    @Test
    void testCheck_PhysicalCollisionsYWithFallingObject() {
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
    void testCheck_PhysicalCollisionsXWithMovingObject() {
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
    void testMario_JumpingAndFalling() {
        when(mockMario.isJumping()).thenReturn(true);
        when(mockMario.isFalling()).thenReturn(false);

        controllerLevel.CheckPhysicalCollisionsY(mockMario, mockObjects);

        verify(mockMario, atLeastOnce()).isJumping();
    }

    @Test
    void testCamera_InteractionDuringCollision() {
        when(mockCamera.getLeftCamLimit()).thenReturn(0.0f);

        controllerLevel.CheckPhysicalCollisionsX(mockMario, mockObjects, mockCamera);

        verify(mockCamera, atLeastOnce()).getLeftCamLimit();
        verify(mockMario, atLeastOnce()).getX();
    }

    @Test
    void testCheck_PhysicalCollisionsYWithJumpingObject() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        GameObject mockObject = mock(GameObject.class);

        when(mockPhysicalObject.getVy()).thenReturn(1.0f);
        when(mockPhysicalObject.isJumping()).thenReturn(true);
        when(mockPhysicalObject.getY()).thenReturn(10.0f);
        when(mockPhysicalObject.collidesWith(mockObject)).thenReturn(true);

        List<GameObject> objects = new ArrayList<>();
        objects.add(mockObject);

        controllerLevel.CheckPhysicalCollisionsY(mockPhysicalObject, objects);

        verify(mockPhysicalObject, atLeastOnce()).collidesWith(mockObject);
        verify(mockPhysicalObject, atLeastOnce()).handleCollision(mockObject, 'U');
    }

    @Test
    void testCheck_PhysicalCollisionsYWithFallingObjectD() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        GameObject mockBlock = mock(GameObject.class);

        when(mockPhysicalObject.getVy()).thenReturn(3.0f);
        when(mockPhysicalObject.isJumping()).thenReturn(false);
        when(mockPhysicalObject.getY()).thenReturn(5.0f);
        when(mockPhysicalObject.getHeight()).thenReturn(1.0f);
        when(mockPhysicalObject.collidesWith(mockBlock)).thenReturn(true);

        List<GameObject> objects = new ArrayList<>();
        objects.add(mockBlock);

        controllerLevel.CheckPhysicalCollisionsY(mockPhysicalObject, objects);

        verify(mockPhysicalObject, atLeastOnce()).setY(anyFloat());
        verify(mockPhysicalObject, atLeastOnce()).collidesWith(mockBlock);
        verify(mockPhysicalObject, atLeastOnce()).handleCollision(mockBlock, 'D');
        verify(mockPhysicalObject, atLeastOnce()).getVy();
    }

    @Test
    void testCheck_PhysicalCollisionsY_ReachesMovementStep() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        GameObject mockBlock = mock(GameObject.class);

        when(mockPhysicalObject.getVy()).thenReturn(3.0f);
        when(mockPhysicalObject.isJumping()).thenReturn(false);
        when(mockPhysicalObject.getY()).thenReturn(5.0f);
        when(mockPhysicalObject.getHeight()).thenReturn(1.0f);
        when(mockPhysicalObject.collidesWith(mockBlock)).thenReturn(false);

        List<GameObject> objects = new ArrayList<>();
        objects.add(mockBlock);

        controllerLevel.CheckPhysicalCollisionsY(mockPhysicalObject, objects);

        verify(mockPhysicalObject, atLeastOnce()).setY(anyFloat());
        verify(mockPhysicalObject, atLeastOnce()).getVy();
    }

    @Test
    void testCheckPhysicalCollisionsX_WithLeftCameraLimitCollision() {
        when(mockPhysicalObject.getX()).thenReturn(0.0f);
        when(mockPhysicalObject.getVx()).thenReturn(-1.0f);
        when(mockCamera.getLeftCamLimit()).thenReturn(0.0f);

        controllerLevel.CheckPhysicalCollisionsX(mockPhysicalObject, mockObjects, mockCamera);

        verify(mockPhysicalObject, times(1)).handleWallColision(0.0f);
    }

    @Test
    void testCheckPhysicalCollisionsX_EnemyCollisionFromLeft() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getY()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(0.0f);
        when(enemy.getX()).thenReturn(6.0f);
        when(enemy.getY()).thenReturn(4.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(enemy, 'L');
    }

    @Test
    void testCheckPhysicalCollisionsX_EnemyCollisionFromRight() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getY()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(0.0f);
        when(enemy.getX()).thenReturn(4.0f);
        when(enemy.getY()).thenReturn(4.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(enemy, 'R');
    }

    @Test
    void testCheckPhysicalCollisionsX_CollisionWithObjectMovingRight() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        GameObject object = mock(GameObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(object);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(3.0f);
        when(mainObject.collidesWith(object)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(object, 'R');
    }

    @Test
    void testCheckPhysicalCollisionsX_CollisionWithObjectMovingLeft() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        GameObject object = mock(GameObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(object);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(-3.0f);
        when(mainObject.collidesWith(object)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(object, 'L');
    }

    @Test
    void testEnemyCollision_LeftSide_Above() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getY()).thenReturn(6.0f);
        when(enemy.getX()).thenReturn(6.0f);
        when(enemy.getY()).thenReturn(5.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(enemy, 'L');
    }

    @Test
    void testEnemyCollision_LeftSide_Below() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getY()).thenReturn(4.0f);
        when(enemy.getX()).thenReturn(6.0f);
        when(enemy.getY()).thenReturn(5.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, never()).handleCollision(enemy, 'L');
    }

    @Test
    void testEnemyCollision_RightSide_Above() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(6.0f);
        when(mainObject.getY()).thenReturn(6.0f);
        when(enemy.getX()).thenReturn(5.0f);
        when(enemy.getY()).thenReturn(5.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(enemy, 'R');
    }

    @Test
    void testEnemyCollision_RightSide_Below() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        PhysicalObject enemy = mock(PhysicalObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(enemy);

        when(mainObject.getX()).thenReturn(6.0f);
        when(mainObject.getY()).thenReturn(4.0f);
        when(enemy.getX()).thenReturn(5.0f);
        when(enemy.getY()).thenReturn(5.0f);
        when(mainObject.collidesWithPhysical(enemy, Math.abs(enemy.getVx()), -0.2f)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, never()).handleCollision(enemy, 'R');
    }

    @Test
    void testObjectNotEqualMainObject_True() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        GameObject object = mock(GameObject.class);
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(object);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(1.0f);
        when(mainObject.collidesWith(object)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, times(1)).handleCollision(object, 'R');
    }

    @Test
    void testObjectNotEqualMainObject_False() {
        PhysicalObject mainObject = mock(PhysicalObject.class);
        GameObject object = mainObject;
        Camera camera = mock(Camera.class);
        List<GameObject> objects = new ArrayList<>();
        objects.add(object);

        when(mainObject.getX()).thenReturn(5.0f);
        when(mainObject.getVx()).thenReturn(1.0f);

        controllerLevel.CheckPhysicalCollisionsX(mainObject, objects, camera);

        verify(mainObject, never()).handleCollision(any(), anyChar());
    }

    @Test
    void testStep_CallsControllerFireBallStep() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        ControllerFireBall mockControllerFireBall = mock(ControllerFireBall.class);
        controllerLevel = spy(new ControllerLevel());
        doReturn(mockControllerFireBall).when(controllerLevel).getControllerFireBall();

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockControllerFireBall, times(1)).step(mockLevel, mockAction, mockTime);
    }

    @Test
    void testStep_CallsControllerMarioStep() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        ControllerMario mockControllerMario = mock(ControllerMario.class);
        controllerLevel = spy(new ControllerLevel());
        doReturn(mockControllerMario).when(controllerLevel).getControllerMario();

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockControllerMario, times(1)).step(mockLevel, mockAction, mockTime);
    }

    @Test
    void testStep_CallsControllerEnemyStep() {
        long mockTime = 100L;

        ControllerEnemy mockControllerEnemy = mock(ControllerEnemy.class);
        controllerLevel = spy(new ControllerLevel());
        doReturn(mockControllerEnemy).when(controllerLevel).getControllerEnemy();

        controllerLevel.step(mockLevel, GeneralGui.ACTION.RIGHT, mockTime);

        verify(mockControllerEnemy, times(1)).step(mockLevel, mockTime);
    }

    @Test
    void testStep_CallsControllerSurprisesStep() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        ControllerSurprises mockControllerSurprises = mock(ControllerSurprises.class);
        controllerLevel = spy(new ControllerLevel());
        doReturn(mockControllerSurprises).when(controllerLevel).getControllerSurprises();

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockControllerSurprises, times(1)).step(mockLevel, mockAction, mockTime);
    }

    @Test
    void testStep_CallsControllerBlocksStep() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        ControllerBlocks mockControllerBlocks = mock(ControllerBlocks.class);
        controllerLevel = spy(new ControllerLevel());
        doReturn(mockControllerBlocks).when(controllerLevel).getControllerBlocks();

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(mockControllerBlocks, times(1)).step(mockLevel, mockAction, mockTime);
    }

    @Test
    void testStep_CallsCheckCollisions() {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.RIGHT;
        long mockTime = 100L;

        controllerLevel = spy(new ControllerLevel());

        controllerLevel.step(mockLevel, mockAction, mockTime);

        verify(controllerLevel, times(1)).checkCollisions(mockMario, mockObjects, mockCamera);
    }

    @Test
    void testCheckCollisions_CallsCheckPhysicalCollisionsXForMario() {
        controllerLevel = spy(new ControllerLevel());

        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        verify(controllerLevel, times(1)).CheckPhysicalCollisionsX(mockMario, mockObjects, mockCamera);
    }

    @Test
    void testCheckCollisions_CallsCheckPhysicalCollisionsYForMario() {
        controllerLevel = spy(new ControllerLevel());

        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        verify(controllerLevel, times(1)).CheckPhysicalCollisionsY(mockMario, mockObjects);
    }

    @Test
    void testCheckCollisions_CallsCheckPhysicalCollisionsXForPhysicalObjects() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        mockObjects.add(mockPhysicalObject);
        controllerLevel = spy(new ControllerLevel());

        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        verify(controllerLevel, times(1)).CheckPhysicalCollisionsX(mockPhysicalObject, mockObjects, mockCamera);
    }

    @Test
    void testCheckCollisions_CallsCheckPhysicalCollisionsYForPhysicalObjects() {
        PhysicalObject mockPhysicalObject = mock(PhysicalObject.class);
        mockObjects.add(mockPhysicalObject);
        controllerLevel = spy(new ControllerLevel());

        controllerLevel.checkCollisions(mockMario, mockObjects, mockCamera);

        verify(controllerLevel, times(1)).CheckPhysicalCollisionsY(mockPhysicalObject, mockObjects);
    }

    @Test
    void testCheckPhysicalCollisionsY_BlockBelow() {
        PhysicalObject physicalObject = mock(PhysicalObject.class);
        BuildingBlock block = mock(BuildingBlock.class);

        when(physicalObject.getX()).thenReturn(5.0f);
        when(physicalObject.getWidth()).thenReturn(2.0f);
        when(physicalObject.getY()).thenReturn(3.0f);
        when(block.getX()).thenReturn(4.0f);
        when(block.getWidth()).thenReturn(3.0f);
        when(block.getY()).thenReturn(4.0f);
        when(block.getHeight()).thenReturn(1.0f);

        List<GameObject> objects = new ArrayList<>();
        objects.add(block);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject, never()).setFalling(true);
    }

    @Test
    void testCheckPhysicalCollisionsY_NoBlockBelow() {
        PhysicalObject physicalObject = mock(PhysicalObject.class);
        BuildingBlock block = mock(BuildingBlock.class);

        when(physicalObject.getX()).thenReturn(5.0f);
        when(physicalObject.getWidth()).thenReturn(2.0f);
        when(physicalObject.getY()).thenReturn(3.0f);
        when(block.getX()).thenReturn(8.0f);
        when(block.getWidth()).thenReturn(3.0f);
        when(block.getY()).thenReturn(4.0f);
        when(block.getHeight()).thenReturn(1.0f);

        List<GameObject> objects = new ArrayList<>();
        objects.add(block);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject, times(1)).setFalling(true);
    }

    @Test
    void testCheckPhysicalCollisionsY_JumpingObject() {
        PhysicalObject physicalObject = mock(PhysicalObject.class);
        GameObject object = mock(GameObject.class);

        when(physicalObject.getVy()).thenReturn(1.0f);
        when(physicalObject.isJumping()).thenReturn(true);
        when(physicalObject.getY()).thenReturn(10.0f);
        when(physicalObject.collidesWith(object)).thenReturn(true);

        List<GameObject> objects = new ArrayList<>();
        objects.add(object);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject, atLeastOnce()).collidesWith(object);
        verify(physicalObject, atLeastOnce()).handleCollision(object, 'U');
    }
}
