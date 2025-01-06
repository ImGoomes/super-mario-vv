package org.grupogjl.model.game.elements;

import org.grupogjl.model.game.elements.blocks.BuildingBlock;
import org.grupogjl.model.game.elements.blocks.GoalBlock;
import org.grupogjl.model.game.elements.blocks.InteractableBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarioTest {

    private Mario mario;
    private StateGame mockObserver;
    private Camera mockCamera;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = mock(Random.class);
        mario = new Mario(10.0f, 20.0f, 1.0f, 1.0f);
        mockObserver = mock(StateGame.class);
        mockCamera = mock(Camera.class);
        mario.setObserver(mockObserver);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, mario.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, mario.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(1.0f, mario.getWidth(), "Width should be initialized correctly");
        assertEquals(1.0f, mario.getHeight(), "Height should be initialized correctly");
        assertEquals(3, mario.getLives(), "Mario should start with 3 lives");
        assertEquals(0, mario.getCoins(), "Mario should start with 0 coins");
    }

    @Test
    void testHit_Cooldown() {
        mario.setHitCooldown(true);
        boolean coolDown = mario.isHitCooldown();
        assertEquals(true, coolDown, "Mario should be in hit cooldown");
    }

    @Test
    void testJump() {
        mario.setJumping(false);
        mario.setFalling(false);
        mario.jump();
        assertTrue(mario.isJumping(), "Mario should start jumping");
        assertEquals(1.3f, mario.getVy(), "Mario's vertical velocity should be set for jumping");
    }

    @Test
    void testJump_Jumping() {
        mario.setJumping(true);
        mario.setFalling(false);
        mario.jump();
        assertFalse(mario.isFalling(), "Mario shouldn't start jumping because is jumping");
    }

    @Test
    void testJump_Falling() {
        mario.setJumping(false);
        mario.setFalling(true);
        mario.jump();
        assertFalse(mario.isJumping(), "Mario shouldn't start jumping because is falling");
    }

    @Test
    void testJump_FallingAndFalling() {
        mario.setJumping(true);
        mario.setFalling(true);
        mario.jump();
        assertTrue(mario.isJumping() && mario.isFalling(), "Mario shouldn't start jumping");
    }

    @Test
    void testMove_Left() {
        mario.moveLeft();
        assertEquals(-0.5f, mario.getVx(), "Mario should move left with velocity -0.5");
    }

    @Test
    void testMove_Right() {
        mario.moveRight();
        assertEquals(0.5f, mario.getVx(), "Mario should move right with velocity 0.5");
    }

    @Test
    void testHandle_WallCollision() {
        mario.handleWallColision(5.0f);
        assertEquals(5.0f, mario.getX(), "Mario's X-coordinate should reset to the wall limit");
        assertEquals(0.0f, mario.getVx(), "Mario's horizontal velocity should reset to 0");
    }

    @Test
    void testNotify_StateLives() throws IOException {
        mario.notifyState("lives");
        verify(mockObserver, times(1)).resetLevel();
    }

    @Test
    void testNotify_StateGoal() throws IOException {
        mario.notifyState("goal");
        verify(mockObserver, times(1)).nextLevel();
    }

    @Test
    void testNotify_StateNonExists() throws IOException {
        mario.notifyState("NonExists");
        verify(mockObserver, never()).resetLevel();
        verify(mockObserver, never()).nextLevel();
    }

    @Test
    void testNotify_StateException() throws IOException {
        doThrow(new IOException()).when(mockObserver).resetLevel();
        assertThrows(RuntimeException.class, () -> mario.notifyState("lives"), "Expected RuntimeException to be thrown");

        doThrow(new IOException()).when(mockObserver).nextLevel();
        assertThrows(RuntimeException.class, () -> mario.notifyState("goal"), "Expected RuntimeException to be thrown");
    }

    @Test
    void testHandle_CollisionWithSurprise() {
        Surprise mockSurprise = mock(Surprise.class);
        mario.handleCollision(mockSurprise, 'U');
        verify(mockSurprise, times(1)).activate(mario);
        verify(mockSurprise, times(1)).setActivated(false);
    }

    @Test
    void testHandle_CollisionWithEnemyNotInvincible() {
        Enemy mockEnemy = mock(Enemy.class);
        mario.handleCollision(mockEnemy, 'L');
        assertEquals(2, mario.getLives(), "Mario should lose a life when hit by an enemy");
    }

    @Test
    void testHandle_CollisionWithEnemyInvincibleNotInCooldown() {
        Enemy mockEnemy = mock(Enemy.class);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        mario.handleCollision(mockEnemy, 'L');

        verify(mockEnemy, times(1)).setLives(0);
        assertEquals(0.0f, mario.getVx(), "Mario's horizontal velocity should be set to 0");
    }

    @Test
    void testHandle_CollisionWithEnemyInvincibleInCooldown() {
        Enemy mockEnemy = mock(Enemy.class);
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        mario.handleCollision(mockEnemy, 'L');

        assertEquals(0.0f, mario.getVx());
    }

    @Test
    void testHandle_CollisionWithEnemyWhenBigOrFire() {
        Enemy mockEnemy = mock(Enemy.class);
        mario.setStateBig(true);
        mario.setStateInvencible(false);

        mario.handleCollision(mockEnemy, 'L');

        assertFalse(mario.isStateBig(), "Mario should no longer be big");
        assertFalse(mario.isStateFire(), "Mario should no longer be in fire state");
        assertEquals(0.5f, mario.getHeight(), "Mario's height should be halved");
        assertEquals(15, mario.getInvencibleTime(), "Mario's invincible time should be set to 15");
        assertTrue(mario.isStateInvencible(), "Mario should be invincible");
        assertTrue(mario.isHitCooldown(), "Mario should be in hit cooldown");
    }

    @Test
    void testHandle_CollisionWithEnemyWhenStateFire() {
        Enemy mockEnemy = mock(Enemy.class);
        mario.setStateFire(true);
        mario.setStateInvencible(false);

        mario.handleCollision(mockEnemy, 'L');

        assertFalse(mario.isStateBig(), "Mario should no longer be big");
        assertFalse(mario.isStateFire(), "Mario should no longer be in fire state");
        assertEquals(0.5f, mario.getHeight(), "Mario's height should be halved");
        assertEquals(15, mario.getInvencibleTime(), "Mario's invincible time should be set to 15");
        assertTrue(mario.isStateInvencible(), "Mario should be invincible");
        assertTrue(mario.isHitCooldown(), "Mario should be in hit cooldown");
    }

    @Test
    void testHandle_CollisionWithEnemyJumping() {
        Enemy mockEnemy = mock(Enemy.class);
        when(mockEnemy.getLives()).thenReturn(1);

        mario.handleCollision(mockEnemy, 'D');
        assertTrue(mario.isJumping(), "Mario should jump after stomping an enemy");
        verify(mockEnemy, times(1)).setLives(0);
    }

    @Test
    void testHandle_CollisionWithBuildingBlock_U() {
        BuildingBlock mockBlock = mock(BuildingBlock.class);
        when(mockBlock.getY()).thenReturn(10.0f);
        when(mockBlock.getHeight()).thenReturn(1.0f);
        mario.setHeight(1.0f);

        InteractableBlock mockInteractableBlock = mock(InteractableBlock.class);
        mario.handleCollision(mockInteractableBlock, 'U');

        verify(mockInteractableBlock, times(1)).gotHit(mario);
        assertEquals(0.0f, mario.getVy(), "Mario's vertical velocity should be set to 0");
        assertFalse(mario.isJumping(), "Mario should not be jumping");
        assertTrue(mario.isFalling(), "Mario should be falling");
        assertEquals(1.0f, mario.getY(), "Mario's Y-coordinate should be set correctly");
    }

    @Test
    void testHandle_CollisionWithBuildingBlock_D() {
        BuildingBlock mockBlock = mock(BuildingBlock.class);
        when(mockBlock.getY()).thenReturn(10.0f);
        when(mockBlock.getHeight()).thenReturn(1.0f);

        mario.handleCollision(mockBlock, 'D');

        assertEquals(0.0f, mario.getVy(), "Mario's vertical velocity should be set to 0");
        assertFalse(mario.isFalling(), "Mario should not be falling");
        assertFalse(mario.isJumping(), "Mario should not be jumping");
        assertEquals(9.0f, mario.getY(), "Mario's Y-coordinate should be set correctly");
    }

    @Test
    void testHandle_CollisionWithBuildingBlock_L() {
        BuildingBlock mockBlock = mock(BuildingBlock.class);
        when(mockBlock.getX()).thenReturn(10.0f);
        when(mockBlock.getWidth()).thenReturn(1.0f);

        mario.handleCollision(mockBlock, 'L');

        assertEquals(11.0f, mario.getX(), "Mario's X-coordinate should be set correctly");
        assertEquals(0.0f, mario.getVx(), "Mario's horizontal velocity should be set to 0");
    }

    @Test
    void testHandle_CollisionWithBuildingBlock_R() {
        BuildingBlock mockBlock = mock(BuildingBlock.class);
        when(mockBlock.getX()).thenReturn(10.0f);
        when(mockBlock.getWidth()).thenReturn(1.0f);

        mario.handleCollision(mockBlock, 'R');

        assertEquals(9.0f, mario.getX(), "Mario's X-coordinate should be set correctly");
        assertEquals(0.0f, mario.getVx(), "Mario's horizontal velocity should be set to 0");
    }

    @Test
    void testHandle_CollisionWithGoalBlock() throws IOException {
        GoalBlock mockGoalBlock = mock(GoalBlock.class);
        mario.handleCollision(mockGoalBlock, 'U');
        verify(mockObserver, times(1)).nextLevel();
    }

    @Test
    void testReset() throws IOException {
        mario.setStateBig(true);
        mario.setStateFire(true);

        mario.reset();

        assertEquals(2, mario.getLives(), "Mario should lose a life");
        assertFalse(mario.isStateBig(), "Mario should no longer be big");
        assertFalse(mario.isStateFire(), "Mario should no longer be in fire state");
        assertFalse(mario.isStateInvencible(), "Mario should no longer be invincible");
        verify(mockObserver, times(1)).resetLevel();
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, mario.getVirtX(mockCamera), "getVirtX should calculate the virtual X correctly");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(20.0f, mario.getVirtY(), "getVirtY should calculate the virtual Y correctly");
    }

    @Test
    void testGet_ImageStateBig() {
        mario.setStateBig(true);
        String image = mario.getImage();
        assertTrue(image.equals("marioSuper.png"), "Image should correspond to big Mario state");
    }

    @Test
    void testGet_ImageStateBig_marioStarBig() {
        mario.setStateBig(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        boolean conditionMetStateBig = false;
        for (int i = 0; i < 1000; i++) {
            String image = mario.getImage();
            if ("marioStarBig.png".equals(image)) {
                conditionMetStateBig = true;
                break;
            }
        }

        assertTrue(conditionMetStateBig, "Condition for marioStarBig.png was satisfied");
    }

    @Test
    void testGet_ImageStateFire_marioStarBig() {
        mario.setStateBig(false);
        mario.setStateFire(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        boolean conditionMetStateFire = false;
        for (int i = 0; i < 1000; i++) {
            String image = mario.getImage();
            if ("marioStarBig.png".equals(image)) {
                conditionMetStateFire = true;
                break;
            }
        }

        assertTrue(conditionMetStateFire, "Condition for marioStarBig.png was satisfied");
    }

    @Test
    void testGet_Image_hitMario() {
        mario.setStateBig(false);
        mario.setStateFire(false);
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        boolean conditionMet = false;
        for (int i = 0; i < 1000; i++) {
            String image = mario.getImage();
            if ("hitMario.png".equals(image)) {
                conditionMet = true;
                break;
            }
        }

        assertTrue(conditionMet, "Condition for marioStarBig.png was satisfied");
    }

    @Test
    void testGet_Coins() {
        mario.setCoins(5);
        assertEquals(5, mario.getCoins(), "Mario should return the correct number of coins");
    }
}