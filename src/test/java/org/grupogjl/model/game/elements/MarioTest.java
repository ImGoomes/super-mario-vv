package org.grupogjl.model.game.elements;

import org.grupogjl.model.game.elements.blocks.GoalBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarioTest {

    private Mario mario;
    private StateGame mockObserver;
    private Camera mockCamera;

    @BeforeEach
    void setUp() {
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
    void testJump() {
        mario.jump();
        assertTrue(mario.isJumping(), "Mario should start jumping");
        assertEquals(1.3f, mario.getVy(), "Mario's vertical velocity should be set for jumping");
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
    void testHandle_CollisionWithEnemyJumping() {
        Enemy mockEnemy = mock(Enemy.class);
        when(mockEnemy.getLives()).thenReturn(1);

        mario.handleCollision(mockEnemy, 'D');
        assertTrue(mario.isJumping(), "Mario should jump after stomping an enemy");
        verify(mockEnemy, times(1)).setLives(0);
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
    void testGet_Image() {
        mario.setStateBig(true);
        String image = mario.getImage();
        assertTrue(image.equals("marioSuper.png") || image.equals("marioStarBig.png"), "Image should correspond to big Mario state");

        mario.setStateFire(true);
        image = mario.getImage();
        assertTrue(image.equals("marioSuper.png") || image.equals("marioStarBig.png"), "Image should correspond to fire Mario state");

        mario.setStateBig(false);
        mario.setStateFire(false);
        mario.setStateInvencible(true);
        image = mario.getImage();
        assertTrue(image.equals("marioStar.png")  || image.equals("mario.png") || image.equals("hitMario.png"), "Image should correspond to invincible small Mario state");
    }
}