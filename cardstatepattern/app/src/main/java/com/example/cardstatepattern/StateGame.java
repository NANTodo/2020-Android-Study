package com.example.cardstatepattern;

import android.graphics.Rect;
import android.view.MotionEvent;

public class StateGame implements GameState{

    private static StateGame stategame = new StateGame();

    public StateGame(){

    }

    public static StateGame getInstance(){
        return stategame;
    }

    @Override
    public void onTouch(Game game, CardGameView view, MotionEvent event) {
        if(view.m_SelectCard_1 != null && view.m_SelectCard_2 != null)
            return;

        // 카드 뒤집는 처리
        int px = (int)event.getX();
        int py = (int)event.getY();
        for (int y=0; y<2; y++) {
            for (int x = 0; x < 3; x++) {
                // 각 카드의 박스 값을 생성
                Rect box_card = new Rect(100 + x * 270, 600 + y * 400, 100 + x * 270 + 170, 600 + y * 400 + 240);
                // 선택된 카드 뒤집기
                if (box_card.contains(px, py)) {
                    int s = view.sound.playSound();
                    // (x, y)에 위치한 카드가 선택되었다.
                    if (view.m_Shuffle[x][y].m_State != Card.CARD_MATCHED) {// 맞춘 카드는 뒤집을 필요가 없습니다.
                        if (view.m_SelectCard_1 == null) {
                            // 첫 카드를 뒤집는다면
                            view.m_SelectCard_1 = view.m_Shuffle[x][y];
                            view.m_SelectCard_1.m_State = Card.CARD_OPEN;
                        } else {// 이미 첫번째 카드가 뒤집혀 있어 두번째로 뒤집는다면
                            if (view.m_SelectCard_1 != view.m_Shuffle[x][y] && view.m_SelectCard_2 == null) {// 중복 뒤집기 방지
                                view.m_SelectCard_2 = view.m_Shuffle[x][y];
                                view.m_SelectCard_2.m_State = Card.CARD_OPEN;
                            }
                        }
                    }
                }
            }
        }
        if(view.total == 3) {
            view.total = 0;
            game.setState(StateEnd.getInstance());
        }
    }
}