package org.grupogjl.controller.game.surprises;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerSurprisesTest {

    private ControllerSurprises controllerSurprises;
    private Level level;
    private List<Surprise> surprises;

    @BeforeEach
    void setup() {
        controllerSurprises = spy(new ControllerSurprises());
        level = mock(Level.class);
        surprises = new ArrayList<>();
        when(level.getSurprises()).thenReturn(surprises);
    }

    @Test
    void testStep_VerifyUpdateStatusAndInteractions() {
        GeneralGui.ACTION action = GeneralGui.ACTION.RIGHT;
        long time = 1000L;

        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);
        surprises.add(activeSurprise);

        controllerSurprises.step(level, action, time);

        verify(controllerSurprises, times(1)).updateStatus(level, time);
        verify(level, times(1)).setSurprises(surprises);
    }

    @Test
    void testUpdateStatus_RemovesInactiveSurprises() {
        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);

        Surprise inactiveSurprise = mock(Surprise.class);
        when(inactiveSurprise.isActivated()).thenReturn(false);

        surprises.add(activeSurprise);
        surprises.add(inactiveSurprise);

        controllerSurprises.updateStatus(level, 1000L);

        assert surprises.size() == 1;
        verify(activeSurprise, times(1)).updateLocation();
        verify(inactiveSurprise, never()).updateLocation();
        verify(level, times(1)).setSurprises(surprises);
    }

    @Test
    void testUpdateStatus_UpdatesActiveSurprises() {
        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);
        surprises.add(activeSurprise);

        controllerSurprises.updateStatus(level, 1000L);

        verify(activeSurprise, times(1)).updateLocation();
        verify(level, times(1)).setSurprises(surprises);
    }
}
