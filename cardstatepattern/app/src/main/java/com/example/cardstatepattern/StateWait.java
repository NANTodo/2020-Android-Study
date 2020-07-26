package com.example.cardstatepattern;

import android.view.MotionEvent;

public class StateWait implements GameState {
    private static StateWait stateWait = new StateWait();

    public StateWait(){

    }

    public static StateWait getInstance(){
        return stateWait;
    }

    @Override
    public void onTouch(Game game, CardGameView view, MotionEvent event) {
        game.setState(StateReady.getInstance());
    }
}
