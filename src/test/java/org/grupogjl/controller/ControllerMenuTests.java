package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ControllerMenuTests {

    private ControllerMenu controllerMenu;
    private Game mockGame;
    private StateMenu mockStateMenu;
    private MenuModel mockModel;

    @BeforeEach
    void setup() {
        controllerMenu = new ControllerMenu();

        mockGame = mock(Game.class);
        mockStateMenu = mock(StateMenu.class);
        mockModel = mock(MenuModel.class);

        when(mockGame.getStateMenu()).thenReturn(mockStateMenu);
        when(mockStateMenu.getModel()).thenReturn(mockModel);
    }

    @Test
    void testExecuteOptionSelectedAndActionSelect() {
        when(mockModel.isSelectedOption()).thenReturn(true);

        controllerMenu.step(mockGame, GeneralGui.ACTION.SELECT, 100L);

        verify(mockModel, times(1)).setSelectedOption(false);
        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testExecuteOptionNotSelectedAndActionDown() {
        when(mockModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(mockGame, GeneralGui.ACTION.DOWN, 100L);

        verify(mockModel, times(1)).nextPosition();
        verify(mockModel, never()).setSelectedOption(false);
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testExecuteOptionNotSelectedAndActionUp() {
        when(mockModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(mockGame, GeneralGui.ACTION.UP, 100L);

        verify(mockModel, times(1)).lastPosition();
        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).setSelectedOption(false);
        verify(mockModel, never()).execute(mockGame);
    }

    @Test
    void testExecuteOptionNotSelectedAndActionSelect() {
        when(mockModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(mockGame, GeneralGui.ACTION.SELECT, 100L);

        verify(mockModel, times(1)).execute(mockGame);
        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).setSelectedOption(false);
    }

    @Test
    void testExecuteOptionNotSelectedAndNoRelevantAction() {
        when(mockModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(mockGame, GeneralGui.ACTION.NONE, 100L);

        verify(mockModel, never()).nextPosition();
        verify(mockModel, never()).lastPosition();
        verify(mockModel, never()).setSelectedOption(false);
        verify(mockModel, never()).execute(mockGame);
    }
}
