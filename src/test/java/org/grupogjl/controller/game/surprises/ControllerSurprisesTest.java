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
        // Create a spy of ControllerSurprises to monitor method calls
        controllerSurprises = spy(new ControllerSurprises());
        level = mock(Level.class);
        surprises = new ArrayList<>();
        when(level.getSurprises()).thenReturn(surprises);
    }

    @Test
    void testStep_VerifyUpdateStatusAndInteractions() {
        // Arrange
        GeneralGui.ACTION action = GeneralGui.ACTION.RIGHT; // Example action
        long time = 1000L;

        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);
        surprises.add(activeSurprise);

        // Act
        controllerSurprises.step(level, action, time);

        // Assert
        verify(controllerSurprises, times(1)).updateStatus(level, time); // Verify updateStatus is called
        verify(level, times(1)).setSurprises(surprises); // Verify the surprises list is updated
    }

    @Test
    void testUpdateStatus_RemovesInactiveSurprises() {
        // Arrange
        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);

        Surprise inactiveSurprise = mock(Surprise.class);
        when(inactiveSurprise.isActivated()).thenReturn(false);

        surprises.add(activeSurprise);
        surprises.add(inactiveSurprise);

        // Act
        controllerSurprises.updateStatus(level, 1000L);

        // Assert
        assert surprises.size() == 1; // Only active surprise remains
        verify(activeSurprise, times(1)).updateLocation(); // Active surprise updates location
        verify(inactiveSurprise, never()).updateLocation(); // Inactive surprise does not update location
        verify(level, times(1)).setSurprises(surprises); // Surprises list is updated in level
    }

    @Test
    void testUpdateStatus_UpdatesActiveSurprises() {
        // Arrange
        Surprise activeSurprise = mock(Surprise.class);
        when(activeSurprise.isActivated()).thenReturn(true);
        surprises.add(activeSurprise);

        // Act
        controllerSurprises.updateStatus(level, 1000L);

        // Assert
        verify(activeSurprise, times(1)).updateLocation(); // Active surprise updates location
        verify(level, times(1)).setSurprises(surprises); // Surprises list is updated in level
    }
}
