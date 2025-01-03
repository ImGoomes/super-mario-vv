package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.props.FireBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerMarioTest {

    private ControllerMario controllerMario;
    private Level level;
    private Mario mario;
    private Camera camera;
    private List<GameObject> objects;

    @BeforeEach
    void setup() {
        controllerMario = new ControllerMario();
        level = mock(Level.class);
        mario = mock(Mario.class);
        camera = mock(Camera.class);
        objects = new ArrayList<>();

        when(level.getMario()).thenReturn(mario);
        when(level.getCamera()).thenReturn(camera);
        when(level.getObjects()).thenReturn(objects);
    }

    @Test
    void test_Step() {
        controllerMario.step(level, GeneralGui.ACTION.RIGHT, 1000L);

        verify(mario, times(1)).moveRight();
        verify(camera, times(1)).updateCamera(mario);
    }

    @Test
    void testUpdateMarioStatus_ResetIfBelowLevel() {
        when(mario.getY()).thenReturn(1001f);
        when(level.getHeight()).thenReturn(1000);

        controllerMario.updateMarioStatus(level);

        verify(mario, times(1)).reset();
    }

    @Test
    void testUpdateMarioStatus_GrantExtraLifeForCoins() {
        when(mario.getCoins()).thenReturn(10);
        when(mario.getLives()).thenReturn(3);

        controllerMario.updateMarioStatus(level);

        verify(mario, times(1)).setLives(4);
        verify(mario, times(1)).setCoins(0);
    }

    @Test
    void testMoveMario_JumpAction() {
        controllerMario.moveMario(GeneralGui.ACTION.UP, mario, objects);

        verify(mario, times(1)).jump();
    }

    @Test
    void testMoveMario_ThrowFireball() {
        when(mario.isStateFire()).thenReturn(true);

        controllerMario.moveMario(GeneralGui.ACTION.THROWBALL, mario, objects);

        assertEquals(1, objects.size());
        assertTrue(objects.get(0) instanceof FireBall);
    }

    @Test
    void testMoveMario_StopMovingIfNone() {
        when(mario.isJumping()).thenReturn(false);
        when(mario.isFalling()).thenReturn(false);

        controllerMario.moveMario(GeneralGui.ACTION.NONE, mario, objects);

        verify(mario, times(1)).setVx(0);
    }
}

