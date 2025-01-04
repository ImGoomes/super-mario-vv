package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DestroyableBlockTest {

    private DestroyableBlock destroyableBlock;
    private Camera mockCamera;
    private Mario mockMario;

    @BeforeEach
    void setUp() {
        destroyableBlock = new DestroyableBlock(10.0f, 20.0f, 30.0f, 40.0f);
        mockCamera = mock(Camera.class);
        mockMario = mock(Mario.class);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, destroyableBlock.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, destroyableBlock.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(30.0f, destroyableBlock.getWidth(), "Width should be initialized correctly");
        assertEquals(40.0f, destroyableBlock.getHeight(), "Height should be initialized correctly");
        assertEquals(1, destroyableBlock.getStrenght(), "Default strength should be 1");
    }

    @Test
    void testSetAndGet_Strength() {
        destroyableBlock.setStrenght(5);
        assertEquals(5, destroyableBlock.getStrenght(), "Strength should be updated correctly");
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, destroyableBlock.getVirtX(mockCamera), "getVirtX should return the correct virtual X-coordinate");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(20.0f, destroyableBlock.getVirtY(), "getVirtY should return the Y-coordinate");
    }

    @Test
    void testGet_Image() {
        assertEquals("breakableBlock.png", destroyableBlock.getImage(), "getImage should return the correct image path");
    }

    @Test
    void testGot_HitWithSmallMario() {
        when(mockMario.isStateBig()).thenReturn(false);
        when(mockMario.isStateFire()).thenReturn(false);

        destroyableBlock.gotHit(mockMario);

        assertEquals(1, destroyableBlock.getStrenght(), "Strength should not change when Mario is small");
    }

    @Test
    void testGot_HitWithBigMario() {
        when(mockMario.isStateBig()).thenReturn(true);
        when(mockMario.isStateFire()).thenReturn(false);

        destroyableBlock.gotHit(mockMario);

        assertEquals(0, destroyableBlock.getStrenght(), "Strength should be 0 when Mario is big");
    }

    @Test
    void testGot_HitWithFireMario() {
        when(mockMario.isStateBig()).thenReturn(false);
        when(mockMario.isStateFire()).thenReturn(true);

        destroyableBlock.gotHit(mockMario);

        assertEquals(0, destroyableBlock.getStrenght(), "Strength should be 0 when Mario is in fire state");
    }
}
