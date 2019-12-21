package UI;

import item.CartItem;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaymentFrame extends JDialog {
    JPanel paymentWrapperPane; // 영수증 감싸는 패널
    JPanel successPane; // 결제 성공 시 보여주는 패널
    Container contentPane; // 컨텐츠팬
    CardLayout cardLayout; // 카드레이아웃. 화면 전환에 필요

    PaymentFrame() {
        setTitle("결제창"); // 창 이름 지정
        setModal(true); // 모달로 지정
        setLocationRelativeTo(null); // 스크린 중앙에 나타남

        contentPane = getContentPane(); // 컨텐츠팬
        contentPane.setLayout(new CardLayout()); // 카드 레이아웃으로 지정

        // UI들 지정해주기
        setPaymentUI();
        setSuccessUI();

        cardLayout = (CardLayout) contentPane.getLayout(); // 컨텐츠 팬에서 카드 레이아웃 받아옴 (위에서 직접 변수로 안가져온 이유는 그냥.. 이렇게도 해보고 싶어서..)

        // 리사이즈 불가, 창 보이기
        setResizable(false);
        setVisible(true);
    }

    // 요소들의 수에 따라 창 높이 동적으로 지정해줌
    void setDialogHeight(int selectedListNum) {
        this.setSize(400, (selectedListNum + 2) * 45);
    }

    // 결제가 성공해서 결제 완료됬다고 알려줌
    void setSuccessUI() {
        successPane = new JPanel(new GridLayout(2, 1)); // 레이아웃 지정해주고
        successPane.setBackground(Theme.totalLabelBackgroundColor); // 색깔도 지정
        JLabel thanksLabel = new JLabel("결제 완료되었습니다"); // 결제완료 라벨
        thanksLabel.setHorizontalAlignment(JLabel.CENTER); // 중앙정렬해주고
        successPane.add(thanksLabel); // 추가
        JLabel thanksLabel2 = new JLabel("이용해 주셔서 감사합니다"); // 라벨
        thanksLabel2.setHorizontalAlignment(JLabel.CENTER); // 중앙정렬
        successPane.add(thanksLabel2); // 추가

        contentPane.add(successPane, "thank_you"); // 마지막으로 패널 추가
    }

    // 결제창 UI
    void setPaymentUI() {
        paymentWrapperPane = new JPanel(); // 래퍼 패널 선언
        // 수량, 가격 등 0으로 초기화
        int selectedListNum = 0; // 창 높이 설정에 사용
        int totalPrice = 0;
        int totalAmount = 0;

        for (CartItem item : CartPanel.cartItems) { // 카트패널 요소마다
            if (item.getSelected()) { // 선택되었다면
                paymentWrapperPane.add(new PaymentListPane(item.getName(), item.getPrice() * item.getAmount(), item.getAmount(), false)); // 결제창 리스트에 추가해 보여주기
                totalAmount += item.getAmount(); // 총 수량 업데이트
                totalPrice += item.getPrice() * item.getAmount(); // 총 가격 업데이트
                selectedListNum++; // 선택된 리스트 수 더해주기
            }
        }

        paymentWrapperPane.add(new PaymentListPane("결제금액", totalPrice, totalAmount, true)); // 결제금액 알려주는 패널 추가

        JPanel whichPayment = new JPanel(new GridLayout(1, 2)); // 레이아웃 세팅

        JButton cashButton = new PaymentButton(false); // 현금결제
        whichPayment.add(cashButton); // 추가
        JButton cardButton = new PaymentButton(true); // 카드결제
        whichPayment.add(cardButton); // 추가

        paymentWrapperPane.setLayout(new GridLayout(selectedListNum + 2, 1)); // 레이아웃 업데이트 (그리드 레이아웃 동적으로)
        paymentWrapperPane.add(whichPayment); // 결제 버튼들 추가

        contentPane.add(paymentWrapperPane, "payment"); // 래퍼 패널 컨텐츠팬에 추가
        setDialogHeight(selectedListNum); // 동적 프레임 높이 세팅
    }

    // 버튼들 템플릿
    class PaymentButton extends JButton {
        Boolean isCard; // 카드인지 현금인지 여부

        PaymentButton(Boolean isCard) {
            this.isCard = isCard; // 파라미터 필드에 넣기
            String text = "현금"; // 기본은 현금으로
            // 색상 설정
            Color color = Theme.cashButtonForegroundColor;
            Color backColor = Theme.cashButtonBackgroundColor;
            if (this.isCard) { // 카드라면
                text = "카드"; // 카드로 변경
                // 색상도 변경
                color = Theme.cardButtonForegroundColor;
                backColor = Theme.cardButtonBackgroundColor;
            }
            // 위에서 세팅된 값들 실제로 넣어주기
            setText(text);
            setBackground(backColor);
            setForeground(color);
            addMouseListener(MaterialUIMovement.getMovement(this, backColor));

            // 클릭하면 결제성공창 띄우고 카트 비워주기
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(contentPane, "thank_you");
                    MainFrame.cartPanel.updateCartFile(null, true);
                }
            });
        }
    }

    // 결제 리스트 패널
    class PaymentListPane extends JPanel {
        PaymentListPane(String productName, int price, int amount, Boolean isTotal) {
            setLayout(new GridLayout(1, 3)); // 레이아웃 설정

            // 라벨들
            JLabel nameLabel = new JLabel(productName); // 품목명
            JLabel priceLabel = new JLabel(Integer.toString(price) + "원"); // 가격
            JLabel amountLabel = new JLabel(Integer.toString(amount) + "개"); // 수량

            // 결제 금액용
            if (isTotal) {
                // 색깔 지정
                nameLabel.setBackground(Theme.totalLabelBackgroundColor);
                priceLabel.setBackground(Theme.totalLabelBackgroundColor);
                amountLabel.setBackground(Theme.totalLabelBackgroundColor);
                amountLabel.setText(""); // 결제 금액용에서는 사용안함
            }

            // 중앙정렬
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            priceLabel.setHorizontalAlignment(JLabel.CENTER);
            amountLabel.setHorizontalAlignment(JLabel.CENTER);

            // 추가
            add(nameLabel);
            add(amountLabel);
            add(priceLabel);
        }
    }
}
