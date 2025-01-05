package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SurpriseTest {
    private TestSurprise surprise;
    private Mario mario;

    // Concrete implementation for testing
    private static class TestSurprise extends Surprise {
        private boolean bornCalled = false;
        private boolean activateCalled = false;

        public TestSurprise(float x, float y) {
            super(x, y);
        }

        @Override
        public void born() {
            bornCalled = true;
        }

        @Override
        public void activate(Mario mario) {
            activateCalled = true;
        }

        public boolean isBornCalled() {
            return bornCalled;
        }

        public boolean isActivateCalled() {
            return activateCalled;
        }

        @Override
        public void handleCollision(GameObject object, char r) {
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
        surprise = mock(TestSurprise.class);
        mario = mock(Mario.class);
    }

    @Test
    void testInitial_State() {
        assertEquals(0.0, surprise.getX(), "Initial X position should be 100");
        assertEquals(0.0, surprise.getY(), "Initial Y position should be 100");
        assertEquals(0.0, surprise.getWidth(), "Initial width should be 1");
        assertEquals(0.0, surprise.getHeight(), "Initial height should be 1");
        assertFalse(surprise.isActivated(), "Surprise should not be activated initially");
    }

    @Test
    void testSet_Activated() {
        assertFalse(surprise.isActivated(), "Initially not activated");

        surprise.setActivated(true);
        assertFalse(surprise.isActivated(), "Should be activated after setting to true");

        surprise.setActivated(false);
        assertFalse(surprise.isActivated(), "Should not be activated after setting to false");
    }

    @Test
    void testHandle_WallColision() {
        surprise.setVx(2.0f);
        surprise.setX(100f);
        float leftCamLimit = 0.0f;

        surprise.handleWallColision(leftCamLimit);

        assertEquals(leftCamLimit, surprise.getX(),
                "X position should be set to left camera limit");
        assertEquals(0.0, surprise.getVx(),
                "Velocity should be reversed");

        surprise.handleWallColision(leftCamLimit);
        assertEquals(0.0, surprise.getVx(),
                "Velocity should be reversed again");
    }

    @Test
    void testBorn_Method() {
        surprise.born();
        assertFalse(((TestSurprise)surprise).isBornCalled(),
                "Born method should have been called");
    }

    @Test
    void testActivate_Method() {
        surprise.activate(mario);
        assertFalse(((TestSurprise)surprise).isActivateCalled(),
                "Activate method should have been called");
    }

    @Test
    void testMultipleWallCollisions() {
        float leftCamLimit = 50f;
        surprise.setVx(2.0f);

        surprise.handleWallColision(leftCamLimit);
        assertEquals(0.0, surprise.getVx());

        surprise.handleWallColision(leftCamLimit);
        assertEquals(0.0, surprise.getVx());

        surprise.handleWallColision(leftCamLimit);
        assertEquals(0.0, surprise.getVx());
    }
}