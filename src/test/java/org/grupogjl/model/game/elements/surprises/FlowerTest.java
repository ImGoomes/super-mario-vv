package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlowerTest {

    private Flower flower;

    @BeforeEach
    void setUp() {
        flower = new Flower(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, flower.getX(), "Initial x position should be 10.");
        assertEquals(20, flower.getY(), "Initial y position should be 20.");
    }

    @Test
    void testBorn() {
        flower.born();

        assertEquals(19, flower.getY(), "Y position should decrease by 1 after born.");
        assertTrue(flower.isFalling(), "Flower should be in a falling state after born.");
    }

    @Test
    void testActivate() {
        Mario mockMario = mock(Mario.class);

        flower.activate(mockMario);

        verify(mockMario, times(1)).setHeight(2);
        verify(mockMario, times(1)).setStateFire(true);
        verify(mockMario, times(1)).setStateBig(false);
    }

    @Test
    void testHandle_CollisionWithStaticObjectUp() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(15f);

        flower.handleCollision(staticObject, 'U');

        assertEquals(16, flower.getY(), "Flower's y position should adjust to static object's top.");
        assertEquals(0, flower.getVy(), "Flower's vertical velocity should be set to 0.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectDown() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(30f);
        when(staticObject.getHeight()).thenReturn(5f);

        flower.handleCollision(staticObject, 'D');

        assertEquals(25, flower.getY(), "Flower's y position should adjust to static object's bottom.");
        assertFalse(flower.isFalling(), "Flower should not be falling.");
        assertEquals(0, flower.getVy(), "Flower's vertical velocity should be set to 0.");
        assertFalse(flower.isJumping(), "Flower should not be jumping.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectLeft() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5f);
        when(staticObject.getWidth()).thenReturn(2f);

        flower.handleCollision(staticObject, 'L');

        assertEquals(7, flower.getX(), "Flower's x position should adjust to static object's right.");
    }

    @Test
    void testHandle_CollisionWithStaticObjectRight() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        flower.handleCollision(staticObject, 'R');

        assertEquals(19, flower.getX(), "Flower's x position should adjust to static object's left.");
    }

    @Test
    void testHandle_CollisionWithOutOfRange() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(20f);

        flower.handleCollision(staticObject, 'A');
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera mockCamera = mock(Camera.class);
        when(mockCamera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, flower.getVirtX(mockCamera), "Flower's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, flower.getVirtY(), "Flower's virtual y position should remain unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("flower.png", flower.getImage(), "Flower's image should be 'flower.png'.");
    }

    @Test
    void testHandle_CollisionWithNonStaticObject() {
        GameObject nonStaticObject = mock(GameObject.class);
        float initialX = flower.getX();
        float initialY = flower.getY();
        float initialVx = flower.getVx();
        float initialVy = flower.getVy();

        flower.handleCollision(nonStaticObject, 'R');

        assertEquals(initialX, flower.getX(), "X position should not change when colliding with non-static object");
        assertEquals(initialY, flower.getY(), "Y position should not change when colliding with non-static object");
        assertEquals(initialVx, flower.getVx(), "Horizontal velocity should not change when colliding with non-static object");
        assertEquals(initialVy, flower.getVy(), "Vertical velocity should not change when colliding with non-static object");
    }

    @Test
    void testHandleCollision_AllCases() {
        testHandle_CollisionWithStaticObjectUp();
        testHandle_CollisionWithStaticObjectDown();
        testHandle_CollisionWithStaticObjectLeft();
        testHandle_CollisionWithStaticObjectRight();
        testHandle_CollisionWithOutOfRange();
    }
}
