package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MushroomTest {
    private TestMushroom mushroom;
    private StaticObject mockStaticObject;

    // Concrete implementation for testing
    private static class TestMushroom extends Mushroom {
        public TestMushroom(float x, float y) {
            super(x, y);
        }

        @Override
        public void activate(Mario mario) {

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
        mushroom = new TestMushroom(100f, 100f);
        mockStaticObject = mock(StaticObject.class);
    }

    @Test
    void testBorn() {
        mushroom.born();
        assertEquals(0.2f, mushroom.getVx(), "Horizontal velocity should be 0.2f after born");
        assertTrue(mushroom.isFalling(), "Mushroom should be falling after born");
    }

    @Test
    void testHandle_CollisionUp() {
        mushroom.handleCollision(mockStaticObject, 'U');
        assertEquals(mockStaticObject.getY() + mushroom.getHeight(), mushroom.getY(),
                "Mushroom should be positioned above the static object");
        assertEquals(0f, mushroom.getVy(), "Vertical velocity should be 0 after upward collision");
    }

    @Test
    void testHandle_CollisionDown() {
        mushroom.handleCollision(mockStaticObject, 'D');
        assertEquals(mockStaticObject.getY() - mockStaticObject.getHeight(), mushroom.getY(),
                "Mushroom should be positioned below the static object");
        assertEquals(0f, mushroom.getVy(), "Vertical velocity should be 0 after downward collision");
        assertFalse(mushroom.isFalling(), "Mushroom should not be falling after downward collision");
        assertFalse(mushroom.isJumping(), "Mushroom should not be jumping after downward collision");
    }

    @Test
    void testHandle_CollisionLeft() {
        mushroom.handleCollision(mockStaticObject, 'L');
        assertEquals(mockStaticObject.getX() + mockStaticObject.getWidth(), mushroom.getX(),
                "Mushroom should be positioned to the right of the static object");
        assertEquals(0.2f, mushroom.getVx(), "Horizontal velocity should be 0.2f after left collision");
    }

    @Test
    void testHandle_CollisionRight() {
        mushroom.handleCollision(mockStaticObject, 'R');
        assertEquals(mockStaticObject.getX() - mushroom.getWidth(), mushroom.getX(),
                "Mushroom should be positioned to the left of the static object");
        assertEquals(-0.2f, mushroom.getVx(), "Horizontal velocity should be -0.2f after right collision");
    }

    @Test
    void testHandle_CollisionWithNonStaticObject() {
        GameObject nonStaticObject = mock(GameObject.class);
        float initialX = mushroom.getX();
        float initialY = mushroom.getY();
        float initialVx = mushroom.getVx();
        float initialVy = mushroom.getVy();

        mushroom.handleCollision(nonStaticObject, 'R');

        assertEquals(initialX, mushroom.getX(), "X position should not change when colliding with non-static object");
        assertEquals(initialY, mushroom.getY(), "Y position should not change when colliding with non-static object");
        assertEquals(initialVx, mushroom.getVx(), "Horizontal velocity should not change when colliding with non-static object");
        assertEquals(initialVy, mushroom.getVy(), "Vertical velocity should not change when colliding with non-static object");
    }


    @Test
    void testHandle_UnsupportedCollisionDirection() {
        float initialX = mushroom.getX();
        float initialY = mushroom.getY();
        float initialVx = mushroom.getVx();
        float initialVy = mushroom.getVy();

        mushroom.handleCollision(mockStaticObject, 'Z');

        assertEquals(initialX, mushroom.getX(), "X position should not change for unsupported direction");
        assertEquals(initialY, mushroom.getY(), "Y position should not change for unsupported direction");
        assertEquals(initialVx, mushroom.getVx(), "Horizontal velocity should not change for unsupported direction");
        assertEquals(initialVy, mushroom.getVy(), "Vertical velocity should not change for unsupported direction");
    }

    @Test
    void testHandleCollision_AllCases() {
        testHandle_CollisionUp();
        testHandle_CollisionDown();
        testHandle_CollisionLeft();
        testHandle_CollisionRight();
        testHandle_CollisionWithNonStaticObject();
        testHandle_UnsupportedCollisionDirection();
    }
}