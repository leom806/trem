package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;

import javax.swing.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings({ "serial", "unused" }) // tirando os avisos
public class CadastroUsuario extends JDialog {

	private Font fFormulario = new Font("Calibri", Font.PLAIN, 16), fBotoes = new Font("Calibri", Font.BOLD, 16);;

	private JButton bCadastrar, bCancelar;
	private JTextField tNome, tNumeroFuncionario, tRg, tSetor, tCargo, tData, tUsuario;
	private JPasswordField pSenha;
	private JLabel lNome, lNumeroFuncionario, lRg, lSetor, lCargo, lData, lUsuario, lSenha;

	public CadastroUsuario() {

		initComp();
	}

	public void initComp() {
		setTitle("Cadastro de usu�rio");

		JPanel panel1 = new JPanel();
		// panel1.setBorder(BorderFactory.createTitledBorder("Cadastro de usu�rio"));
		panel1.setLayout(new BorderLayout());

		JPanel cad = new JPanel();// panel com o formul�rio de cadastro
		cad.setBorder(BorderFactory.createTitledBorder("Cadastro de usu�rio"));// borda do panel formul�rio

		FormLayout form = new FormLayout("2dlu, 2dlu, 120dlu, 15dlu, 120dlu, 3dlu", // colunas
				"3dlu, 3dlu, pref, 5dlu,pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 3dlu,"); // linhas

		cad.setLayout(form);

		CellConstraints cc = new CellConstraints();

		cad.add(lNome = new JLabel("Nome:"), cc.xyw(3, 3, 3));
		cad.add(tNome = new JTextField(), cc.xyw(3, 5, 3));

		cad.add(lRg = new JLabel("Rg:"), cc.xy(3, 7));
		cad.add(tRg = new JTextField(), cc.xy(3, 9));

		cad.add(lNumeroFuncionario = new JLabel("N�mero do funcion�rio:"), cc.xy(5, 7));
		cad.add(tNumeroFuncionario = new JTextField(), cc.xy(5, 9));

		cad.add(lSetor = new JLabel("Setor:"), cc.xy(3, 11));
		cad.add(tSetor = new JTextField(), cc.xy(3, 13));

		cad.add(lCargo = new JLabel("Cargo:"), cc.xy(5, 11));
		cad.add(tCargo = new JTextField(), cc.xy(5, 13));

		cad.add(lUsuario = new JLabel("Usu�rio:"), cc.xy(3, 15));
		cad.add(tUsuario = new JTextField(), cc.xy(3, 17));

		cad.add(lSenha = new JLabel("Senha:"), cc.xy(5, 15));
		cad.add(pSenha = new JPasswordField(), cc.xy(5, 17));

		JPanel pBotoesU = new JPanel();
		pBotoesU.add(bCadastrar = new JButton("Cadastrar"));
		pBotoesU.add(bCancelar = new JButton("Cancelar"));

		//colocando font nos botoes
		bCadastrar.setFont(fBotoes);
		bCancelar.setFont(fBotoes);

		eventoUsuario();// chama o m�todo com os eventos dos bot�es

		panel1.add(BorderLayout.CENTER, cad);// adicionando o panel do formul�rio
		panel1.add(BorderLayout.SOUTH, pBotoesU); // adicionando os bot�es

		Container cp = getContentPane();
		cp.add(panel1);

		setSize(500, 330);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void eventoUsuario() {
		bCadastrar.addActionListener((e) -> {
			// evento do bot�o
		});

		bCancelar.addActionListener((e) -> {
			dispose();
		});

	}

}
