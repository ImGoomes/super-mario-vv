package org.grupogjl.audio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class WavAudioPlayerTest {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testPlayInvalidSound() {
        WavAudioPlayer.playSound("non-existent.wav");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        assertFalse(errContent.toString().isEmpty());
    }

    @Test
    public void testPlayValidSound() {
        WavAudioPlayer.playSound("test-sound.wav");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        assertFalse(errContent.toString().isEmpty());
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
        errContent.reset();
    }
}