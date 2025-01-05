package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StarTest {

    private Star star;

    @BeforeEach
    void setUp() {
        star = new Star(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, star.getX(), "Initial x position should be 10.");
        assertEquals(20, star.getY(), "Initial y position should be 20.");
    }

    @Test
    void testBorn() {
        star.born();

        assertEquals(0.2f, star.getVx(), "Horizontal velocity should be set to 0.2 after born.");
        assertTrue(star.isFalling(), "Star should be in a falling state after born.");
    }

    @Test
    void testActivate() {
        Mario mockMario = mock(Mario.class);

        star.activate(mockMario);

        verify(mockMario, times(1)).setInvencibleTime(600);
        verify(mockMario, times(1)).setStateInvencible(true);
    }

    @Test
    void testHandle_CollisionWithStaticObjectUp() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(15f);

        star.handleCollision(staticObject, 'U');

        assertEquals(16, star.getY(), "Star's y position should adjust to static object's top.");
        assertEquals(0, star.getVy(), "Star's vertical velocity should be set to 0.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectDown() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(30f);
        when(staticObject.getHeight()).thenReturn(5f);

        star.handleCollision(staticObject, 'D');

        assertEquals(25, star.getY(), "Star's y position should adjust to static object's bottom.");
        assertFalse(star.isFalling(), "Star should not be falling.");
        assertEquals(-0.1f, star.getVy(), "Star's vertical velocity should be set to -0.1.");
        assertTrue(star.isJumping(), "Star should be in a jumping state.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectLeft() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5f);
        when(staticObject.getWidth()).thenReturn(2f);

        star.handleCollision(staticObject, 'L');

        assertEquals(7, star.getX(), "Star's x position should adjust to static object's right.");
        assertEquals(0.2f, star.getVx(), "Star's horizontal velocity should be set to 0.2.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectRight() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        star.handleCollision(staticObject, 'R');

        assertEquals(19, star.getX(), "Star's x position should adjust to static object's left.");
        assertEquals(-0.2f, star.getVx(), "Star's horizontal velocity should be set to -0.2.");
    }

    @Test
    void testHandle_CollisionOutOfRange() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        star.handleCollision(staticObject, 'A');
    }

    @Test
    void testHandle_CollisionWithNonStaticObject() {
        GameObject nonStaticObject = mock(GameObject.class);
        float initialX = star.getX();
        float initialY = star.getY();
        float initialVx = star.getVx();
        float initialVy = star.getVy();

        star.handleCollision(nonStaticObject, 'R');

        assertEquals(initialX, star.getX(), "X position should not change when colliding with non-static object");
        assertEquals(initialY, star.getY(), "Y position should not change when colliding with non-static object");
        assertEquals(initialVx, star.getVx(), "Horizontal velocity should not change when colliding with non-static object");
        assertEquals(initialVy, star.getVy(), "Vertical velocity should not change when colliding with non-static object");
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera mockCamera = mock(Camera.class);
        when(mockCamera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, star.getVirtX(mockCamera), "Star's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, star.getVirtY(), "Star's virtual y position should remain unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("star.png", star.getImage(), "Star's image should be 'star.png'.");
    }
}
