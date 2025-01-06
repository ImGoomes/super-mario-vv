package org.grupogjl.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class SpriteBuilderTest {

    private SpriteBuilder spriteBuilder;

    @BeforeEach
    void setUp() {
        spriteBuilder = new SpriteBuilder();
    }

    @Test
    void testIs_InCache() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        spriteBuilder.setToCache("testImage.png", mockImage);

        assertTrue(spriteBuilder.isInCache("testImage.png"));
        assertFalse(spriteBuilder.isInCache("nonexistent.png"));
    }

    @Test
    void testSet_ToCache() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);

        spriteBuilder.setToCache("testImage.png", mockImage);

        assertTrue(spriteBuilder.isInCache("testImage.png"));
        assertEquals(mockImage, spriteBuilder.loadImage("testImage.png"));
    }

    @Test
    void testLoad_ImageFromCache() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        spriteBuilder.setToCache("cachedImage.png", mockImage);

        BufferedImage loadedImage = spriteBuilder.loadImage("cachedImage.png");

        assertNotNull(loadedImage);
        assertEquals(mockImage, loadedImage);
    }

    @Test
    void testLoad_ImageResourceNotFound() {
        SpriteBuilder spriteBuilderSpy = Mockito.spy(spriteBuilder);
        Mockito.doReturn(null).when(spriteBuilderSpy).getClass().getResource("/Sprites/nonexistent.png");

        BufferedImage loadedImage = spriteBuilderSpy.loadImage("nonexistent.png");

        assertNull(loadedImage);
        assertFalse(spriteBuilderSpy.isInCache("nonexistent.png"));
    }

    @Test
    void testLoad_ValidImageResource() {
        BufferedImage loadedImage = spriteBuilder.loadImage("coin.png");

        assertNotNull(loadedImage);
        assertTrue(spriteBuilder.isInCache("coin.png"));
    }
}
