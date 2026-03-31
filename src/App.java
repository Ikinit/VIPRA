import engine.MainEngine;
import graphics.MainGUI;

public class App {
    public static void main(String[] args) {
        MainEngine engine = new MainEngine();
        MainGUI gui = new MainGUI(engine);
        engine.setGUI(gui);
    }
}