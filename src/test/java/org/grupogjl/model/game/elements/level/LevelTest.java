package org.grupogjl.model.game.elements.level;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.DestroyableBlock;
import org.grupogjl.model.game.elements.blocks.GoalBlock;
import org.grupogjl.model.game.elements.blocks.SurpriseBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.props.FireBall;
import org.grupogjl.model.game.elements.surprises.Coin;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelTest {
    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(100, 50);
    }

    @Test
    void testInitialization() {
        assertEquals(100, level.getWidth());
        assertEquals(50, level.getHeight());
        assertTrue(level.getObjects().isEmpty());
    }

    @Test
    void testSetAndGet_Camera() {
        Camera camera = mock(Camera.class);
        level.setCamera(camera);
        assertEquals(camera, level.getCamera());
    }

    @Test
    void testSetAndGet_GoalBlock() {
        GoalBlock goalBlock = mock(GoalBlock.class);
        level.setGoalBlock(goalBlock);
        assertEquals(goalBlock, level.getGoalBlock());
    }

    @Test
    void testSetAndGet_Mario() {
        Mario mario = mock(Mario.class);
        level.setMario(mario);
        assertEquals(mario, level.getMario());
    }

    @Test
    void testSetAndGet_Objects() {
        List<GameObject> objects = new ArrayList<>();
        objects.add(mock(GameObject.class));
        level.setObjects(objects);
        assertEquals(objects, level.getObjects());
    }

    @Test
    void testGet_DestroyableBlocks() {
        DestroyableBlock block1 = mock(DestroyableBlock.class);
        DestroyableBlock block2 = mock(DestroyableBlock.class);
        level.getObjects().add(block1);
        level.getObjects().add(block2);
        level.getObjects().add(mock(GameObject.class));

        List<DestroyableBlock> destroyableBlocks = level.getDestroyableBlocks();

        assertEquals(2, destroyableBlocks.size());
        assertTrue(destroyableBlocks.contains(block1));
        assertTrue(destroyableBlocks.contains(block2));
    }

    @Test
    void testSet_DestroyableBlocks() {
        DestroyableBlock block1 = mock(DestroyableBlock.class);
        DestroyableBlock block2 = mock(DestroyableBlock.class);
        List<DestroyableBlock> newBlocks = List.of(block1, block2);

        level.getObjects().add(mock(DestroyableBlock.class));
        level.setDestroyableBlocks(newBlocks);

        List<DestroyableBlock> destroyableBlocks = level.getDestroyableBlocks();
        assertEquals(2, destroyableBlocks.size());
        assertTrue(destroyableBlocks.contains(block1));
        assertTrue(destroyableBlocks.contains(block2));
    }

    @Test
    void testGet_Enemies() {
        Enemy enemy1 = mock(Enemy.class);
        Enemy enemy2 = mock(Enemy.class);
        level.getObjects().add(enemy1);
        level.getObjects().add(enemy2);
        level.getObjects().add(mock(GameObject.class));

        List<Enemy> enemies = level.getEnemies();

        assertEquals(2, enemies.size());
        assertTrue(enemies.contains(enemy1));
        assertTrue(enemies.contains(enemy2));
    }

    @Test
    void testSet_Enemies() {
        Enemy enemy1 = mock(Enemy.class);
        Enemy enemy2 = mock(Enemy.class);
        List<Enemy> newEnemies = List.of(enemy1, enemy2);

        level.getObjects().add(mock(Enemy.class));
        level.setEnemies(newEnemies);

        List<Enemy> enemies = level.getEnemies();
        assertEquals(2, enemies.size());
        assertTrue(enemies.contains(enemy1));
        assertTrue(enemies.contains(enemy2));
    }

    @Test
    void testGetSurprises() {
        Surprise surprise1 = mock(Surprise.class);
        Surprise surprise2 = mock(Surprise.class);
        level.getObjects().add(surprise1);
        level.getObjects().add(surprise2);
        level.getObjects().add(mock(GameObject.class));

        List<Surprise> surprises = level.getSurprises();

        assertEquals(2, surprises.size());
        assertTrue(surprises.contains(surprise1));
        assertTrue(surprises.contains(surprise2));
    }

    @Test
    void testSet_Surprises() {
        Surprise surprise1 = mock(Surprise.class);
        Surprise surprise2 = mock(Surprise.class);
        List<Surprise> newSurprises = List.of(surprise1, surprise2);

        level.getObjects().add(mock(Surprise.class));
        level.setSurprises(newSurprises);

        List<Surprise> surprises = level.getSurprises();
        assertEquals(2, surprises.size());
        assertTrue(surprises.contains(surprise1));
        assertTrue(surprises.contains(surprise2));
    }

    @Test
    void testGet_Coins() {
        Coin coin1 = mock(Coin.class);
        Coin coin2 = mock(Coin.class);
        level.getObjects().add(coin1);
        level.getObjects().add(coin2);
        level.getObjects().add(mock(GameObject.class));

        List<Coin> coins = level.getCoins();

        assertEquals(2, coins.size());
        assertTrue(coins.contains(coin1));
        assertTrue(coins.contains(coin2));
    }

    @Test
    void testSetAndGet_FireBalls() {
        FireBall fireBall1 = mock(FireBall.class);
        FireBall fireBall2 = mock(FireBall.class);
        List<FireBall> fireBalls = List.of(fireBall1, fireBall2);

        level.setFireBalls(fireBalls);

        List<FireBall> retrievedFireBalls = level.getFireBalls();
        assertEquals(2, retrievedFireBalls.size());
        assertTrue(retrievedFireBalls.contains(fireBall1));
        assertTrue(retrievedFireBalls.contains(fireBall2));
    }
}
