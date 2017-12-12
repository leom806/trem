package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;

import javax.swing.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import database.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import models.Locomotiva;

@SuppressWarnings({ "serial", "unused" })

/**
 * Classe que estende JFrame e cadastra locomotivas.
 * 
 * @author Antonignoni Cesar
 */
public class CadastroLocomotiva extends JDialog {
        /** Botoes de salvar, limpar, fechar e de criar novo. */
	private JButton bSalvar, bLimpar, bFechar, bNovo;
        /** Campos para input de classe, bitola, comprimento e peso. */
	private JTextField tClasse, tBitola, tComprimento, tPeso;
	/** Descricao da locomotiva. */
        private JTextArea txaDescricao;
	/** Define as fontes para os botoes e para os textos. */
        private Font fBotoes = new Font("Calibri", Font.BOLD, 20), fTexto = new Font("Calibri", Font.BOLD, 18);
	/** Label para a classe, descricao, bitola, peso e comprimento. */
        private JLabel lClasse, lDescricao, lBitola, lPeso, lComprimento;

        /** Construtor da locomotiva. */
	public CadastroLocomotiva() {
		initComp();
	}

        /**
         * Metodo que inicializa o componente.
         */
	public void initComp() {
		setTitle("VLC - Cadastro de Locomotiva"); // titulo da janela

		// paineis para setar a localizacao dos componentes
		JPanel cima = new JPanel();
		JPanel centro = new JPanel();
		JPanel baixo = new JPanel();

		JLabel tit = new JLabel("Locomotiva");// titulo
		tit.setFont(fBotoes); // fonte
		tit.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento do texto
		cima.add(tit);

		// centro
		centro.setBorder(BorderFactory.createTitledBorder("Cadastro de Locomotiva")); // borda

		// formlayout do formulario de cadastro
		FormLayout form = new FormLayout("130dlu, 20dlu, 100dlu, 3dlu", // colunas
				"pref, 5dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu"); // linhas
		CellConstraints cc = new CellConstraints();
		JPanel af = new JPanel();

		centro.setLayout(form);
		centro.add(lClasse = new JLabel("Classe"), cc.xy(1, 1));
		centro.add(tClasse = new JTextField(), cc.xy(1, 3));

		centro.add(lDescricao = new JLabel("Descrição"), cc.xy(1, 5));
		centro.add(txaDescricao = new JTextArea(), cc.xywh(1, 7, 1, 5));

		centro.add(lBitola = new JLabel("Bitola"), cc.xy(3, 1));
		centro.add(tBitola = new JTextField(), cc.xy(3, 3));

		centro.add(lPeso = new JLabel("Peso Máximo"), cc.xy(3, 5));
		centro.add(tPeso = new JTextField(), cc.xy(3, 7));

		centro.add(lComprimento = new JLabel("Comprimento"), cc.xy(3, 9));
		centro.add(tComprimento = new JTextField(), cc.xy(3, 11));

		// adicionando botoes no panel baixo
		baixo.add(bNovo = new JButton("Novo"));// botao novo
		baixo.add(bSalvar = new JButton("Salvar")); // botao de salvar o cadastro
		baixo.add(bLimpar = new JButton("Limpar")); // botao limpar campos
		baixo.add(bFechar = new JButton("Cancelar")); // botao cancelar

		// setando a fonte dos botoes
		bNovo.setFont(fBotoes);
		bSalvar.setFont(fBotoes);
		bLimpar.setFont(fBotoes);
		bFechar.setFont(fBotoes);

		bSalvar.setFocusable(false);
		bLimpar.setFocusable(false);
		bNovo.setFocusable(false);
		bFechar.setFocusable(false);

		// evento dos botoes
		eventoBotoes();

		// panel final
		JPanel tudo = new JPanel();
		tudo.setLayout(new BorderLayout());
		tudo.add(BorderLayout.NORTH, cima);
		tudo.add(BorderLayout.CENTER, centro);
		tudo.add(BorderLayout.SOUTH, baixo);

		// container da classe
		Container cp = getContentPane();
		cp.add(tudo);

		setFocusable(false);
		setAutoRequestFocus(true);
		setAlwaysOnTop(true);
		setSize(500, 300); // tamanho
		setLocationRelativeTo(null); // localizacao
	}

        /**
         * Metodo para acionar acoes aos cliques de botoes.
         */
	public void eventoBotoes() {
		bNovo.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
                            if(tBitola.getText().equals("") || tClasse.getText().equals("") || tComprimento.getText().equals("") || tPeso.getText().equals("") || txaDescricao.getText().equals("")){
                                JOptionPane.showMessageDialog(this, "É necessário preencher todos os campos para prosseguir.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }else{
                                ResultSet set = null;
                                Statement statement = null;
                                Connection conn = DB.getInstance().getConnection();
                                int count = 0;
                                
                                try{
                                    statement = conn.createStatement();
                                    set = statement.executeQuery("SELECT * FROM locomotivas WHERE classe="+tClasse.getText());
                                    
                                    while(set.next()){
                                        count++;
                                    }
                                    
                                    if(count > 0){
                                        JOptionPane.showMessageDialog(this, "Já existe uma locomotiva cadastrada com as informações dadas.", "Warning", JOptionPane.WARNING_MESSAGE);
                                    }else{
                                        Locomotiva loc = new Locomotiva(Integer.parseInt(tClasse.getText()), txaDescricao.getText(), Double.parseDouble(tPeso.getText()), Double.parseDouble(tBitola.getText()), Double.parseDouble(tComprimento.getText()));

                                        loc.save();

                                        tBitola.setText("");
                                        tClasse.setText("");
                                        tComprimento.setText("");
                                        tPeso.setText("");
                                        txaDescricao.setText("");
                                        
                                        JOptionPane.showMessageDialog(this, "Locomotiva adicionada com sucesso.", "Success", JOptionPane.PLAIN_MESSAGE);
                                    }
                                }catch(SQLException ex){
                                    System.err.println("Erro de SQL: "+ex.getMessage());
                                }
                            }
			});
		});

		bSalvar.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
                            if(tBitola.getText().equals("") || tClasse.getText().equals("") || tComprimento.getText().equals("") || tPeso.getText().equals("") || txaDescricao.getText().equals("")){
                                JOptionPane.showMessageDialog(this, "É necessário preencher todos os campos para prosseguir.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }else{
                                ResultSet set = null;
                                Statement statement = null;
                                Connection conn = DB.getInstance().getConnection();
                                int count = 0;
                                
                                try{
                                    statement = conn.createStatement();
                                    set = statement.executeQuery("SELECT * FROM locomotivas WHERE classe="+tClasse.getText());
                                    
                                    while(set.next()){
                                        count++;
                                    }
                                    
                                    if(count > 0){
                                        JOptionPane.showMessageDialog(this, "Já existe uma locomotiva cadastrada com as informações dadas.", "Warning", JOptionPane.WARNING_MESSAGE);
                                    }else{
                                        Locomotiva loc = new Locomotiva(Integer.parseInt(tClasse.getText()), txaDescricao.getText(), Double.parseDouble(tPeso.getText()), Double.parseDouble(tBitola.getText()), Double.parseDouble(tComprimento.getText()));

                                        loc.save();

                                        tBitola.setText("");
                                        tClasse.setText("");
                                        tComprimento.setText("");
                                        tPeso.setText("");
                                        txaDescricao.setText("");
                                        
                                        JOptionPane.showMessageDialog(this, "Locomotiva adicionada com sucesso.", "Success", JOptionPane.PLAIN_MESSAGE);
                                    }
                                }catch(SQLException ex){
                                    System.err.println("Erro de SQL: "+ex.getMessage());
                                }
                            }
                        });
		});
		bLimpar.addActionListener((e) -> { // actionListener do botao limparCampos
			SwingUtilities.invokeLater(() -> {
				tBitola.setText("");
				tClasse.setText("");
				tComprimento.setText("");
				tPeso.setText("");
				txaDescricao.setText("");
			});
		});

		bFechar.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
				dispose();
			});
		});
	}

}
