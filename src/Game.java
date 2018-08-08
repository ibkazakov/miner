import gui.main_window.GameWindow;

public class Game {
    private static GameWindow game;

    public static void main(String[] args) {
        game = new GameWindow(9, 9, 10);
    }
}
