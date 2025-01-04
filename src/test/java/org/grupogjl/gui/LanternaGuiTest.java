package org.grupogjl.gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LanternaGuiTest {

    private SpriteBuilder spriteBuilder;
    private Screen mockScreen;
    private SpriteBuilder mockSpriteBuilder;
    private LanternaGui lanternaGui;

    @BeforeEach
    void setUp() {
        spriteBuilder = new SpriteBuilder();
        mockScreen = Mockito.mock(Screen.class);
        mockSpriteBuilder = Mockito.mock(SpriteBuilder.class);
        lanternaGui = new LanternaGui(mockScreen);
        lanternaGui.setSpriteBuilder(mockSpriteBuilder);
    }

    @Test
    void testDraw_Image() {
        String filename = "validImage.png";
        float x = 1.0f;
        float y = 1.0f;

        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        when(mockImage.getWidth()).thenReturn(100);
        when(mockImage.getHeight()).thenReturn(100);
        when(mockSpriteBuilder.loadImage(filename)).thenReturn(mockImage);

        lanternaGui.drawImage(x, y, filename);

        for (int i = 0; i < mockImage.getWidth(); i++) {
            for (int j = 0; j < mockImage.getHeight(); j++) {
                if (!lanternaGui.isTransparent(mockImage, i, j)) {
                    Color c = new Color(mockImage.getRGB(i, j));
                    String color = "#" + Integer.toHexString(c.getRGB()).substring(2);
                    verify(lanternaGui).drawPixel((int) Math.floor(x * 16 + i), (int) Math.floor(y * 16 + j), color);
                }
            }
        }
    }

    @Test
    void testGet_NextActionQuit() throws IOException {
        KeyStroke keyStroke = new KeyStroke('q', false, false);
        when(mockScreen.pollInput()).thenReturn(keyStroke);

        GeneralGui.ACTION action = lanternaGui.getNextAction();

        assertEquals(GeneralGui.ACTION.QUIT, action);
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
        when(mockSpriteBuilder.loadImage(anyString())).thenReturn(mockImage);

        lanternaGui.drawMenuText(0, 0, " ", "#FFFFFF");
        verify(mockSpriteBuilder, never()).loadImage(anyString());

        lanternaGui.drawMenuText(0, 0, ".", "#FFFFFF");
        verify(mockSpriteBuilder).loadImage("/Letters/dot.png");

        lanternaGui.drawMenuText(0, 0, ":", "#FFFFFF");
        verify(mockSpriteBuilder).loadImage("/Letters/doubledot.png");

        lanternaGui.drawMenuText(0, 0, "-", "#FFFFFF");
        verify(mockSpriteBuilder).loadImage("/Letters/hifen.png");

        lanternaGui.drawMenuText(0, 0, "!", "#FFFFFF");
        verify(mockSpriteBuilder).loadImage("/Letters/exclamationMark.png");

        lanternaGui.drawMenuText(0, 0, "A", "#FFFFFF");
        verify(mockSpriteBuilder).loadImage("/Letters/A.png");
    }

    @Test
    void testSet_Screen() throws NoSuchFieldException, IllegalAccessException {
        Screen newMockScreen = Mockito.mock(Screen.class);
        lanternaGui.setScreen(newMockScreen);

        Field screenField = LanternaGui.class.getDeclaredField("screen");
        screenField.setAccessible(true);
        Screen actualScreen = (Screen) screenField.get(lanternaGui);

        assertEquals(newMockScreen, actualScreen);
    }

    @Test
    void testGet_NextActionNone() throws IOException {
        when(mockScreen.pollInput()).thenReturn(null);
        assertEquals(GeneralGui.ACTION.NONE, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionEOF() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.EOF));
        assertEquals(GeneralGui.ACTION.QUIT, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionQuitCharacter() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke('q', false, false));
        assertEquals(GeneralGui.ACTION.QUIT, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionArrowUp() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.ArrowUp));
        assertEquals(GeneralGui.ACTION.UP, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionArrowRight() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.ArrowRight));
        assertEquals(GeneralGui.ACTION.RIGHT, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionArrowDown() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.ArrowDown));
        assertEquals(GeneralGui.ACTION.DOWN, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionArrowLeft() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.ArrowLeft));
        assertEquals(GeneralGui.ACTION.LEFT, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionThrowBall() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke('b', false, false));
        assertEquals(GeneralGui.ACTION.THROWBALL, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionSelect() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.Enter));
        assertEquals(GeneralGui.ACTION.SELECT, lanternaGui.getNextAction());
    }

    @Test
    void testGet_NextActionUknown() throws IOException {
        when(mockScreen.pollInput()).thenReturn(new KeyStroke(KeyType.Unknown));
        assertEquals(GeneralGui.ACTION.NONE, lanternaGui.getNextAction());
    }
}
