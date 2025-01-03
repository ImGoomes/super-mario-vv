package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.props.FireBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerFireBallTest {

    private ControllerFireBall controllerFireBall;
    private Level level;
    private List<FireBall> fireBalls;

    @BeforeEach
    void setup() {
        controllerFireBall = new ControllerFireBall();
        level = mock(Level.class);
        fireBalls = new ArrayList<>();
        when(level.getFireBalls()).thenReturn(fireBalls);
    }

    @Test
    void testUpdateStatus_RemovesInactiveFireBalls() {
        FireBall activeFireBall = mock(FireBall.class);
        when(activeFireBall.isActive()).thenReturn(true);

        FireBall inactiveFireBall = mock(FireBall.class);
        when(inactiveFireBall.isActive()).thenReturn(false);

        fireBalls.add(activeFireBall);
        fireBalls.add(inactiveFireBall);

        controllerFireBall.updateStatus(level, 1000L);

        assertEquals(1, fireBalls.size());
        assertEquals(activeFireBall, fireBalls.get(0));
        verify(activeFireBall, times(1)).updateLocation();
        verify(inactiveFireBall, never()).updateLocation();
        verify(level, times(1)).setFireBalls(fireBalls);
    }

    @Test
    void testUpdateStatus_UpdatesActiveFireBalls() {
        FireBall activeFireBall = mock(FireBall.class);
        when(activeFireBall.isActive()).thenReturn(true);

        fireBalls.add(activeFireBall);

        controllerFireBall.updateStatus(level, 1000L);

        verify(activeFireBall, times(1)).updateLocation();
        assertEquals(1, fireBalls.size());
        verify(level, times(1)).setFireBalls(fireBalls);
    }

    @Test
    void testStep_UpdatesFireBalls() {
        FireBall activeFireBall = mock(FireBall.class);
        when(activeFireBall.isActive()).thenReturn(true);

        fireBalls.add(activeFireBall);

        controllerFireBall.step(level, GeneralGui.ACTION.THROWBALL, 1000L);

        verify(activeFireBall, times(1)).updateLocation();
        verify(level, times(1)).setFireBalls(fireBalls);
    }
}
