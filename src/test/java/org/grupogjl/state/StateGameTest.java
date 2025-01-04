package org.grupogjl.state;

import org.grupogjl.viewer.ViewerGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.level.LoaderLevelBuilder;

import java.io.IOException;

class StateGameTest {
    private StateGame stateGame;

    @Mock
    private Game game;

    @Mock
    private LanternaGui gui;

    @Mock
    private Level mockLevel;

    @Mock
    private LoaderLevelBuilder mockLoader;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        stateGame = new StateGame();
    }

    @Test
    void testInitial_State() {
        assertEquals(1, stateGame.getLeveln());
        assertNotNull(stateGame.getLevel());
        assertEquals(2, stateGame.getState());
        assertFalse(stateGame.isGameOver());
    }

    @Test
    void testGet_Model() {
        Level level = stateGame.getModel();
        assertNotNull(level);
        assertEquals(stateGame.getLevel(), level);
    }

    @Test
    void testSetAndGet_Level() {
        Level newLevel = mock(Level.class);
        stateGame.setLevel(newLevel);
        assertEquals(newLevel, stateGame.getLevel());
    }

    @Test
    void testSetAndGet_GameOver() {
        assertFalse(stateGame.isGameOver());
        stateGame.setGameOver(true);
        assertTrue(stateGame.isGameOver());
    }

    @Test
    void testReset_Level() throws IOException {
        Level initialLevel = stateGame.getLevel();
        Mario mario = initialLevel.getMario();
        float initialX = mario.getX();
        float initialY = mario.getY();

        mario.setX(10);
        mario.setY(10);

        stateGame.resetLevel();

        Level resetLevel = stateGame.getLevel();
        Mario resetMario = resetLevel.getMario();
        assertEquals(0, resetMario.getX());
        assertEquals(0, resetMario.getY());
    }

    @Test
    void testNextLevel_WithinBounds() throws IOException {
        assertEquals(1, stateGame.getLeveln());
        stateGame.nextLevel();
        assertEquals(2, stateGame.getLeveln());
    }

    @Test
    void testStepGameLogic() throws IOException {
        when(game.getStateGame()).thenReturn(stateGame);

        when(gui.getNextAction()).thenReturn(GeneralGui.ACTION.UP);

        long startTime = System.currentTimeMillis();
        stateGame.step(game, gui, startTime);

        verify(gui).getNextAction();
        verify(gui).refresh();
    }

    @Test
    void testDraw_WithValidGui(){
        GeneralGui mockGeneralGui = mock(GeneralGui.class);
        stateGame.draw(mockGeneralGui);
    }

    @Test
    void testDraw_RuntimeException() throws IOException {
        GeneralGui mockGeneralGui = mock(GeneralGui.class);
        ViewerGame mockViewerGame = mock(ViewerGame.class);
        stateGame.setViewer(mockViewerGame);

        doThrow(IOException.class).when(mockViewerGame).draw(any(StateGame.class), any(GeneralGui.class));

        assertThrows(RuntimeException.class, () -> stateGame.draw(mockGeneralGui));
    }

    @Test
    void testMario_Setup() {
        Level level = stateGame.getLevel();
        Mario mario = level.getMario();
        assertNotNull(mario);
        assertEquals(0, mario.getX());
        assertEquals(0, mario.getY());
        assertEquals(1, mario.getWidth());
        assertEquals(1, mario.getHeight());
        assertEquals(3, mario.getLives());
        assertEquals(0, mario.getCoins());
    }

    @Test
    void testMario_LevelInteraction() throws IOException {
        Level initialLevel = stateGame.getLevel();
        Mario initialMario = initialLevel.getMario();

        stateGame.nextLevel();

        Level newLevel = stateGame.getLevel();
        Mario newLevelMario = newLevel.getMario();

        assertEquals(0, newLevelMario.getX());
        assertEquals(0, newLevelMario.getY());
        assertEquals(initialMario, newLevelMario);
    }
}