package org.grupogjl;

import org.grupogjl.Game;
import org.grupogjl.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {

    private Game mockGame;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
    }

    @Test
    void testMainExecution() throws IOException, URISyntaxException, FontFormatException, InterruptedException {
        Mockito.mockStatic(Game.class);
        when(Game.getInstance()).thenReturn(mockGame);

        Main.main(new String[]{});

        verify(Game.class);
        Game.getInstance();

        verify(mockGame, times(1)).run();
    }
}
