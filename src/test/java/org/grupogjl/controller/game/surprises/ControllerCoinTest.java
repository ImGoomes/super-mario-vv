package org.grupogjl.controller.game.surprises;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ControllerCoinTest {

    private ControllerCoin controllerCoin;
    private Level level;
    private Mario mario;
    private List<Coin> coins;

    @BeforeEach
    void setup() {
        controllerCoin = new ControllerCoin();
        level = mock(Level.class);
        mario = mock(Mario.class);
        coins = new ArrayList<>();

        when(level.getMario()).thenReturn(mario);
        when(level.getCoins()).thenReturn(coins);
    }

    @Test
    void testUpdateStatus_DeactivatesCoinAndIncrementsMarioCoins() {
        Coin coin = mock(Coin.class);
        when(coin.getActivationTimer()).thenReturn(0);
        when(mario.getCoins()).thenReturn(5);
        coins.add(coin);

        controllerCoin.updateStatus(level, 1000L);

        verify(coin, times(1)).setActivated(false);
        verify(mario, times(1)).setCoins(6);
        verify(coin, never()).setActivationTimer(anyInt());
    }

    @Test
    void testUpdateStatus_DecrementsCoinActivationTimer() {
        Coin coin = mock(Coin.class);
        when(coin.getActivationTimer()).thenReturn(5);
        coins.add(coin);

        controllerCoin.updateStatus(level, 1000L);

        verify(coin, times(1)).setActivationTimer(4);
        verify(coin, never()).setActivated(false);
        verify(mario, never()).setCoins(anyInt());
    }

    @Test
    void testStep_UpdatesCoinStatus() {
        Coin coin = mock(Coin.class);
        when(coin.getActivationTimer()).thenReturn(0);
        when(mario.getCoins()).thenReturn(5);
        coins.add(coin);

        controllerCoin.step(level, GeneralGui.ACTION.NONE, 1000L);

        verify(coin, times(1)).setActivated(false);
        verify(mario, times(1)).setCoins(6);
    }
}
