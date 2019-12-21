package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;

public class MenuSetterFrame extends JDialog {

    JLabel imageFileName; // 이미지 파일을 표시하는 라벨
    JButton imageAdderButton; // 이미지를 추가할 버튼
    String categoryName; // 카테고리 이름
    String menuName; // 메뉴 이름
    String menuPrice; // 메뉴 가격
    ArrayList<String> currentCategories = new ArrayList<>(); // 현재 카테고리 리스트
    MainFrame mainFrame; // 메인 프레임 객체

    MenuSetterFrame(MainFrame mainFrame) {
        setSize(600, 300); // 사이즈는 조금 작게 지정
        setResizable(false); // 리사이즈 불가
        setModal(true); // 모달로 설정
        setTitle("메뉴 추가"); // 창 이름 지정
        this.mainFrame = mainFrame; // 메인프레임 객체 저장

        JPanel adderForm = new JPanel(new GridLayout(5, 1)); // 위쪽 레이아웃 지정
        JPanel aboutImage = new JPanel(new GridLayout(1, 2)); // 이미지 추가 패널 레이아웃 지정

        FileDialog filedialog = new FileDialog(this, "이미지를 선택", FileDialog.LOAD); // 파일 다이얼로그
        filedialog.setSize(300, 200); // 크기 설정
        filedialog.setFile("*.jpg"); // jpg 파일만 가능

        aboutImage.add(imageFileName = new JLabel("")); // 라벨 일단 빈 문자열로 초기화
        aboutImage.add(imageAdderButton = new JButton("메뉴 이미지")); // 이미지 추가 버튼
        // 클릭하면 파일 다이얼로그 띄우고 파일 잡기
        imageAdderButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filedialog.setVisible(true);
                if (filedialog.getFile() == null) { // 파일 안잡았으면 에러 띄움
                    JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
                } else {
                    imageFileName.setText(filedialog.getFile()); // 파일 잡혔으면 라벨 업데이트해 파일이름 보여주기
                }

            }
        });

        // 각 입력필드 선언
        JTextField categoryField = new JTextField("메뉴 카테고리");
        JTextField menuField = new JTextField("메뉴");
        JTextField menuPriceField = new JTextField("메뉴 가격");

        // 하고 넣어주기
        adderForm.add(categoryField);
        adderForm.add(menuField);
        adderForm.add(menuPriceField);
        adderForm.add(aboutImage);

        JButton addButton = new JButton("추가"); // 메뉴 추가버튼
        // 누르면 일단 폼이 valid 한지 검사하고 모두 정상이면 메뉴 추가
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 내용 검사
                // 빠진 양식이 있으면 에러
                if (categoryField.getText().isEmpty() || menuField.getText().isEmpty() || menuPriceField.getText().isEmpty() || imageFileName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "양식을 모두 채워주세요", "경고", JOptionPane.WARNING_MESSAGE);
                }
                // 가격이 숫자가 아니면 에러
                else if(!menuPriceField.getText().matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(null, "가격은 숫자만 허용됩니다", "경고", JOptionPane.WARNING_MESSAGE);
                }
                // 모두 정상이라면
                else {
                    // 입력받은 값들 모아서
                    categoryName = categoryField.getText();
                    menuName = menuField.getText();
                    menuPrice = menuPriceField.getText();

                    // 메뉴 카테고리로부터 현재 존재하는 카테고리들 불러다가 리스트 작성
                    for (ArrayList<String> categoryList : MainFrame.menuCategory) {
                        currentCategories.add(categoryList.get(0));
                    }

                    // 만약 카테고리가 이미 존재한다면
                    if(currentCategories.contains(categoryName)) {
                        int index = currentCategories.indexOf(categoryName); // 해당 카테고리에
                        MainFrame.menuCategory.get(index).add(menuName + "," + menuPrice); // 메뉴 추가
                    }
                    // 카테고리가 없다면
                    else {
                        ArrayList<String> newMenu = new ArrayList<>(); // 새 메뉴 리스트
                        newMenu.add(categoryName); // 카테고리 명 삽입
                        newMenu.add(menuName + "," + menuPrice); // 메뉴 이름, 가격 삽입
                        MainFrame.menuCategory.add(newMenu); // 이 리스트를 통째로 추가
                    }

                    setImageToProject(filedialog); // 이미지도 추가
                    MainFrame.writeMenuFile(); // 메뉴파일 업데이트
                    mainFrame.reset(); // 메인프레임 UI 리셋
                    // 성공 알림창 표시
                    JOptionPane.showMessageDialog(null, "메뉴를 추가하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        adderForm.add(addButton); // 추가버튼 추가
        getContentPane().add(adderForm); // 입력폼 패널 추가
        setVisible(true); // 프레임 보이도록 하기
    }

    // 이미지 추가 메서드
    void setImageToProject(FileDialog fileDialog) {
        Path origin = Paths.get(fileDialog.getDirectory() + fileDialog.getFile()); // 원 이미지 패스
        Path copied = Paths.get("images/" + this.categoryName + "/product/" + this.menuName + ".jpg").toAbsolutePath(); // 복사할 패스

        try {
            if (Files.notExists(copied.getParent())) // 만약 부모 폴더들이 존재하지 않는다면
                Files.createDirectories(copied.getParent()); // 디렉토리 생성해주기
            Files.copy(origin, copied, StandardCopyOption.REPLACE_EXISTING); // 파일 복사
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
