package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerEnemyTest {

    private ControllerEnemy controllerEnemy;
    private Level level;
    private Camera camera;
    private List<Enemy> enemies;

    @BeforeEach
    void setup() {
        controllerEnemy = new ControllerEnemy();
        level = mock(Level.class);
        camera = mock(Camera.class);
        enemies = new ArrayList<>();
    }

    @Test
    void test_AddEnemies() {
        when(level.getEnemies()).thenReturn(enemies);
        when(level.getCamera()).thenReturn(camera);

        Enemy enemy = mock(Enemy.class);
        enemies.add(enemy);

        controllerEnemy.step(level, 1000L);

        verify(level, atLeast(2)).getEnemies();
        verify(level, times(1)).getCamera();
    }

    @Test
    void testUpdateStatus_RemovesDeadEnemies() {
        Enemy aliveEnemy = mock(Enemy.class);
        when(aliveEnemy.getLives()).thenReturn(1);
        when(aliveEnemy.wasRevealed()).thenReturn(true);

        Enemy deadEnemy = mock(Enemy.class);
        when(deadEnemy.getLives()).thenReturn(0);

        enemies.add(aliveEnemy);
        enemies.add(deadEnemy);

        when(level.getEnemies()).thenReturn(enemies);

        controllerEnemy.updateStatus(level, 1000L);

        verify(aliveEnemy, times(1)).updateLocation();
        verify(deadEnemy, never()).updateLocation();
        assert enemies.size() == 1;
    }

    @Test
    void test_MoveEnemiesLeft() {
        Enemy revealedEnemy = mock(Enemy.class);
        when(revealedEnemy.wasRevealed()).thenReturn(true);
        when(revealedEnemy.isFalling()).thenReturn(false);
        when(revealedEnemy.getVx()).thenReturn((float) -1);

        Enemy hiddenEnemy = mock(Enemy.class);
        when(hiddenEnemy.wasRevealed()).thenReturn(false);

        enemies.add(revealedEnemy);
        enemies.add(hiddenEnemy);

        controllerEnemy.moveEnemies(enemies, camera);

        verify(camera, times(1)).isEnemyOnCam(revealedEnemy);
        verify(camera, times(1)).isEnemyOnCam(hiddenEnemy);
        verify(revealedEnemy, times(1)).moveLeft();
        verify(revealedEnemy, never()).moveRight();
        verify(hiddenEnemy, never()).moveLeft();
        verify(hiddenEnemy, never()).moveRight();
    }

    @Test
    void test_MoveEnemiesRight() {
        Enemy revealedEnemy = mock(Enemy.class);
        when(revealedEnemy.wasRevealed()).thenReturn(true);
        when(revealedEnemy.isFalling()).thenReturn(false);
        when(revealedEnemy.getVx()).thenReturn((float) 1);

        enemies.add(revealedEnemy);

        controllerEnemy.moveEnemies(enemies, camera);

        verify(camera, times(1)).isEnemyOnCam(revealedEnemy);
        verify(revealedEnemy, never()).moveLeft();
        verify(revealedEnemy, times(1)).moveRight();
    }
}
