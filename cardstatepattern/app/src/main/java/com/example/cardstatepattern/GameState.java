package com.example.cardstatepattern;

import android.view.MotionEvent;

public interface GameState {
    public void onTouch(Game game, CardGameView view, MotionEvent event);
}
