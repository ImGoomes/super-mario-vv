package org.grupogjl;

import org.grupogjl.state.StateGame;
import org.grupogjl.state.StateMenu;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class GameTests {
    private Game game;

    @BeforeEach
    void setUp() throws FontFormatException, IOException, URISyntaxException {
        game = Game.getInstance();
    }

    @Test
    void testSingletonInstance() throws FontFormatException, IOException, URISyntaxException {
        Game anotherInstance = Game.getInstance();
        assertSame(game, anotherInstance, "Game should be a singleton");
    }

    @Test
    void testSetStateMenu() {
        game.setStateMenu();
        assertTrue(game.getState() instanceof StateMenu, "State should be StateMenu");
    }

    @Test
    void testSetStateGame() throws IOException {
        game.setStateGame();
        assertTrue(game.getState() instanceof StateGame, "State should be StateGame");
    }

    @Test
    void testSetStatePause() throws IOException {
        game.setStateGame();
        game.setStatePause();
        assertTrue(game.getState() instanceof StatePause, "State should be StatePause");
    }

    @Test
    void testSetStateNull() {
        game.setStateNull();
        assertNull(game.getState(), "State should be null");
    }

    @Test
    void testRun() throws InterruptedException, IOException {
        // Todo: This test is more complex because it involves threading and timing.
    }
}