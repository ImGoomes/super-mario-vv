package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SurpriseBlockTest {

    private SurpriseBlock surpriseBlock;
    private Camera mockCamera;
    private Mario mockMario;
    private Surprise mockSurprise;

    @BeforeEach
    void setUp() {
        surpriseBlock = new SurpriseBlock(10.0f, 20.0f, 30.0f, 40.0f);
        mockCamera = mock(Camera.class);
        mockMario = mock(Mario.class);
        mockSurprise = mock(Surprise.class);
    }

    @Test
    void testConstructor() {
        assertEquals(10.0f, surpriseBlock.getX(), "X-coordinate should be initialized correctly");
        assertEquals(20.0f, surpriseBlock.getY(), "Y-coordinate should be initialized correctly");
        assertEquals(30.0f, surpriseBlock.getWidth(), "Width should be initialized correctly");
        assertEquals(40.0f, surpriseBlock.getHeight(), "Height should be initialized correctly");
        assertFalse(surpriseBlock.isUsed(), "Surprise block should be unused initially");
        assertFalse(surpriseBlock.getOpen(), "Surprise block should be closed initially");
        assertNull(surpriseBlock.getSurprise(), "Surprise should be null initially");
    }

    @Test
    void testSetAndGet_Used() {
        surpriseBlock.setUsed(true);
        assertTrue(surpriseBlock.isUsed(), "Used status should be updated correctly");
    }

    @Test
    void testSetAndGet_Surprise() {
        surpriseBlock.setSurprise(mockSurprise);
        assertEquals(mockSurprise, surpriseBlock.getSurprise(), "Surprise should be set and retrieved correctly");
    }

    @Test
    void testSetAndGet_Open() {
        surpriseBlock.setOpen(true);
        assertTrue(surpriseBlock.getOpen(), "Open status should be updated correctly");
    }

    @Test
    void testGet_VirtX() {
        when(mockCamera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, surpriseBlock.getVirtX(mockCamera), "getVirtX should return the correct virtual X-coordinate");
    }

    @Test
    void testGet_VirtY() {
        assertEquals(20.0f, surpriseBlock.getVirtY(), "getVirtY should return the correct Y-coordinate");
    }

    @Test
    void testGetImage() {
        assertEquals("surpriseBlock.png", surpriseBlock.getImage(), "Image should be 'surpriseBlock.png' when unused");

        surpriseBlock.setUsed(true);
        assertEquals("emptySurpriseBlock.png", surpriseBlock.getImage(), "Image should be 'emptySurpriseBlock.png' when used");
    }

    @Test
    void testGot_Hit() {
        surpriseBlock.setSurprise(mockSurprise);
        surpriseBlock.setUsed(false);

        surpriseBlock.gotHit(mockMario);

        assertTrue(surpriseBlock.isUsed(), "Block should be marked as used after gotHit");
        verify(mockSurprise, times(1)).setY(anyFloat());
        verify(mockSurprise, times(1)).setActivated(true);
        verify(mockSurprise, times(1)).born();
    }

    @Test
    void testGotHit_AlreadyUsed() {
        surpriseBlock.setUsed(true);

        surpriseBlock.gotHit(mockMario);

        verifyNoInteractions(mockSurprise);
    }

    @Test
    void testFree_Surprise() {
        surpriseBlock.setSurprise(mockSurprise);
        when(mockSurprise.getY()).thenReturn(50.0f);

        surpriseBlock.freeSurprise();

        verify(mockSurprise, times(1)).setY(49.0f); // currentY - 1
        verify(mockSurprise, times(1)).setActivated(true);
        verify(mockSurprise, times(1)).born();
    }
}
