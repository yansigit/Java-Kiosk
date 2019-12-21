package UI;

import item.CartItem;
import item.Item;
import mdlaf.animation.MaterialUIMovement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class CartPanel extends JPanel {
    // 장바구니 리스트
    public static ArrayList<CartItem> cartItems = new ArrayList<>();
    private JPanel cartListPanelHolderPane; // 카트 리스트 패널을 감싸는 패널. 다시 그릴때 용이하도록
    public CartInfoPanel cartInfoPanel; // 카트 실시간 가격 및 수량 등 보여주는 패널

    //생성자
    CartPanel() {
        readCartFile(); // 카트 파일 읽기

        setLayout(new BorderLayout()); // 보더 레이아웃으로 지정
        cartInfoPanel = new CartInfoPanel();
        add(cartInfoPanel, BorderLayout.NORTH); // 카트정보 (가격 등) 표시하는 패널 추가

        cartListPanelHolderPane = new JPanel();
        cartListPanelHolderPane.setLayout(new GridLayout(1, 1));
        cartListUpdate();

        // 스크롤 가능하도록 JScrollPane에다 물려주기
        JScrollPane cartScrollPane = new JScrollPane(cartListPanelHolderPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤 패널 1 (menuScrollPane) 선언, tabbedPane 물려주기
        cartScrollPane.setPreferredSize(new Dimension(0, 300)); // 높이 30으로 설정하고
        add(cartScrollPane, BorderLayout.CENTER); // 스크롤패널 추가

        JPanel checkOutPane = new JPanel(new GridLayout(1, 1)); // 결제버튼 홀딩용 패널
        JButton checkoutButton = new JButton("결제"); // 결제 버튼

        // 결제버튼 색깔 설정
        checkoutButton.setBackground(Theme.payButtonBackgroundColor);
        checkoutButton.setForeground(Theme.payButtonForegroundColor);
        checkoutButton.addMouseListener(MaterialUIMovement.getMovement(checkoutButton, Theme.payButtonBackgroundColorFocused));

        // 결제버튼 마우스 클릭하면 결제창 뜨도록 이벤트 설정
        checkoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new PaymentFrame();
            }
        });

        checkOutPane.add(checkoutButton); // 결제버튼 추가 (더미)
        checkOutPane.setPreferredSize(new Dimension(0, 60)); // 높이 60으로 설정
        add(checkOutPane, BorderLayout.SOUTH); // 결제버튼패널 추가
    }

    // 아이템이 카트에 들어있는지를 검사
    public Boolean isInCart(Item item) {
        for(CartItem cartItem : cartItems) { // 카트아이템 리스트 요소마다
            if(cartItem.getName().equals(item.getName())) { // 해당 아이템과 이름이 같은지 비교
                return true; // 참이라면 true 리턴
            }
        }
        return false; // 아니면 false
    }

    // 카트 리스트 업데이트 메소드
    void cartListUpdate() {
        readCartFile(); // 카트파일 다시 읽고

        // 카트리스트홀더 재구성
        cartListPanelHolderPane.removeAll();
        cartListPanelHolderPane.add(new CartListPanel());
        cartListPanelHolderPane.revalidate();
        cartListPanelHolderPane.repaint();

        // 카트정보 패널 업데이트
        cartInfoPanel.cartInfoUpdate();
    }

    // 카트에 담긴 물건들 읽어주는 파일
    // 나중에 writeCartFile() 메서드를 통해 카트에 담긴 목록을 저장도 할수있게 할 것.
    private void readCartFile() {

        BufferedReader br = null; // 파일 내용을 저장할 버퍼
        try {
            br = new BufferedReader(new FileReader("cart.list")); // cart.list 내용을 읽어주기
        } catch (FileNotFoundException e) { // 에러체킹
            e.printStackTrace(); // 에러가 나면 에러내용 출력
        }

        String line = null; // 버퍼에서 읽어들인 문자열 저장할 변수

        // 카트가 비어있지 않으면 비워줌
        if (!cartItems.isEmpty())
            cartItems.clear();

        for (; ; ) { // 무한루프
            try {
                line = br.readLine(); // 버퍼리더가 한줄씩 읽어 line에 저장
                if (line == null) break; // 읽은게 없다면 루프 탈출
                if (line.equals("")) continue; // 빈 문자열이라면 다음 루프로 넘기기

                String[] cart = line.split(","); // , 문자를 기준으로 나누기
                cartItems.add(new CartItem(cart[0], Integer.parseInt(cart[1]), Integer.parseInt(cart[2]), Boolean.parseBoolean(cart[3]))); // Arraylist에 카트아이템 내용 저장
            } catch (IOException | NullPointerException e) { // 에러가 난다면
                e.printStackTrace(); // 에러 내용 출력하고
                break; // 루프 탈출
            }
        }
    }

    // 카트파일 쓰거나 초기화하는 메서드 (업데이트할 아이템, 초기화 여부)
    // item이 Null이면 모든 카트 아이템 내용을 파일에 씀
    public void updateCartFile(Item item, Boolean purge) {
        BufferedWriter wr = null; // 버퍼 라이터 초기화하고
        try {
            // 초기화한다면
            if(purge) {
                wr = new BufferedWriter(new FileWriter("cart.list")); // 카트파일 열고
                wr.write(""); // 빈 문자열 삽입
            }
            else if (item == null) { // item이 null이면
                wr = new BufferedWriter(new FileWriter("cart.list")); // 카트파일 열고
                for(CartItem cartItem:cartItems) { // 카트아이템 리스트의 모든 요소를
                    wr.write(cartItem.getName() + "," + cartItem.getPrice() + "," + cartItem.getAmount() + "," + cartItem.getSelected()); // 카트 파일에 정리해서 쓰기
                    wr.newLine(); // 엔터
                }
            } else { // 그냥 단순 추가일 경우
                wr = new BufferedWriter(new FileWriter("cart.list", true)); // 파일을 append 모드로 열고
                wr.write(item.getName() + "," + item.getPrice() + "," + 1 + "," + false); // 해당 아이템의 정보를 정리해서 쓰기
                wr.newLine(); // 엔터
            }

            wr.flush(); // 버퍼 강제 출력
        } catch (IOException e) { // 에러체킹
            e.printStackTrace(); // 에러가 나면 에러내용 출력
        }

        cartListUpdate(); // 그리고 카트리스트 업데이트 해줌
    }

    // 카트 정보패널
    public class CartInfoPanel extends JPanel {
        JLabel countLabel; // 아이템이 얼마나 있는지
        JLabel wonLabel; // 아이템 가격

        // 생성자
        CartInfoPanel() {
            setLayout(new GridLayout(1, 6)); // 가로 6개짜리 그리드 레이아웃
            setPreferredSize(new Dimension(0, 60)); // 높이는 60

            /* 여기서 버튼들은 사실상 Summary Title 역할 */

            add(new InfoButton("메뉴"));

            add(new InfoButton("수량(개)")); // 버튼추가 - 수량
            countLabel = new JLabel("777"); // 수량 라벨
            countLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬해주고
            add(countLabel); // 추가

            add(new InfoButton("금액(원)")); // 버튼추가 - 금액
            wonLabel = new JLabel("777");// 금액 라벨
            wonLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬해주고
            add(wonLabel); // 추가

            InfoButton emptyCartButton = new InfoButton("취소"); // 취소버튼
            // 취소버튼 클릭하면 장바구니 비워줌
            emptyCartButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("clicked");
                    updateCartFile(null, true);
                }
            });
            add(emptyCartButton); // 버튼추가 - 취소
            cartInfoUpdate(); // 카트정보패널 업데이트
        }

        // 카트정보패널 업데이트 메서드
        public void cartInfoUpdate() {
            int totalPrice = 0; // 총 가격 0으로 초기화
            int totalAmount = 0; // 총 수량 0으로 초기화
            for (CartItem cartItem : CartPanel.cartItems) { // 카트에 있는 아이템마다
                if(cartItem.getSelected()) { // 선택됬다면
                    totalPrice += cartItem.getPrice() * cartItem.getAmount(); // 총 가격에 합산
                    totalAmount += cartItem.getAmount(); // 총 수량에 합산
                }
            }
            // 해당 정보 라벨들 업데이트
            wonLabel.setText(Integer.toString(totalPrice));
            countLabel.setText(Integer.toString(totalAmount));
        }

        // 그냥 정보패널에 들어가는 버튼 템플릿
        class InfoButton extends JButton {
            InfoButton(String text) {
                super(text); // 문자열 지정해주고
                // 색깔 지정
                this.setBackground(Theme.selectButtonBackgroundColor);
                this.addMouseListener(MaterialUIMovement.getMovement(this, Theme.selectButtonBackgroundColorFocused));
            }
        }
    }

    // 카트 리스트 패널
    class CartListPanel extends JPanel {
        // 생성자
        CartListPanel() {
            // 동적으로 그리드 레이아웃 길이 할당
            int cartSize = cartItems.size();
            // 4개 이하면 보기좋으라고 4줄로 해주기
            if (cartSize < 4) cartSize = 4;
            setLayout(new GridLayout(cartSize, 1));
            // 카트아이템 출력 메서드 호출 (페이지 1)
            showCartItems(1);
        }

        // 카트아이템 출력 메서드. page = 출력할 페이지
        void showCartItems(int page) {
            int startnum = (page - 1) * 4; // 페이지 처음 element index 설정

            for (int i = startnum; i < cartItems.size(); i++) {
                add(cartItems.get(i)); // 카트아이템 추가
            }
        }
    }
}