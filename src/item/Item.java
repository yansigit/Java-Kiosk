package item;

import UI.CartPanel;
import UI.MainFrame;
import UI.Theme;
import img.MenuIcon;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Item extends JPanel {
    private String name; // 메뉴 이름 필드
    private int price; // 메뉴 가격 필드
    private JButton imageButton; // 이미지 표시 및 버튼
    private String categoryName; // 카테고리 이름

    // 생성자 (이름, 가격, 이미지의 경로) 입력받음
    public Item(String name, int price, int index) {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // 레이아웃은 BorderLayout으로
        this.name = name; // 이름 필드에 이름 저장
        this.price = price; // 가격 필드에 가격 저장
        this.categoryName = MainFrame.menuCategory.get(index).get(0);

        imageButton = new JButton(getImageIcon()); // icon을 넣은 이미지버튼 생성
        imageButton.setPreferredSize(new Dimension(150,110)); // 이미지 버튼 크기 지정

        // 버튼이 눌리면 자기자신 (아이템)이 카트에 있는지 확인하고
        // 아닐 경우 자기자신을 추가하고 카트를 업데이트한다
        imageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!MainFrame.cartPanel.isInCart(Item.this)) {
                    MainFrame.cartPanel.updateCartFile(Item.this, false);
                }
            }
        });

        // 버튼 색 지정
        imageButton.setBackground(Theme.imageButtonBackground);
        imageButton.addMouseListener(MaterialUIMovement.getMovement(imageButton, Theme.imageButtonBackgroundFocused));

        add(imageButton); // 이미지 버튼 올려주고

        JPanel labelPanel = new JPanel(); // 라벨 표시용 패널 선언
        labelPanel.setLayout(new GridLayout(2,1)); // 세로 두개의 그리드레이아웃

        JLabel nameLabel = new JLabel(this.name); // 상품명 표시 라벨
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 35)); // 폰트 설정

        JLabel priceLabel = new JLabel(Integer.toString(this.price)); // 가격 표시 라벨
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트 설정

        labelPanel.add(nameLabel); // 라벨패널에 상품명 라벨 추가
        labelPanel.add(priceLabel); // 라벨패널에 가격 라벨 추가
        add(labelPanel); // 라벨패널 추가
    }

    // 이미지아이콘 생성
    // 카테고리명과 메뉴이름을 통해 동적으로 아이템을 위한 이미지아이콘 받아옴
    private ImageIcon getImageIcon() {
        /* 메뉴들 이미지 경로 구축해주기 */
        String imagePath = "images/"; // 이미지 기본 경로
        imagePath = imagePath.concat(this.categoryName);

        imagePath = imagePath.concat("/product/"); // product 일때는 작은 이미지로
        imagePath = imagePath.concat(this.name.concat(".jpg")); // 이 프로젝트의 모든 이미지파일은 jpg 확장자임

        return new MenuIcon(imagePath).getIcon(); // 다른 메뉴들은 리사이징
    }

    // 이름 반환
    public String getName() {
        return this.name;
    }

    // 가격 반환
    public int getPrice() {
        return this.price;
    }
}