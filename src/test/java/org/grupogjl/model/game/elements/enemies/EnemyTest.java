package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    private TestEnemy enemy;

    @Mock
    private StaticObject mockStaticObject;

    @Mock
    private Enemy mockEnemyObject;

    // Concrete implementation for testing
    private static class TestEnemy extends Enemy {
        public TestEnemy(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public float getVirtX(Camera camera) {
            return 0;
        }

        @Override
        public float getVirtY() {
            return 0;
        }

        @Override
        public String getImage() {
            return "";
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enemy = new TestEnemy(100f, 100f, 20f, 20f);

        when(mockStaticObject.getX()).thenReturn(200f);
        when(mockStaticObject.getY()).thenReturn(200f);
        when(mockStaticObject.getWidth()).thenReturn(50f);
        when(mockStaticObject.getHeight()).thenReturn(50f);
    }

    @Test
    void testInitial_State() {
        assertEquals(100f, enemy.getX(), "Initial X position should be 100");
        assertEquals(100f, enemy.getY(), "Initial Y position should be 100");
        assertEquals(20f, enemy.getWidth(), "Width should be 20");
        assertEquals(20f, enemy.getHeight(), "Height should be 20");
        assertEquals(1, enemy.getLives(), "Initial lives should be 1");
        assertFalse(enemy.wasRevealed(), "Enemy should not be revealed initially");
    }

    @Test
    void testSet_Revealed() {
        assertFalse(enemy.wasRevealed(), "Initially not revealed");

        enemy.setRevealed(true);
        assertTrue(enemy.wasRevealed(), "Should be revealed after setting to true");

        enemy.setRevealed(false);
        assertFalse(enemy.wasRevealed(), "Should not be revealed after setting to false");
    }

    @Test
    void testHandleWallCollision() {
        enemy.setVx(2.0f);
        float leftCamLimit = 50f;

        enemy.handleWallColision(leftCamLimit);

        assertEquals(leftCamLimit, enemy.getX(), "X position should be set to left camera limit");
        assertEquals(-2.0f, enemy.getVx(), "Velocity should be reversed");
    }

    @Test
    void testMovement() {
        enemy.moveLeft();
        assertEquals(-0.2f, enemy.getVx(), "Should move left with velocity -0.2");

        enemy.moveRight();
        assertEquals(0.2f, enemy.getVx(), "Should move right with velocity 0.2");
    }

    @Test
    void testLives() {
        assertEquals(1, enemy.getLives(), "Initial lives should be 1");

        enemy.setLives(2);
        assertEquals(2, enemy.getLives(), "Lives should be updated to 2");

        enemy.setLives(0);
        assertEquals(0, enemy.getLives(), "Lives should be updated to 0");
    }

    @Test
    void testHandle_CollisionWithEnemy() {
        enemy.handleCollision(mockEnemyObject, 'R');
        assertEquals(-0.2f, enemy.getVx(), "Should reverse direction on right collision");

        enemy.handleCollision(mockEnemyObject, 'L');
        assertEquals(0.2f, enemy.getVx(), "Should reverse direction on left collision");
    }

    @Test
    void testHandle_CollisionWithStaticObject() {
        enemy.handleCollision(mockStaticObject, 'U');
        assertEquals(mockStaticObject.getY() + enemy.getHeight(), enemy.getY());
        assertEquals(0f, enemy.getVy());

        enemy.handleCollision(mockStaticObject, 'D');
        assertEquals(mockStaticObject.getY() - mockStaticObject.getHeight(), enemy.getY());
        assertEquals(0f, enemy.getVy());
        assertFalse(enemy.isFalling());
        assertFalse(enemy.isJumping());

        enemy.setVx(0.2f);
        enemy.handleCollision(mockStaticObject, 'L');
        assertEquals(mockStaticObject.getX() + mockStaticObject.getWidth(), enemy.getX());
        assertEquals(-0.2f, enemy.getVx());

        enemy.setVx(0.2f);
        enemy.handleCollision(mockStaticObject, 'R');
        assertEquals(mockStaticObject.getX() - enemy.getWidth(), enemy.getX());
        assertEquals(-0.2f, enemy.getVx());
    }

    @Test
    void testHandle_CollisionWithUnknownObject() {
        GameObject unknownObject = mock(GameObject.class);
        float initialX = enemy.getX();
        float initialY = enemy.getY();
        float initialVx = enemy.getVx();
        float initialVy = enemy.getVy();

        enemy.handleCollision(unknownObject, 'R');

        assertEquals(initialX, enemy.getX(), "Position should not change");
        assertEquals(initialY, enemy.getY(), "Position should not change");
        assertEquals(initialVx, enemy.getVx(), "Velocity should not change");
        assertEquals(initialVy, enemy.getVy(), "Velocity should not change");
    }

    @Test
    void testCollision_Interactions() {
        enemy.setVx(0.2f);
        enemy.setFalling(true);
        enemy.setJumping(true);

        enemy.handleCollision(mockStaticObject, 'D');

        assertAll(
                () -> assertFalse(enemy.isFalling(), "Should not be falling after collision"),
                () -> assertFalse(enemy.isJumping(), "Should not be jumping after collision"),
                () -> assertEquals(0f, enemy.getVy(), "Vertical velocity should be 0")
        );
    }
}