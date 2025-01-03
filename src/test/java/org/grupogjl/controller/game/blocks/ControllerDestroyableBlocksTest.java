package org.grupogjl.controller.game.blocks;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.blocks.DestroyableBlock;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerDestroyableBlocksTest {

    private ControllerDestroyableBlocks controller;
    private Level mockLevel;
    private DestroyableBlock mockBlock1;
    private DestroyableBlock mockBlock2;

    @BeforeEach
    void setUp() {
        controller = new ControllerDestroyableBlocks();
        mockLevel = mock(Level.class);
        mockBlock1 = mock(DestroyableBlock.class);
        mockBlock2 = mock(DestroyableBlock.class);
    }

    @Test
    void testUpdateStatus_RemovesBlocksWithZeroOrLessStrength() {
        when(mockBlock1.getStrenght()).thenReturn(0);
        when(mockBlock2.getStrenght()).thenReturn(10);

        List<DestroyableBlock> blocks = new ArrayList<>();
        blocks.add(mockBlock1);
        blocks.add(mockBlock2);

        when(mockLevel.getDestroyableBlocks()).thenReturn(blocks);

        controller.updateStatus(mockLevel, System.currentTimeMillis());

        verify(mockLevel).setDestroyableBlocks(argThat(updatedBlocks ->
                updatedBlocks.size() == 1 && updatedBlocks.contains(mockBlock2)
        ));
    }

    @Test
    void testUpdateStatus_LeavesStrongBlocksUnchanged() {
        when(mockBlock1.getStrenght()).thenReturn(20);
        when(mockBlock2.getStrenght()).thenReturn(10);

        List<DestroyableBlock> blocks = new ArrayList<>();
        blocks.add(mockBlock1);
        blocks.add(mockBlock2);

        when(mockLevel.getDestroyableBlocks()).thenReturn(blocks);

        controller.updateStatus(mockLevel, System.currentTimeMillis());

        verify(mockLevel).setDestroyableBlocks(argThat(updatedBlocks ->
                updatedBlocks.size() == 2 && updatedBlocks.containsAll(blocks)
        ));
    }

    @Test
    void testStep_CallsUpdateStatus() {
        long currentTime = System.currentTimeMillis();

        controller.step(mockLevel, GeneralGui.ACTION.UP, currentTime);

        verify(mockLevel, atLeastOnce()).getDestroyableBlocks();
    }
}
