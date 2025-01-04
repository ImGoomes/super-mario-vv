package org.grupogjl;

import org.grupogjl.state.State;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StateMenu;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    private Game game;
    private State mockState;

    @BeforeEach
    void setUp() throws FontFormatException, IOException, URISyntaxException {
        game = Game.getInstance();
        mockState = mock(State.class);
        game.setStateGame(mockState);
    }

    @Test
    void testSingleton_Instance() throws FontFormatException, IOException, URISyntaxException {
        Game anotherInstance = Game.getInstance();
        assertSame(game, anotherInstance, "Game should be a singleton");
    }

    @Test
    void testSet_StateMenu() {
        game.setStateMenu();
        game.getStateMenu();
        assertTrue(game.getState() instanceof StateMenu, "State should be StateMenu");
    }

    @Test
    void testSet_StateGame() throws IOException {
        game.setStateGame();
        assertTrue(game.getState() instanceof StateGame, "State should be StateGame");
    }

    @Test
    void testSet_StatePause() throws IOException {
        game.setStateGame();
        game.getStateGame();
        game.setStatePause();
        game.getStatePause();
        assertTrue(game.getState() instanceof StatePause, "State should be StatePause");
    }

    @Test
    void testSet_StateNull() {
        game.setStateNull();
        assertNull(game.getState(), "State should be null");
    }

//    @Test
//    void testRun() throws InterruptedException, IOException {
//        // Mock dependencies
//        LanternaGui mockGui = mock(LanternaGui.class);
//        State mockState = mock(State.class);
//
//        // Create the Game instance with the mocked GUI
//        Game game = new Game(mockGui);
//
//        game.setStateGame(mockState);
//
//        Thread testThread = new Thread(() -> {
//            try {
//                game.run();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        testThread.start();
//
//        Thread.sleep(500);
//
//        game.setStateNull();
//
//        testThread.join();
//
//        verify(mockState, atLeastOnce()).step(eq(game), eq(mockGui), anyLong());
//    }
}