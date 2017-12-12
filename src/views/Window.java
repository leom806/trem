package views;

import helpers.Helper;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

@SuppressWarnings("serial")
/** Classe de Janela que estende JFrame e passa parametros e acoes para ela. */
public class Window extends JFrame {
    /** Construtor padrao da janela. */
    public Window() {
        this("");
    }

    /**
     * Construtor especifico da janela
     * @param title
     *                titulo para a janela
     */
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
