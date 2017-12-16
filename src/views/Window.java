package views;

import helpers.Helper;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame {

    public Window() {
        this("");
    }

    public Window(String title) {
        super(title);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Helper.getInstance().onExit();
            }
        });
    }
}
