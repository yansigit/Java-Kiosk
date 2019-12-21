package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import menu.Menu;

public class MainFrame extends JFrame {

    public static ArrayList<ArrayList<String>> menuCategory = new ArrayList<>(); // 카테고리 및 메뉴 리스트
    public static CartPanel cartPanel; // 카트 패널 객체를 담음
    private Container contentPane = null; // 컨텐츠팬
    private JPanel menuHolderPane = null; // 메뉴홀더
    private Menu menuPane = null; // 메뉴패널

    private MainFrame() {
        init(); // 초기화
        this.setVisible(true); // 메인 프레임 보이게하기
    }

    private void init() {
        readMenuFile(); // menu.list 읽기

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X버튼 누르면 종료
        setSize(1000, 800); // 창 사이즈 설정
        setResizable(false); // 리사이즈 불가능하게

        createMenuBar(); // 메뉴바 생성 메소드
        setPanel(); // UI init 메소드
    }

    public void reset() {
        contentPane.removeAll(); // 컨텐츠 팬에서 모두 삭제하고
        setPanel(); // 다시 패널 세팅
        // 그리고 다시 그려주기
        contentPane.invalidate();
        contentPane.validate();
        contentPane.repaint();
    }

    // 메뉴바 생성해주는 메서드
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // 메뉴바 선언하고
        JMenu viewMenu = new JMenu("테마"); // 메뉴 추가
        JMenuItem defaultTheme = new JMenuItem("마테리얼 테마"); // 마테리얼 테마로 세팅해주는 메뉴 아이템
        JMenuItem christmasThemeItem = new JMenuItem("크리스마스 테마"); // 크리스마스 테마로 세팅해주는 메뉴 아이템

        JMenu menuAdderMenu = new JMenu("메뉴"); // 메뉴 하나 더 추가
        JMenuItem menuAdderItem = new JMenuItem("추가"); // 메뉴 추가 레이아웃 보여줄 아이템
        JMenuItem refreshItem = new JMenuItem("새로고침"); // 새로고침 해줄 아이템

        // 한글 깨져서 폰트 설정해줌
        viewMenu.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        defaultTheme.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        christmasThemeItem.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        menuAdderMenu.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        menuAdderItem.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        refreshItem.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

        // lambda 써서 new ActionListener 대체 가능! 좀더 연구하기
        defaultTheme.addActionListener(e -> {
            new Theme(); // 기본 테마로 설정
            reset(); // UI 리셋
        });
        christmasThemeItem.addActionListener(e -> {
            new Theme().setChristmasTheme(); // 크리스마스 테마로 설정
            reset(); // UI 리셋
        });
        menuAdderItem.addActionListener(e -> {
            new MenuSetterFrame(this); // 메뉴 추가 프레임 생성
        });
        refreshItem.addActionListener(e -> {
            reset(); // UI 리셋
        });

        // 메뉴 및 아이템들 메뉴바에 추가
        viewMenu.add(defaultTheme);
        viewMenu.add(christmasThemeItem);
        menuAdderMenu.add(menuAdderItem);
        menuAdderMenu.add(refreshItem);
        menuBar.add(menuAdderMenu);
        menuBar.add(viewMenu);

        // 메뉴바 셋
        this.setJMenuBar(menuBar);
    }

    /* private void setCategory(JPanel categoryButtonsPane) {
        int index = 0;
        categoryButtonsPane = new JPanel();
        for(ArrayList<String> list : menuCategory) {
            CategoryButtons btn = new CategoryButtons(list.get(0), index);
            categoryButtonsPane.add(btn);
            index++;
        }
        categoryButtonsPane.setLayout(new GridLayout(1,index));
    } */

    // UI init 해주는 메서드
    private void setPanel() {
        contentPane = getContentPane(); // 메인프레임의 ContentPane 가지고오기
        contentPane.setLayout(new BorderLayout()); // 보더 레이아웃으로 설정
        contentPane.setBackground(Theme.backgroundColor); // 배경색 설정

        // 카테고리 지정 버튼들을 담을 패널
        JPanel categoryButtonsPane = new JPanel();
        categoryButtonsPane.setPreferredSize(new Dimension(0,60)); // 높이 60으로 지정 */

        int index = 0; // 인덱스 0으로 초기화해주고
        // 메뉴카테고리 리스트들만큼 카테고리 버튼 생성해서 카테고리 버튼 패널에 넣어줌
        for(ArrayList<String> list : menuCategory) {
            CategoryButtons btn = new CategoryButtons(list.get(0), index);
            categoryButtonsPane.add(btn);
            index++;
        }
        categoryButtonsPane.setLayout(new GridLayout(1,index)); // 카테고리버튼 패널은 그리드레이아웃

        contentPane.add(categoryButtonsPane, BorderLayout.NORTH); // 카테고리버튼 패널 North에 넣어주기

        menuHolderPane = new JPanel(); // 메뉴 패널을 들고있을 홀더
        menuPane = new Menu(0); // 메뉴 패널
        menuHolderPane.add(menuPane); // 홀더에 메뉴패널 추가

        // 홀더에 ScrollPane 물려서 스크롤 가능하게 만들기
        JScrollPane menuScrollPane = new JScrollPane(menuHolderPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(menuScrollPane, BorderLayout.CENTER); // contentPane에 ScrollPane 추가, 위치는 Center

        cartPanel = new CartPanel(); // cartPanel 카트패널
        contentPane.add(cartPanel, BorderLayout.SOUTH); // contentPane에 카트패널 추가, SOUTH로 위치 설정
    }

    // 카테고리 선택 버튼 클래스. 누르면 해당 카테고리의 메뉴가 표시된다.
    class CategoryButtons extends JButton {

        CategoryButtons(String text, int index) {
            // 버튼 만들고
            super(text);
            // 마우스 리스너 추가
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    menuPane = new Menu(index); // 인덱스를 통해 동적으로 세팅

                    /* 메뉴패널홀더 다시 그리기 */
                    menuHolderPane.removeAll(); // 홀더 안 내용 전부 삭제하고
                    menuHolderPane.add(menuPane); // 홀더에 메뉴패널 추가
                    menuHolderPane.revalidate(); // 그리고 다시
                    menuHolderPane.repaint(); // 그려주기
                }
            });
            this.setForeground(Theme.menuButtonForegroundColor); // 전경색 지정
            this.setBackground(Theme.menuButtonBackgroundColor); // 배경색 지정
            this.addMouseListener(MaterialUIMovement.getMovement(this, Theme.menuButtonColorBackgroundFocused)); // 기본색으로 돌아가지 않게
        }
    }

    // 메뉴파일 쓰는 메서드
    static void writeMenuFile() {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("menu.list")); // 버퍼라이터를 통해 파일 쓰기
            for (ArrayList<String> categoryList : menuCategory) { // 파일카테고리 하나마다
                wr.write("-"); // 첫번째줄은 카테고리명
                for (String menu : categoryList) { // 카테고리 메뉴 하나마다
                    wr.write(menu); // 쓰고
                    wr.newLine(); // 엔터
                }
                wr.write(">END"); // 메뉴 끝
                wr.newLine(); // 엔터
                wr.newLine(); // 엔터
            }
            wr.flush(); // 버퍼 강제출력
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 메뉴 파일을 별도로 만들고 사용자가 편집할 수 있게끔 했다. 메뉴 파일을 읽어들이는 메소드.
    static void readMenuFile() {

        BufferedReader br = null; // 파일 내용을 저장할 버퍼
        try {
            br = new BufferedReader(new FileReader("menu.list")); // menu.list 내용을 읽어주기
        } catch (FileNotFoundException e) { // 에러체킹
            e.printStackTrace(); // 에러가 나면 에러내용 출력
        }

        int head = 0, child = 0; // head = 카테고리 번호, child = 메뉴 번호
        String line = null; // 버퍼에서 읽어들인 문자열 저장할 변수

        for (; ; ) { // 무한루프
            try {
                line = br.readLine(); // 버퍼리더가 한줄씩 읽어 line에 저장
                if (line==null) break; // 읽은게 없다면 루프 탈출
                if (line.equals("")) continue; // 빈 문자열이라면 다음 루프로 넘기기

                if (line.contains(">END")) { // 카테고리가 끝났다
                    head++; // 카테고리 넘버 +1 해주고
                    child = 0; // 메뉴 넘버는 0으로
                } else { // 아니라면
                    if(line.contains("-")) {
                        line = line.substring(1); // 카테고리의 이름이라면 ('-'으로 시작한다면), -를 빼줌.
                        menuCategory.add(new ArrayList<String>()); // 카테고리에 새 ArrayList 자식으로 추가
                    }
                    menuCategory.get(head).add(line); // 메뉴 추가
                    //menuList[head][child] = line; // menuList 필드에 저장
                    child++; // 메뉴 넘버 +1
                }
            } catch (IOException | NullPointerException e) { // 에러가 난다면
                e.printStackTrace(); // 에러 내용 출력하고
                break; // 루프 탈출
            }
        }
    }

    // 메인 메소드
    public static void main(String[] args) {
        /* 개인이 개발중인 라이브러리인 만큼 element에 hover할 경우
        바탕색이 기본색 (회색)으로 되어버리거나
        한글 출력이 기본적으로 깨지는 등 잔잔한 불편한점이 있지만 일단 이쁘니까.... */
        try {
            UIManager.setLookAndFeel (new MaterialLookAndFeel()); // 룩앤필 설정
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace ();
        }
        UIManager.put("Button.font", new Font("맑은 고딕", Font.BOLD, 20));
        UIManager.put("Label.font", new Font("맑은 고딕", Font.BOLD, 20));
        //UIManager.put("Button.mouseHoverEnable", false);
        new Theme();
        MainFrame main = new MainFrame(); // 메인프레임 객체 만들어주기
    }
}
