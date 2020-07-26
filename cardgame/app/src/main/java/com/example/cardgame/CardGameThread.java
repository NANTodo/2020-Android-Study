package com.example.cardgame;

import android.view.View;

public class CardGameThread extends Thread{
    CardGameView m_View;
    CardGameThread(CardGameView _View) {
        m_View = _View;
    } // Constructor

    @Override
    public void run() {
        while (true) {
            m_View.checkMatch();
            if (m_View.finish()) {
                break;
            }
        }

    }
}
