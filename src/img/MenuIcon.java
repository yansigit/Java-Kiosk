package img;

import javax.swing.*;
import java.awt.*;

// 이미지 아이콘 관련 처리를 담당하는 클래스
public class MenuIcon {
    private Image resizedImage; // 이미지 필드
    private ImageIcon icon; // 이미지아이콘 필드

    //생성자 (Without resizing)
    public MenuIcon(String path) {
        icon = new ImageIcon(path); //path경로의 이미지로 이미지아이콘 생성
    }

    //리사이징된 이미지를 리턴하는 메서드
    public Image getResizedImage() {
        return resizedImage;
    }

    //이미지아이콘을 리턴하는 메서드
    public ImageIcon getIcon() {
        return icon;
    }
}
