package org.grupogjl;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

class MainTest {

    @Test
    void testMain() throws IOException, URISyntaxException, FontFormatException, InterruptedException {
        Game mockGame = mock(Game.class);

        try (MockedStatic<Game> mockedGame = mockStatic(Game.class)) {
            mockedGame.when(Game::getInstance).thenReturn(mockGame);

            Main.main(new String[]{});

            mockedGame.verify(Game::getInstance, times(1));

            verify(mockGame, times(1)).run();
        }
    }
}
