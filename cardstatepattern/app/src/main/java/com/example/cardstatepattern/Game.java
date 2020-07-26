package com.example.cardstatepattern;

import android.view.MotionEvent;

public class Game {
    private GameState state;

    public Game(){
        state = StateReady.getInstance();
    }

    public void setState(GameState state){
        this.state = state;
    }

    public void onTouch(Game game, CardGameView view, MotionEvent event){
        state.onTouch(game, view, event);
    }
}
