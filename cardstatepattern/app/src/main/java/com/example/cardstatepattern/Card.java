package com.example.cardstatepattern;

public class Card {
    // 1. 카드 상태 상수 정의
    public static final int CARD_SHOW = 0;
    public static final int CARD_CLOSE = 1;
    public static final int CARD_OPEN = 2;
    public static final int CARD_MATCHED = 0;

    public static final int IMG_RED = 1;
    public static final int IMG_GREEN = 2;
    public static final int IMG_BLUE = 3;

    // 2. 카드 상태 멤버 변수 선언 및 초기화
    public int m_State,
            m_Color;

    public Card(int _Color) {
        m_State = CARD_SHOW;
        m_Color = _Color;
    } // Constructor
}
