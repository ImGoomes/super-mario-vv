package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.commands.GameCommand;
import org.grupogjl.controller.game.ControllerLevel;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ControllerGameTests {

    private ControllerGame controllerGame;
    private Game mockGame;
    private StateGame mockStateGame;
    private Level mockLevel;
    private Mario mockMario;
    private GameCommand mockGameOverCommand;
    private GameCommand mockExitToMenuCommand;
    private GameCommand mockPauseCommand;
    private ControllerLevel mockControllerLevel;

    @BeforeEach
    void setup() {
        controllerGame = new ControllerGame();

        mockGame = mock(Game.class);
        mockStateGame = mock(StateGame.class);
        mockLevel = mock(Level.class);
        mockMario = mock(Mario.class);
        mockGameOverCommand = mock(GameCommand.class);
        mockExitToMenuCommand = mock(GameCommand.class);
        mockPauseCommand = mock(GameCommand.class);
        mockControllerLevel = mock(ControllerLevel.class);

        when(mockGame.getStateGame()).thenReturn(mockStateGame);
        when(mockStateGame.getModel()).thenReturn(mockLevel);
        when(mockLevel.getMario()).thenReturn(mockMario);

        controllerGame = new ControllerGame() {
            @Override
            public GameCommand getGameOverCommand() {
                return mockGameOverCommand;
            }

            @Override
            public GameCommand getExitToMenuCommand() {
                return mockExitToMenuCommand;
            }

            @Override
            public GameCommand getPauseCommand() {
                return mockPauseCommand;
            }

            @Override
            public ControllerLevel getControllerLevel() {
                return mockControllerLevel;
            }
        };
    }

    @Test
    void testSet_GameOverAndExitToMenu() throws IOException {
        when(mockMario.getLives()).thenReturn(0);

        controllerGame.step(mockGame, GeneralGui.ACTION.SELECT, 100L);

        verify(mockGameOverCommand).execute(mockGame);
        verify(mockExitToMenuCommand).execute(mockGame);
        verifyNoInteractions(mockPauseCommand);
        verifyNoInteractions(mockControllerLevel);
    }

    @Test
    void testSet_GameOverWithoutSelectAction() throws IOException {
        when(mockMario.getLives()).thenReturn(0);

        controllerGame.step(mockGame, GeneralGui.ACTION.QUIT, 100L);

        verify(mockGameOverCommand).execute(mockGame);
        verifyNoInteractions(mockExitToMenuCommand);
        verifyNoInteractions(mockPauseCommand);
        verifyNoInteractions(mockControllerLevel);
    }

    @Test
    void testSet_PauseAction() throws IOException {
        when(mockMario.getLives()).thenReturn(3);

        controllerGame.step(mockGame, GeneralGui.ACTION.QUIT, 100L);

        verify(mockPauseCommand).execute(mockGame);
        verifyNoInteractions(mockGameOverCommand);
        verifyNoInteractions(mockExitToMenuCommand);
        verifyNoInteractions(mockControllerLevel);
    }

    @Test
    void testSet_NoneAction() throws IOException {
        when(mockMario.getLives()).thenReturn(3);

        controllerGame.step(mockGame, GeneralGui.ACTION.NONE, 100L);

        verify(mockControllerLevel).step(mockLevel, GeneralGui.ACTION.NONE, 100L);
        verifyNoInteractions(mockGameOverCommand);
        verifyNoInteractions(mockExitToMenuCommand);
        verifyNoInteractions(mockPauseCommand);
    }
}