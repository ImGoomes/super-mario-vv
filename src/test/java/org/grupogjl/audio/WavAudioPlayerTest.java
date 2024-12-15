package org.grupogjl.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WavAudioPlayerTest {

    @Test
    void testPlaySound_ValidFile() {
        assertDoesNotThrow(() -> WavAudioPlayer.playSound("gameSound.wav"));
    }

    @Test
    public void testPlaySound_FileNotFound() {
        assertDoesNotThrow(() -> WavAudioPlayer.playSound("non_existent.wav"));
    }

    @Test
    public void testPlaySound_NullFile() {
        assertDoesNotThrow(() -> WavAudioPlayer.playSound(null));
    }
}