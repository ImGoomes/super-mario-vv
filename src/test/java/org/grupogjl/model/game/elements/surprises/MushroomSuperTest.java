package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MushroomSuperTest {

    private MushroomSuper mushroomSuper;

    @BeforeEach
    void setUp() {
        mushroomSuper = new MushroomSuper(10, 20);
    }

    @Test
    void testInitial_Setup() {
        assertEquals(10, mushroomSuper.getX(), "Initial x position should be 10.");
        assertEquals(20, mushroomSuper.getY(), "Initial y position should be 20.");
    }

    @Test
    void testActivate_WhenMarioIsSmall() {
        Mario mockMario = mock(Mario.class);
        when(mockMario.isStateBig()).thenReturn(false);
        when(mockMario.isStateFire()).thenReturn(false);

        mushroomSuper.activate(mockMario);

        verify(mockMario, times(1)).setStateBig(true);
        verify(mockMario, times(1)).setHeight(2);
    }

    @Test
    void testActivate_WhenMarioIsAlreadyBig() {
        Mario mockMario = mock(Mario.class);
        when(mockMario.isStateBig()).thenReturn(true);
        when(mockMario.isStateFire()).thenReturn(false);

        mushroomSuper.activate(mockMario);

        verify(mockMario, never()).setStateBig(true);
        verify(mockMario, never()).setHeight(2);
    }

    @Test
    void testActivate_WhenMarioIsInFireState() {
        Mario mockMario = mock(Mario.class);
        when(mockMario.isStateBig()).thenReturn(false);
        when(mockMario.isStateFire()).thenReturn(true);

        mushroomSuper.activate(mockMario);

        verify(mockMario, never()).setStateBig(true);
        verify(mockMario, never()).setHeight(2);
    }

    @Test
    void testGet_VirtualCoordinates() {
        Camera mockCamera = mock(Camera.class);
        when(mockCamera.getLeftCamLimit()).thenReturn(5f);

        assertEquals(5, mushroomSuper.getVirtX(mockCamera), "MushroomSuper's virtual x position should be adjusted by the camera's left limit.");
        assertEquals(20, mushroomSuper.getVirtY(), "MushroomSuper's virtual y position should remain unchanged.");
    }

    @Test
    void testGet_Image() {
        assertEquals("superMushroom.png", mushroomSuper.getImage(), "MushroomSuper's image should be 'superMushroom.png'.");
    }
}
