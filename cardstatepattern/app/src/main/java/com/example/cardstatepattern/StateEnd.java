package com.example.cardstatepattern;

import android.view.MotionEvent;

public class StateEnd implements GameState{
    private static StateEnd stateend = new StateEnd();

    public StateEnd(){

    }

    public static StateEnd getInstance(){
        return stateend;
    }

    @Override
    public void onTouch(Game game, CardGameView view, MotionEvent event) {
        game.setState(StateWait.getInstance());
        view.setCardShuffle();
//            startGame();
        view.m_SelectCard_1 = null;
        view.m_SelectCard_2 = null;

    }
}
