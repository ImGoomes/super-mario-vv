package org.grupogjl.controller.game.blocks;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.blocks.SurpriseBlock;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerSurpriseBlocksTest {

    private ControllerSurpriseBlocks controller;
    private Level mockLevel;
    private SurpriseBlock mockSurpriseBlock;
    private Surprise mockSurprise;

    @BeforeEach
    void setUp() {
        controller = new ControllerSurpriseBlocks();
        mockLevel = mock(Level.class);
        mockSurpriseBlock = mock(SurpriseBlock.class);
        mockSurprise = mock(Surprise.class); // Mocking the Surprise class
    }

    @Test
    void testStep_AddsSurpriseIfBlockIsUsedAndActivated() {
        when(mockSurpriseBlock.isUsed()).thenReturn(true);
        when(mockSurpriseBlock.getSurprise()).thenReturn(mockSurprise);
        when(mockSurprise.isActivated()).thenReturn(true);

        List<GameObject> objects = new ArrayList<>();
        List<SurpriseBlock> surpriseBlocks = List.of(mockSurpriseBlock);

        when(mockLevel.getObjects()).thenReturn(objects);
        when(mockLevel.getSurpriseBlocks()).thenReturn(surpriseBlocks);

        controller.step(mockLevel, GeneralGui.ACTION.RIGHT, System.currentTimeMillis());

        verify(mockLevel).setObjects(argThat(updatedObjects ->
                updatedObjects.size() == 1 && updatedObjects.contains(mockSurprise)
        ));
    }

    @Test
    void testStep_DoesNotAddSurpriseIfNotActivated() {
        when(mockSurpriseBlock.isUsed()).thenReturn(true);
        when(mockSurpriseBlock.getSurprise()).thenReturn(mockSurprise);
        when(mockSurprise.isActivated()).thenReturn(false);

        List<GameObject> objects = new ArrayList<>();
        List<SurpriseBlock> surpriseBlocks = List.of(mockSurpriseBlock);

        when(mockLevel.getObjects()).thenReturn(objects);
        when(mockLevel.getSurpriseBlocks()).thenReturn(surpriseBlocks);

        controller.step(mockLevel, GeneralGui.ACTION.UP, System.currentTimeMillis());

        verify(mockLevel).setObjects(argThat(List::isEmpty));
    }

    @Test
    void testStep_DoesNotAddSurpriseIfBlockIsUnused() {
        when(mockSurpriseBlock.isUsed()).thenReturn(false);
        List<GameObject> objects = new ArrayList<>();
        when(mockLevel.getObjects()).thenReturn(objects);
        when(mockLevel.getSurpriseBlocks()).thenReturn(List.of(mockSurpriseBlock));

        controller.step(mockLevel, GeneralGui.ACTION.DOWN, System.currentTimeMillis());

        verify(mockLevel).setObjects(argThat(List::isEmpty));
    }

    @Test
    void testStep_DoesNotDuplicateSurpriseIfAlreadyPresent() {
        List<GameObject> existingObjects = new ArrayList<>();
        existingObjects.add(mockSurprise);

        when(mockSurpriseBlock.isUsed()).thenReturn(true);
        when(mockSurpriseBlock.getSurprise()).thenReturn(mockSurprise);
        when(mockLevel.getObjects()).thenReturn(existingObjects);
        when(mockLevel.getSurpriseBlocks()).thenReturn(List.of(mockSurpriseBlock));

        controller.step(mockLevel, GeneralGui.ACTION.LEFT, System.currentTimeMillis());

        verify(mockLevel).setObjects(argThat(objects ->
                objects.size() == 1 && objects.contains(mockSurprise)));
    }
}
