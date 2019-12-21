package UI;

import mdlaf.utils.MaterialColors;
import java.awt.*;

public class Theme {
    // 단순히 각 컴포넌트의 색깔들을 저장하는 필드들
    // Focused가 들어갔다면 마우스가 해당 컴포넌트 위를 hover하고 있을 경우의 색
    // Selected가 들어간 경우 해당 컴포넌트가 선택된 경우의 색
    public static Color menuButtonBackgroundColor;
    public static Color menuButtonColorBackgroundFocused;
    public static Color backgroundColor;
    public static Color totalLabelBackgroundColor;
    public static Color cashButtonBackgroundColor;
    public static Color cashButtonBackgroundColorFocused;
    public static Color cashButtonForegroundColor;
    public static Color cardButtonBackgroundColor;
    public static Color cardButtonForegroundColor;
    public static Color cardButtonBackgroundFocused;
    public static Color payButtonBackgroundColor;
    public static Color payButtonForegroundColor;
    public static Color payButtonBackgroundColorFocused;
    public static Color selectButtonBackgroundColor;
    public static Color selectButtonBackgroundColorSelected;
    public static Color selectButtonBackgroundColorSelectedFocused;
    public static Color selectButtonBackgroundColorFocused;
    public static Color imageButtonBackground;
    public static Color imageButtonBackgroundFocused;
    public static Color selectButtonForegroundColor;
    public static Color menuButtonForegroundColor;
    public static Color selectButtonForegroundColorSelected;

    Theme() {
        // 기본: 마테리얼 테마
        menuButtonBackgroundColor = MaterialColors.YELLOW_200;
        menuButtonColorBackgroundFocused = MaterialColors.YELLOW_500;
        backgroundColor = MaterialColors.WHITE;
        totalLabelBackgroundColor = MaterialColors.YELLOW_100;
        cashButtonBackgroundColor = MaterialColors.GREEN_400;
        cashButtonBackgroundColorFocused = MaterialColors.GREEN_600;
        cashButtonForegroundColor = MaterialColors.WHITE;
        cardButtonBackgroundColor = MaterialColors.RED_400;
        cardButtonBackgroundFocused = MaterialColors.RED_600;
        cardButtonForegroundColor = MaterialColors.WHITE;
        payButtonBackgroundColor = MaterialColors.PINK_400;
        payButtonBackgroundColorFocused = MaterialColors.PINK_600;
        payButtonForegroundColor = MaterialColors.WHITE;
        selectButtonBackgroundColor = MaterialColors.GRAY_200;
        selectButtonBackgroundColorFocused = MaterialColors.GRAY_400;
        selectButtonBackgroundColorSelected = MaterialColors.BLUE_200;
        selectButtonBackgroundColorSelectedFocused = MaterialColors.BLUE_400;
        imageButtonBackground = MaterialColors.WHITE;
        imageButtonBackgroundFocused = MaterialColors.RED_400;
        selectButtonForegroundColor = MaterialColors.BLACK;
        selectButtonForegroundColorSelected = MaterialColors.BLACK;
        menuButtonForegroundColor = MaterialColors.BLACK;
    }

    // 테마를 크리스마스 테마로 변경
    void setChristmasTheme() {
        menuButtonBackgroundColor = MaterialColors.RED_300;
        menuButtonColorBackgroundFocused = MaterialColors.RED_500;
        selectButtonBackgroundColor = MaterialColors.WHITE;
        selectButtonBackgroundColorFocused = MaterialColors.RED_100;
        selectButtonBackgroundColorSelected = MaterialColors.RED_300;
        selectButtonBackgroundColorSelectedFocused = MaterialColors.RED_500;
        selectButtonForegroundColor = MaterialColors.BLACK;
        selectButtonForegroundColorSelected = MaterialColors.WHITE;
        menuButtonForegroundColor = MaterialColors.WHITE;
        totalLabelBackgroundColor = MaterialColors.RED_100;
        cashButtonBackgroundColor = MaterialColors.PINK_400;
        cashButtonBackgroundColorFocused = MaterialColors.PINK_600;
    }
}
