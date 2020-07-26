package com.example.cardstatepattern;

import android.view.MotionEvent;

public class StateReady implements GameState {
    private static StateReady ready = new StateReady();
    public StateReady(){

    }

    public static StateReady getInstance(){
        return ready;
    }

    @Override
    public void onTouch(Game game, CardGameView view, MotionEvent event) {
        view.startGame();
        game.setState(StateGame.getInstance());
    }
}