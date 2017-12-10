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

public class CadastroLocomotiva extends JDialog {

	private JButton bSalvar, bLimpar, bFechar, bNovo; // bot�o do formul�rio de cadastro de locomotiva

	private JTextField tClasse, tBitola, tComprimento, tPeso; // text are do formul�rio de cadastro
	private JTextArea txaDescricao;
	private Font fBotoes = new Font("Calibri", Font.BOLD, 20), fTexto = new Font("Calibri", Font.BOLD, 18); // fontes
	private JLabel lClasse, lDescricao, lBitola, lPeso, lComprimento;

	public CadastroLocomotiva() { // construtor
		initComp();
	}

	public void initComp() {
		setTitle("VLC - Cadastro de Locomotiva"); // titulo da janela

		// paineis para setar a localiza��o dos componentes
		JPanel cima = new JPanel();
		JPanel centro = new JPanel();
		JPanel baixo = new JPanel();

		JLabel tit = new JLabel("Locomotiva");// titulo
		tit.setFont(fBotoes); // fonte
		tit.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento do texto
		cima.add(tit);

		// centro
		centro.setBorder(BorderFactory.createTitledBorder("Cadastro de Locomotiva")); // borda

		// formlayout do formul�rio de cadastro
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
		baixo.add(bNovo = new JButton("Novo"));// bot�o novo
		baixo.add(bSalvar = new JButton("Salvar")); // bot�o de salvar o cadastro
		baixo.add(bLimpar = new JButton("Limpar")); // bot�o limpar campos
		baixo.add(bFechar = new JButton("Cancelar")); // bot�o cancelar

		// setando a fonte dos bot�es
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
		setLocationRelativeTo(null); // localizac�o
	}

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
		bLimpar.addActionListener((e) -> { // actionListener do bot�o limparCampos
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
