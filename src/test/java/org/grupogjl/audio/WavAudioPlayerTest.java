package org.grupogjl.audio;

import org.grupogjl.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WavAudioPlayerTest {

    @Test
    public void testPlaySound_NonExistentFile() {
        // Verify that non-existent file handling doesn't throw unhandled exception
        assertDoesNotThrow(() -> WavAudioPlayer.playSound("non_existent.wav"));
    }

    @Test
    public void testPlaySound_NullFile() {
        // Verify that null file handling doesn't throw unhandled exception
        assertDoesNotThrow(() -> WavAudioPlayer.playSound(null));
    }
}