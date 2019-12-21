package item;

import UI.CartPanel;
import UI.MainFrame;
import UI.Theme;
import mdlaf.animation.MaterialUIMovement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartItem extends JPanel {
    private String name; // 상품명
    private int price; // 가격
    private int amount = 1; // 수량
    JButton selectButton; // 선택 버튼
    private Boolean selected; // 선택되었는지 여부
    private AmountChanger amountChanger; // 수량변경 패널

    // 장바구니 상품 아이템 (이름, 가격, )
    public CartItem(String name, int price, int amount, Boolean selected) {
        //setLayout(new BorderLayout(0,20));
        setLayout(new GridLayout(1, 5)); // 가로 5개 그리드 레이아웃
        setBorder(new EmptyBorder(10, 10, 10, 10)); // padding 10px
        this.name = name; // 필드에 이름 삽입
        this.price = price; // 필드에 가격 삽입
        this.amount = amount; // 필드에 수량 삽입
        this.selected = selected; // 필드에 선택여부 삽입

        add(new JLabel(this.name)); // 이름 라벨 추가
        add(amountChanger = new AmountChanger()); // 수량 변경해주는 버튼들 삽입
        JLabel priceLabel = new JLabel(Integer.toString(this.price)); // 가격 라벨 선언
        priceLabel.setHorizontalAlignment(JLabel.CENTER); // 중앙정렬
        add(priceLabel); // 가격 라벨 추가

        selectButton = new JButton(); // 선택버튼 생성

        // 선택됬다면
        if (this.selected) {
            // 색 지정
            selectButton.setForeground(Theme.selectButtonForegroundColorSelected);
            selectButton.setBackground(Theme.selectButtonBackgroundColorSelected);
            selectButton.addMouseListener(MaterialUIMovement.getMovement(selectButton, Theme.selectButtonBackgroundColorSelectedFocused));

            // 선택되었다고 텍스트 변경
            selectButton.setText("선택된 상품입니다");
        }
        // 선택되지 않았다면
        else {
            // 색 지정
            selectButton.setForeground(Theme.selectButtonForegroundColor);
            selectButton.setBackground(Theme.selectButtonBackgroundColor);
            selectButton.addMouseListener(MaterialUIMovement.getMovement(selectButton, Theme.selectButtonBackgroundColorFocused));

            // 선택되지 않았을 때 텍스트
            selectButton.setText("선택하려면 클릭");
        }

        // 선택버튼 누르면
        selectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelected(); // 선택 토글
                MainFrame.cartPanel.cartInfoPanel.cartInfoUpdate(); // 카트 정보 업데이트
                MainFrame.cartPanel.updateCartFile(null, false); // 카트 내용 업데이트
            }
        });
        add(selectButton); // 선택버튼 추가
    }

    // 선택되었는지 여부 토글
    public void toggleSelected() {
        this.selected = !this.selected;
    }

    // 선택여부 반환
    public Boolean getSelected() {
        return selected;
    }

    // 이름 반환
    public String getName() {
        return name;
    }

    // 가격 반환
    public int getPrice() {
        return price;
    }

    // 수량 반환
    public int getAmount() {
        return amount;
    }

    // 수량 변경해주는놈
    class AmountChanger extends JPanel {

        //생성자
        AmountChanger() {
            setLayout(new BorderLayout()); // 보더 레이아웃
            JLabel countLabel = new JLabel(Integer.toString(amount)); // 수량 라벨 선언
            countLabel.setPreferredSize(new Dimension(40, 10)); // 크기 맞춰주고
            countLabel.setHorizontalAlignment(JLabel.CENTER); // 중앙정렬
            JButton minusButton = new JButton("-"); // 뺴기 버튼

            // 색상 설정
            minusButton.setBackground(Theme.selectButtonBackgroundColor);
            minusButton.addMouseListener(MaterialUIMovement.getMovement(minusButton, Theme.selectButtonBackgroundColorFocused));

            JButton plusButton = new JButton("+"); // 더하기 버튼

            // 색상 설정
            plusButton.setBackground(Theme.selectButtonBackgroundColor);
            plusButton.addMouseListener(MaterialUIMovement.getMovement(plusButton, Theme.selectButtonBackgroundColorFocused));

            // 뺴기 버튼 클릭하면
            minusButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    amount -= 1; // 수량 낮추고
                    countLabel.setText(Integer.toString(amount)); // 수량 라벨 업데이트
                    MainFrame.cartPanel.cartInfoPanel.cartInfoUpdate(); // 카트 정보 업데이트
                    // 수량이 0 이면 삭제
                    if (amount == 0) {
                        CartPanel.cartItems.remove(CartItem.this);
                    }
                    // 카트내용 업데이트
                    MainFrame.cartPanel.updateCartFile(null, false);
                }
            });

            // 더하기 버튼 클릭하면
            plusButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    amount += 1; // 수량 올리고
                    countLabel.setText(Integer.toString(amount)); // 수량라벨 업데이트
                    MainFrame.cartPanel.cartInfoPanel.cartInfoUpdate(); // 카트정보 업데이트
                    MainFrame.cartPanel.updateCartFile(null, false); // 카트내용 업데이트
                }
            });

            add(minusButton, BorderLayout.WEST); // - 버튼 추가 WEST
            add(countLabel, BorderLayout.CENTER); // 수량라벨 추가 CENTER
            add(plusButton, BorderLayout.EAST); // + 버튼 추가 EAST
        }

    }
}
