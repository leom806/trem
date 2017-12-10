package views;

import java.awt.Container;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import database.DB;

import java.sql.SQLException;

@SuppressWarnings({"serial", "unused"})
public class Login extends Window {

    private JButton bLogin, bCadastro;
    private JTextField tUsuario;
    private JPasswordField pSenha;
    private Font fBotoes = new Font("Calibri", Font.BOLD, 16);
    private DB database = DB.getInstance();
    private static Menu menu;

    public Login() {
        super("VLC - Login");

        try {
            inicializarBD();
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar estabelecer a conexão com o Banco de Dados.\n" + e.getMessage());
            System.exit(1);
        }

        if (!podeRodar()) {
            JOptionPane.showMessageDialog(this, "VALOCO já está em execução.");
            System.exit(0);
        }

        menu();
    }

    private boolean podeRodar() {
        File f = new File(".lock");
        if (f.exists()) {
            return false;
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void inicializarBD() throws SQLException {        
        database.getConnection();
        database.createTables();
    }

    private void menu() {

        Font f = new Font("Arial", Font.BOLD, 18);

        JLabel lUsuario = new JLabel("Usuário:");
        lUsuario.setFont(f);

        JLabel lSenha = new JLabel("Senha:");
        lSenha.setFont(f);

        JLabel msg = new JLabel("Bem vindo!");
        msg.setFont(new Font("Arial", Font.BOLD, 22));
        msg.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel msg2 = new JLabel("Por favor, digite o seu usuário e senha.");
        msg2.setFont(new Font("Arial", Font.BOLD, 16));
        msg2.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel formulario = new JPanel();

        // gerenciador de layout
        FormLayout form = new FormLayout("33dlu:grow, 1dlu, 33dlu:grow, 1dlu ", // colunas
                "pref, 3dlu, pref, 10dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref");// linhas
        formulario.setLayout(form);
        CellConstraints cc = new CellConstraints();

        // primeira linha
        formulario.add(msg, cc.xyw(1, 1, 3));
        formulario.add(msg2, cc.xyw(1, 3, 3));

        // segunda linha
        formulario.add(lUsuario, cc.xyw(1, 5, 3));
        formulario.add(tUsuario = new JTextField(), cc.xyw(1, 7, 3));

        // terceira linha
        formulario.add(lSenha, cc.xyw(1, 9, 3));
        formulario.add(pSenha = new JPasswordField(), cc.xyw(1, 11, 3));

        // quarta linha
        formulario.add(bLogin = new JButton("Entrar"), cc.xy(1, 13));
        formulario.add(bCadastro = new JButton("Cadastrar"), cc.xy(3, 13));

        bLogin.setFont(fBotoes);
        bCadastro.setFont(fBotoes);
        bLogin.setFocusable(false);
        bCadastro.setFocusable(false);

        eventoLogin();

        Container cp = getContentPane();
        cp.add(formulario);

        setSize(400, 235);
        setLocationRelativeTo(null);
    }

    private void eventoLogin() {
        bLogin.addActionListener((e) -> {
            menu = Menu.getInstance(); // Cria o menu
            menu.setVisible(true);
            dispose();
        });

        bCadastro.addActionListener((e) -> {
            CadastroUsuario cadUser = new CadastroUsuario();
            cadUser.setVisible(true);

        });
    }

    public static void reload(){
        Menu.deleteInstance();
        menu.dispose();
        menu = Menu.getInstance();
        menu.setVisible(true);
    }
    
}
