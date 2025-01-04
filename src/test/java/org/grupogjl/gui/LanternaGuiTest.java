package org.grupogjl.gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LanternaGuiTest {

    private Screen mockScreen;
    private SpriteBuilder mockSpriteBuilder;
    private LanternaGui lanternaGui;

    @BeforeEach
    void setUp() {
        mockScreen = Mockito.mock(Screen.class);
        mockSpriteBuilder = Mockito.mock(SpriteBuilder.class);
        lanternaGui = new LanternaGui(mockScreen);
        lanternaGui.setSpriteBuilder(mockSpriteBuilder);
    }

    @Test
    void testGet_NextActionQuit() throws IOException {
        KeyStroke keyStroke = new KeyStroke('q', false, false);
        when(mockScreen.pollInput()).thenReturn(keyStroke);

        GeneralGui.ACTION action = lanternaGui.getNextAction();

        assertEquals(GeneralGui.ACTION.QUIT, action);
    }

    @Test
    void testGet_NextActionArrowUp() throws IOException {
        KeyStroke keyStroke = new KeyStroke(KeyType.ArrowUp);
        when(mockScreen.pollInput()).thenReturn(keyStroke);

        GeneralGui.ACTION action = lanternaGui.getNextAction();

        assertEquals(GeneralGui.ACTION.UP, action);
    }

    @Test
    void testDraw_Pixel() {
        TextGraphics mockTextGraphics = Mockito.mock(TextGraphics.class);
        when(mockScreen.newTextGraphics()).thenReturn(mockTextGraphics);

        lanternaGui.drawPixel(10, 20, "#FF0000");

        verify(mockTextGraphics).setForegroundColor(TextColor.Factory.fromString("#FF0000"));
        verify(mockTextGraphics).setBackgroundColor(TextColor.Factory.fromString("#FF0000"));
        verify(mockTextGraphics).putString(10, 20, " ");
    }

    @Test
    void testIs_Transparent() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        when(mockImage.getRGB(0, 0)).thenReturn(0x00FFFFFF);

        boolean isTransparent = lanternaGui.isTransparent(mockImage, 0, 0);

        assertTrue(isTransparent);
    }

    @Test
    void testDraw_GameOver() throws IOException {
        TextGraphics mockTextGraphics = Mockito.mock(TextGraphics.class);
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);

        when(mockScreen.newTextGraphics()).thenReturn(mockTextGraphics);
        when(mockSpriteBuilder.loadImage(anyString())).thenReturn(mockImage);
        when(mockImage.getWidth()).thenReturn(100);
        when(mockImage.getHeight()).thenReturn(100);

        lanternaGui.drawGameOver();

        verify(mockScreen).refresh();
        verify(mockTextGraphics).setBackgroundColor(TextColor.Factory.fromString("#6c9cfc"));
    }

    @Test
    void test_Clear() {
        TextGraphics mockTextGraphics = Mockito.mock(TextGraphics.class);
        when(mockScreen.newTextGraphics()).thenReturn(mockTextGraphics);

        lanternaGui.clear();

        verify(mockTextGraphics).setBackgroundColor(TextColor.Factory.fromString("#6c9cfc"));
        verify(mockTextGraphics).fillRectangle(any(), any(), eq(' '));
    }

    @Test
    void testDraw_MenuText() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        when(mockSpriteBuilder.loadImage("/Letters/A.png")).thenReturn(mockImage);

        lanternaGui.drawMenuText(0, 0, "A");

        verify(mockSpriteBuilder).loadImage("/Letters/A.png");
    }
}
