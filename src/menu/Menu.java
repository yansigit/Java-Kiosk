package menu;

import UI.MainFrame;
import item.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Menu extends JPanel {
    private ArrayList<String> menu; // 메뉴를 담는 리스트

    //생성자
    public Menu(int index) {
        menu = MainFrame.menuCategory.get(index); // 메뉴 카테고리에서 인덱스를 통해 메뉴들을 받아옴
        setBorder(new EmptyBorder(10,0,10,0)); // CSS로 따지면 Padding 넣어주기
        setLayout(new GridLayout((menu.size()/2),2,10,10)); // 그리드 레이아웃으로 지정. 메뉴 숫자만큼 row 지정

        setMenu(index); // setMenu 메서드 호출
    }

    // 메뉴를 그려주는 메서드
    private void setMenu(int index) {
        Item itemDummy; // 메뉴 아이템

        // 메뉴 아이템 크기만큼 루프돌려서
        for(int i = 1; i < menu.size(); i++) {
            if(menu.get(i) == null) break; // 요소가 비어있다면 루프 탈출
            String[] sp = menu.get(i).split(","); // , 기준으로 문자열 나누기

            itemDummy = new Item(sp[0], Integer.parseInt(sp[1]), index); // (메뉴 이름, 가격, 카테고리 ID) 넣고 아이템 만들어서

            add(itemDummy); // 추가
        }
    }
}