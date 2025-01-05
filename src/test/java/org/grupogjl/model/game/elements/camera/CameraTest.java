package org.grupogjl.model.game.elements.camera;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    private Camera camera;
    private Mario mario;
    private Enemy enemy;

    @BeforeEach
    void setUp() {
        camera = new Camera();
        mario = mock(Mario.class);
        enemy = mock(Enemy.class);
    }

    @Test
    void testCamera_Initialization() {
        assertEquals(0, camera.getLeftCamLimit());
        assertEquals(26, camera.getRightCamLimit());
    }

    @Test
    void testUpdateCamera_MarioInsideBounds() {
        when(mario.getX()).thenReturn(10.0f);

        camera.updateCamera(mario);

        assertEquals(0, camera.getLeftCamLimit());
        assertEquals(26, camera.getRightCamLimit());
    }

    @Test
    void testUpdateCamera_MarioMovesBeyondBounds() {
        when(mario.getX()).thenReturn(20.0f);

        camera.updateCamera(mario);

        assertEquals(7, camera.getLeftCamLimit());
        assertEquals(33, camera.getRightCamLimit());
    }

    @Test
    void testIsEnemyOnCam_EnemyNotOnCamera() {
        when(enemy.getX()).thenReturn(40.0f);
        when(enemy.getWidth()).thenReturn(5.0f);
        when(enemy.wasRevealed()).thenReturn(false);

        camera.isEnemyOnCam(enemy);

        verify(enemy, never()).setRevealed(true);
    }

    @Test
    void testIsEnemyOnCam_EnemyOnCamera() {
        when(enemy.getX()).thenReturn(10.0f);
        when(enemy.getWidth()).thenReturn(5.0f);
        when(enemy.wasRevealed()).thenReturn(false);

        camera.isEnemyOnCam(enemy);

        verify(enemy, times(1)).setRevealed(true);
    }

    @Test
    void testIsEnemyOnCam_EnemyAlreadyRevealed() {
        when(enemy.getX()).thenReturn(10.0f);
        when(enemy.getWidth()).thenReturn(5.0f);
        when(enemy.wasRevealed()).thenReturn(true);

        camera.isEnemyOnCam(enemy);

        verify(enemy, never()).setRevealed(true);
    }

    @Test
    void testIsEnemyOnCam_EnemyNotOnCamera_LeftOfBounds() {
        when(enemy.getX()).thenReturn(-5.0f);
        when(enemy.getWidth()).thenReturn(5.0f);
        when(enemy.wasRevealed()).thenReturn(false);

        camera.isEnemyOnCam(enemy);

        verify(enemy, never()).setRevealed(true);
    }

    @Test
    void testIsEnemyOnCam_EnemyNotOnCamera_RightOfBounds() {
        when(enemy.getX()).thenReturn(30.0f);
        when(enemy.getWidth()).thenReturn(5.0f);
        when(enemy.wasRevealed()).thenReturn(false);

        camera.isEnemyOnCam(enemy);

        verify(enemy, never()).setRevealed(true);
    }

    @Test
    void testIsEnemyOnCam_EnemyPartiallyOnCamera() {
        when(enemy.getX()).thenReturn(25.0f);
        when(enemy.getWidth()).thenReturn(10.0f);
        when(enemy.wasRevealed()).thenReturn(false);

        camera.isEnemyOnCam(enemy);

        verify(enemy, never()).setRevealed(true);
    }
}
