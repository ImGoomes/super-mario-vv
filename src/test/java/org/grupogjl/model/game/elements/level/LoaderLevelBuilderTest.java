package org.grupogjl.model.game.elements.level;

import org.grupogjl.model.game.elements.blocks.*;
import org.grupogjl.model.game.elements.enemies.Goomba;
import org.grupogjl.model.game.elements.enemies.KoopaTroopa;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.surprises.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoaderLevelBuilderTest {
    private LoaderLevelBuilder levelBuilder;
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() throws IOException {
        String levelData = """
                ######
                $?????
                +p g u
                """;
        bufferedReader = new BufferedReader(new StringReader(levelData));
        levelBuilder = spy(new LoaderLevelBuilder(1));
        doReturn(levelBuilder.readLines(bufferedReader)).when(levelBuilder).readLines(any());
    }

    @Test
    void testRead_Lines() throws IOException {
        List<String> lines = levelBuilder.readLines(bufferedReader);

        assertEquals(3, lines.size());
        assertEquals("######", lines.get(0));
        assertEquals("$?????", lines.get(1));
        assertEquals("+p g u", lines.get(2));
    }

    @Test
    void testGet_Width() {
        int width = levelBuilder.getWidth();

        assertEquals(212, width);
    }

    @Test
    void testGet_Height() {
        int height = levelBuilder.getHeight();

        assertEquals(13, height);
    }

    @Test
    void testCreate_GameObjects() {
        List<GameObject> objects = levelBuilder.createGameObjects();

        assertEquals(562, objects.size());
        assertTrue(objects.stream().anyMatch(o -> o instanceof UnbreakableBlock));
        assertTrue(objects.stream().anyMatch(o -> o instanceof DestroyableBlock));
        assertTrue(objects.stream().anyMatch(o -> o instanceof GoalBlock));
        assertTrue(objects.stream().anyMatch(o -> o instanceof KoopaTroopa));
        assertTrue(objects.stream().anyMatch(o -> o instanceof Goomba));
        assertTrue(objects.stream().anyMatch(o -> o instanceof Pipe));
        assertTrue(objects.stream().anyMatch(o -> o instanceof SurpriseBlock));
    }

    @Test
    void testSurprise_BlockRandomization() {
        List<GameObject> objects = levelBuilder.createGameObjects();

        long surpriseBlockCount = objects.stream().filter(o -> o instanceof SurpriseBlock).count();
        assertEquals(13, surpriseBlockCount);

        for (GameObject object : objects) {
            if (object instanceof SurpriseBlock surpriseBlock) {
                assertNotNull(surpriseBlock.getSurprise());
                assertTrue(
                        surpriseBlock.getSurprise() instanceof Coin
                                || surpriseBlock.getSurprise() instanceof Mushroom1UP
                                || surpriseBlock.getSurprise() instanceof MushroomSuper
                                || surpriseBlock.getSurprise() instanceof Flower
                                || surpriseBlock.getSurprise() instanceof Star
                );
            }
        }
    }

    @Test
    void testConnect_Pipes() {
        List<GameObject> objects = levelBuilder.createGameObjects();
        List<GameObject> connectedObjects = levelBuilder.connectPipes(objects);

        long pipeCount = connectedObjects.stream().filter(o -> o instanceof Pipe).count();
        assertEquals(6, pipeCount); // Level data contains one 'u'

        for (GameObject object : connectedObjects) {
            if (object instanceof Pipe pipe) {
                assertNotNull(pipe.getConection());
            }
        }
    }
}
