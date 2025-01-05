package org.grupogjl.model.game.elements.props;

import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireBallTest {

    private FireBall fireBall;

    @BeforeEach
    void setUp() {
        fireBall = new FireBall(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, fireBall.getX(), "Initial x position should be 10.");
        assertEquals(20, fireBall.getY(), "Initial y position should be 20.");
        assertEquals(1, fireBall.getVx(), "Initial horizontal velocity should be 1.");
        assertEquals(0, fireBall.getVy(), "Initial vertical velocity should be 0.");
        assertEquals(0.1f, fireBall.getG(), "Gravity should be 0.1.");
        assertTrue(fireBall.isFalling(), "FireBall should initially be falling.");
        assertTrue(fireBall.isActive(), "FireBall should initially be active.");
    }

    @Test
    void testSet_Active() {
        fireBall.setActive(false);
        assertFalse(fireBall.isActive(), "FireBall should be inactive after setActive(false).");
    }

    @Test
    void testHandle_CollisionWithStaticObjectUp() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(15f);

        fireBall.handleCollision(staticObject, 'U');

        assertEquals(16, fireBall.getY(), "FireBall's y position should adjust to static object's top.");
        assertEquals(0, fireBall.getVy(), "FireBall's vertical velocity should be set to 0.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectDown() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(30f);
        when(staticObject.getHeight()).thenReturn(5f);

        fireBall.handleCollision(staticObject, 'D');

        assertEquals(25, fireBall.getY(), "FireBall's y position should adjust to static object's bottom.");
        assertFalse(fireBall.isFalling(), "FireBall should not be falling.");
        assertEquals(-0.1f, fireBall.getVy(), "FireBall's vertical velocity should be set to -0.1.");
        assertTrue(fireBall.isJumping(), "FireBall should be in a jumping state.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectLeft() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5f);
        when(staticObject.getWidth()).thenReturn(2f);

        fireBall.handleCollision(staticObject, 'L');

        assertEquals(7, fireBall.getX(), "FireBall's x position should adjust to static object's right.");
        assertFalse(fireBall.isActive(), "FireBall should be deactivated after hitting a static object on the left.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectRight() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        fireBall.handleCollision(staticObject, 'R');

        assertEquals(19, fireBall.getX(), "FireBall's x position should adjust to static object's left.");
        assertFalse(fireBall.isActive(), "FireBall should be deactivated after hitting a static object on the right.");
    }

    @Test
    void testHandle_CollisionWithEnemy() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getLives()).thenReturn(5);

        fireBall.handleCollision(enemy, ' ');

        verify(enemy, times(1)).setLives(3);
        assertFalse(fireBall.isActive(), "FireBall should be deactivated after hitting an enemy.");
    }

    @Test
    void testHandle_WallCollision() {
        fireBall.handleWallColision(0);

        assertEquals(0, fireBall.getX(), "FireBall's x position should reset to the wall limit.");
        assertEquals(0, fireBall.getVx(), "FireBall's horizontal velocity should be set to 0.");
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera camera = mock(Camera.class);
        when(camera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, fireBall.getVirtX(camera), "FireBall's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, fireBall.getVirtY(), "FireBall's virtual y position should be unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("fireBall.png", fireBall.getImage(), "FireBall's image should be 'fireBall.png'.");
    }

    @Test
    void testHandleCollision_UpperCollisionWithStaticObject() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(15f);

        fireBall.handleCollision(staticObject, 'U');

        assertEquals(16, fireBall.getY(), "FireBall's y position should adjust to the static object's top.");
        assertEquals(0, fireBall.getVy(), "FireBall's vertical velocity should be set to 0.");
    }

    @Test
    void testHandleCollision_DownwardCollisionWithStaticObject() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(30f);
        when(staticObject.getHeight()).thenReturn(5f);

        fireBall.handleCollision(staticObject, 'D');

        assertEquals(25, fireBall.getY(), "FireBall's y position should adjust to the static object's bottom.");
        assertFalse(fireBall.isFalling(), "FireBall should not be falling.");
        assertEquals(-0.1f, fireBall.getVy(), "FireBall's vertical velocity should be set to -0.1.");
        assertTrue(fireBall.isJumping(), "FireBall should enter a jumping state.");
    }

    @Test
    void testHandleCollision_LeftwardCollisionWithStaticObject() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5f);
        when(staticObject.getWidth()).thenReturn(2f);

        fireBall.handleCollision(staticObject, 'L');

        assertEquals(7, fireBall.getX(), "FireBall's x position should adjust to the static object's right edge.");
        assertFalse(fireBall.isActive(), "FireBall should be deactivated after a leftward collision.");
    }

    @Test
    void testHandleCollision_RightwardCollisionWithStaticObject() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        fireBall.handleCollision(staticObject, 'R');

        assertEquals(19, fireBall.getX(), "FireBall's x position should adjust to the static object's left edge.");
        assertFalse(fireBall.isActive(), "FireBall should be deactivated after a rightward collision.");
    }

    @Test
    void testHandleCollision_NonExistent() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        fireBall.handleCollision(staticObject, 'A');
    }

    @Test
    void testHandleCollision_AllCases() {
        testHandleCollision_UpperCollisionWithStaticObject();
        testHandleCollision_DownwardCollisionWithStaticObject();
        testHandleCollision_LeftwardCollisionWithStaticObject();
        testHandleCollision_RightwardCollisionWithStaticObject();
        testHandleCollision_NonExistent();
    }
}
