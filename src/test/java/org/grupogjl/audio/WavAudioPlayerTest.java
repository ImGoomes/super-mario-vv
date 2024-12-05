package org.grupogjl.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.AudioSystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WavAudioPlayerTest {

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

    @Test
    void testPlaySound_ExceptionHandling() throws Exception {
        String validFileName = "testSound.wav";

        Mockito.mockStatic(AudioSystem.class);
        when(AudioSystem.getClip()).thenThrow(new RuntimeException("Mock exception"));

        assertDoesNotThrow(() -> WavAudioPlayer.playSound(validFileName), "The playSound method should not crash even if an exception occurs.");
    }
}