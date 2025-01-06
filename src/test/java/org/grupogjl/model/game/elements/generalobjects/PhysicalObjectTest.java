package org.grupogjl.model.game.elements.generalobjects;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PhysicalObjectTest {
    private TestPhysicalObject physicalObject;

    @Mock
    private StaticObject mockStaticObject;

    @Mock
    private PhysicalObject mockPhysicalObject;

    // Concrete implementation for testing
    private static class TestPhysicalObject extends PhysicalObject {
        public TestPhysicalObject(float x, float y, float width, float height) {
            super(x, y, width, height);
        }

        @Override
        public void handleCollision(GameObject object, char r) {
            // Test implementation
        }

        @Override
        public void handleWallColision(float leftCamLimit) {
            // Test implementation
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
        physicalObject = new TestPhysicalObject(100f, 100f, 20f, 20f);

        when(mockStaticObject.getX()).thenReturn(200f);
        when(mockStaticObject.getY()).thenReturn(200f);
        when(mockStaticObject.getWidth()).thenReturn(50f);
        when(mockStaticObject.getHeight()).thenReturn(50f);

        when(mockPhysicalObject.getX()).thenReturn(200f);
        when(mockPhysicalObject.getY()).thenReturn(200f);
        when(mockPhysicalObject.getWidth()).thenReturn(50f);
        when(mockPhysicalObject.getHeight()).thenReturn(50f);
    }

    @Test
    void testInitial_State() {
        assertEquals(100f, physicalObject.getX(), "Initial X position should be 100");
        assertEquals(100f, physicalObject.getY(), "Initial Y position should be 100");
        assertEquals(20f, physicalObject.getWidth(), "Width should be 20");
        assertEquals(20f, physicalObject.getHeight(), "Height should be 20");
        assertEquals(0f, physicalObject.getVx(), "Initial X velocity should be 0");
        assertEquals(0f, physicalObject.getVy(), "Initial Y velocity should be 0");
        assertEquals(0.23F, physicalObject.getG(), "Gravity should be 0.23F");
        assertFalse(physicalObject.isJumping(), "Should not be jumping initially");
        assertFalse(physicalObject.isFalling(), "Should not be falling initially");
    }

    @Test
    void testUpdate_LocationWhileJumping() {
        physicalObject.setJumping(true);
        physicalObject.setVy(1.0f);
        float initialY = physicalObject.getY();

        physicalObject.updateLocation();

        assertTrue(physicalObject.getVy() < 1.0f, "Velocity should decrease due to gravity");
        assertNotEquals(initialY, physicalObject.getY(), "Y position should change");
    }

    @Test
    void testUpdate_LocationWhileFalling() {
        physicalObject.setFalling(true);
        physicalObject.setVy(1.0f);
        float initialY = physicalObject.getY();

        physicalObject.updateLocation();

        assertTrue(physicalObject.getVy() <= 1.4f, "Velocity should be capped at 1.4");
        assertTrue(physicalObject.getY() > initialY, "Should fall downward");
    }

    @Test
    void testJump_ToFallTransition() {
        physicalObject.setJumping(true);
        physicalObject.setVy(-0.1f);

        physicalObject.updateLocation();

        assertFalse(physicalObject.isJumping(), "Should stop jumping");
        assertTrue(physicalObject.isFalling(), "Should start falling");
    }

    @Test
    void testCollides_WithStatic() {
        when(mockStaticObject.getX()).thenReturn(110f);
        when(mockStaticObject.getY()).thenReturn(110f);

        assertTrue(physicalObject.collidesWithStatic(mockStaticObject),
                "Should detect collision when objects overlap");

        when(mockStaticObject.getX()).thenReturn(300f);
        when(mockStaticObject.getY()).thenReturn(300f);

        assertFalse(physicalObject.collidesWithStatic(mockStaticObject),
                "Should not detect collision when objects don't overlap");
    }

    @Test
    void testCollidesWithPhysical() {
        when(mockPhysicalObject.getX()).thenReturn(110f);
        when(mockPhysicalObject.getY()).thenReturn(110f);

        assertTrue(physicalObject.collidesWithPhysical(mockPhysicalObject, 0f, 0f),
                "Should detect collision when objects overlap");

        when(mockPhysicalObject.getX()).thenReturn(300f);
        when(mockPhysicalObject.getY()).thenReturn(300f);

        assertFalse(physicalObject.collidesWithPhysical(mockPhysicalObject, 0f, 0f),
                "Should not detect collision when objects don't overlap");
    }

    @Test
    void testCollidesWith() {
        GameObject staticGameObject = mockStaticObject;
        GameObject physicalGameObject = mockPhysicalObject;

        when(mockStaticObject.getX()).thenReturn(110f);
        when(mockStaticObject.getY()).thenReturn(110f);
        assertTrue(physicalObject.collidesWith(staticGameObject),
                "Should detect collision with static object");

        when(mockPhysicalObject.getX()).thenReturn(110f);
        when(mockPhysicalObject.getY()).thenReturn(110f);
        assertTrue(physicalObject.collidesWith(physicalGameObject),
                "Should detect collision with physical object");
    }

    @Test
    void testVelocity_Limits() {
        physicalObject.setFalling(true);
        physicalObject.setVy(2.0f);

        physicalObject.updateLocation();

        assertEquals(1.4f, physicalObject.getVy(),
                "Vertical velocity should be capped at 1.4f");
    }

    @Test
    void testPosition_Setters() {
        physicalObject.setX(50f);
        physicalObject.setY(60f);
        physicalObject.setWidth(30f);
        physicalObject.setHeight(40f);

        assertEquals(50f, physicalObject.getX(), "X position should be updated");
        assertEquals(60f, physicalObject.getY(), "Y position should be updated");
        assertEquals(30f, physicalObject.getWidth(), "Width should be updated");
        assertEquals(40f, physicalObject.getHeight(), "Height should be updated");
    }

    @Test
    void testGravity_Modification() {
        float newGravity = 0.5f;
        physicalObject.setG(newGravity);
        assertEquals(newGravity, physicalObject.getG(),
                "Gravity value should be modifiable");
    }

    @Test
    void testCollidesWith_StaticObject() {
        GameObject staticGameObject = mockStaticObject;

        when(mockStaticObject.getX()).thenReturn(110f);
        when(mockStaticObject.getY()).thenReturn(110f);
        assertTrue(physicalObject.collidesWith(staticGameObject),
                "Should detect collision with static object");

        when(mockStaticObject.getX()).thenReturn(300f);
        when(mockStaticObject.getY()).thenReturn(300f);
        assertFalse(physicalObject.collidesWith(staticGameObject),
                "Should not detect collision with static object");
    }

    @Test
    void testCollidesWith_PhysicalObject() {
        GameObject physicalGameObject = mockPhysicalObject;

        when(mockPhysicalObject.getX()).thenReturn(110f);
        when(mockPhysicalObject.getY()).thenReturn(110f);
        assertTrue(physicalObject.collidesWith(physicalGameObject),
                "Should detect collision with physical object");

        when(mockPhysicalObject.getX()).thenReturn(300f);
        when(mockPhysicalObject.getY()).thenReturn(300f);
        assertFalse(physicalObject.collidesWith(physicalGameObject),
                "Should not detect collision with physical object");
    }

    @Test
    void testCollidesWithPhysical_BoundaryConditions() {
        when(mockPhysicalObject.getX()).thenReturn(120f);
        when(mockPhysicalObject.getY()).thenReturn(120f);
        assertFalse(physicalObject.collidesWithPhysical(mockPhysicalObject, 0f, 0f),
                "Should not detect collision at the boundary");

        when(mockPhysicalObject.getX()).thenReturn(121f);
        when(mockPhysicalObject.getY()).thenReturn(121f);
        assertFalse(physicalObject.collidesWithPhysical(mockPhysicalObject, 0f, 0f),
                "Should not detect collision just outside the boundary");

        assertFalse(physicalObject.collidesWithPhysical(mockPhysicalObject, 1f, 1f),
                "Should not detect collision with offset");

        assertTrue(physicalObject.collidesWithPhysical(mockPhysicalObject, 2f, 2f),
                "Should detect collision with offset just outside the boundary");
    }
}