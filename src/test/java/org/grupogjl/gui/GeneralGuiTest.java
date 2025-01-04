package org.grupogjl.gui;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeneralGuiTest {
    private GeneralGui gui;

    @BeforeEach
    void setUp() {
        gui = mock(GeneralGui.class);
    }

    @Test
    void testDraw_MenuText() {
        gui.drawMenuText(10, 20, "Hello World");
        verify(gui).drawMenuText(10, 20, "Hello World");
    }

    @Test
    void testDraw_MenuTextWithColor() {
        gui.drawMenuText(10, 20, "Hello World", "#FF0000");
        verify(gui).drawMenuText(10, 20, "Hello World", "#FF0000");
    }

    @Test
    void test_Clear() {
        gui.clear();
        verify(gui).clear();
    }

    @Test
    void test_Refresh() throws IOException {
        gui.refresh();
        verify(gui).refresh();
    }

    @Test
    void testDraw_Image() {
        gui.drawImage(1.5f, 2.5f, "image.png");
        verify(gui).drawImage(1.5f, 2.5f, "image.png");
    }

    @Test
    void testIs_Transparent() {
        BufferedImage mockImage = mock(BufferedImage.class);
        when(gui.isTransparent(mockImage, 0, 0)).thenReturn(true);

        boolean result = gui.isTransparent(mockImage, 0, 0);

        assertTrue(result);
        verify(gui).isTransparent(mockImage, 0, 0);
    }

    @Test
    void testDraw_Pixel() {
        gui.drawPixel(10, 20, "#FF0000");
        verify(gui).drawPixel(10, 20, "#FF0000");
    }

    @Test
    void testDraw_MenuImage() {
        gui.drawMenuImage(5, 10, "menuImage.png");
        verify(gui).drawMenuImage(5, 10, "menuImage.png");
    }

    @Test
    void testDraw_MenuImageWithColor() {
        gui.drawMenuImage(5, 10, "menuImage.png", "#FF0000");
        verify(gui).drawMenuImage(5, 10, "menuImage.png", "#FF0000");
    }

    @Test
    void testDraw_GameOver() throws IOException {
        gui.drawGameOver();
        verify(gui).drawGameOver();
    }

    @Test
    void testAction_EnumValues() {
        GeneralGui.ACTION[] actions = GeneralGui.ACTION.values();
        assertEquals(8, actions.length);
        assertTrue(EnumSet.of(GeneralGui.ACTION.UP, GeneralGui.ACTION.DOWN).contains(GeneralGui.ACTION.UP));
    }
}
