package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.controller.ControllerMenu;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.viewer.ViewerMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StateMenuTest {

    private StateMenu stateMenu;
    private Game mockGame;
    private LanternaGui mockGui;
    private ControllerMenu mockController;
    private ViewerMenu mockViewer;
    private MenuModel mockMenuModel;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockGui = mock(LanternaGui.class);
        mockController = mock(ControllerMenu.class);
        mockViewer = mock(ViewerMenu.class);
        mockMenuModel = mock(MenuModel.class);

        stateMenu = new StateMenu();
        stateMenu.controller = mockController;
        stateMenu.viewer = mockViewer;
    }

    @Test
    void testGet_Model() {
        MenuModel model = stateMenu.getModel();
        assertEquals(stateMenu.getModel(), model, "StateMenu should return its menu model");
    }

    @Test
    void testGet_State() {
        assertEquals(1, stateMenu.getState(), "StateMenu should return 1 as its state ID");
    }

    @Test
    void testStep_Menu() throws IOException {
        GeneralGui.ACTION mockAction = GeneralGui.ACTION.UP;
        when(mockGui.getNextAction()).thenReturn(mockAction);

        long startTime = System.currentTimeMillis();

        stateMenu.step(mockGame, mockGui, startTime);

        verify(mockGui, times(1)).getNextAction();
        verify(mockController, times(1)).step(mockGame, mockAction, startTime);
        verify(mockViewer, times(1)).draw(stateMenu, mockGui);
    }
}
