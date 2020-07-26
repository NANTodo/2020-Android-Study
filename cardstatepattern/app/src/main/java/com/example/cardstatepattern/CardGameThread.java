package com.example.cardstatepattern;

public class CardGameThread extends Thread {
    CardGameView m_View;
    CardGameThread(CardGameView _View){
        m_View = _View;
    }
    public void run( ){
        while (true)
            m_View.checkMatch( );
    }
}
