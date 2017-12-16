package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import database.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

@SuppressWarnings({ "serial", "unused" }) // tirando os avisos

public class RemoveLocomotiva extends JFrame{

	private Font fFormulario = new Font("Calibri", Font.BOLD, 16);
	private JLabel lClasse, lInfo;
	private JButton bRemove, bCancelar;
	private JTextField tClasse;

	public RemoveLocomotiva() {
		initComp();
	}

	public void initComp() {
		setTitle("Remoção de Vagão");

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());

		JPanel form = new JPanel();
		form.setBorder(BorderFactory.createTitledBorder(""));

		FormLayout informacoes = new FormLayout("3dlu, 3dlu, pref:grow, 3dlu", // colunas
				"10dlu, 10dlu, pref, 3dlu, 15dlu, 5dlu, pref"); // linhas

		CellConstraints cc = new CellConstraints();

		form.setLayout(informacoes);

		form.add(lClasse = new JLabel("Classe:"), cc.xy(3, 3));
		form.add(tClasse = new JTextField(), cc.xy(3, 5));
		form.add(lInfo = new JLabel("Digite a classe para a verificação."), cc.xy(3, 7));

		JPanel botoes = new JPanel();
		botoes.add(bRemove = new JButton("Remover"));
		botoes.add(bCancelar = new JButton("Cancelar"));

		// fonte
		lClasse.setFont(fFormulario);
		lInfo.setFont(fFormulario);
		bRemove.setFont(fFormulario);
		bCancelar.setFont(fFormulario);

		eventoRemoveLocomotiva();
		panel1.add(BorderLayout.CENTER, form);
		panel1.add(BorderLayout.SOUTH, botoes);

		Container cp = getContentPane();

		cp.add(panel1);
		setSize(330, 230);
		setFocusable(false);
		setAutoRequestFocus(true);
		setAlwaysOnTop(true);

		setLocationRelativeTo(null);
	}

	public void eventoRemoveLocomotiva() {
		bRemove.addActionListener((e) -> {
                    int total = 0;
                    ResultSet set = null;
                    Statement statement = null;
                    Connection conn = DB.getInstance().getConnection();
                    
                    if(tClasse.getText().equals("")){
                        JOptionPane.showMessageDialog(this, "É necessário fornecer a Classe para realizar essa ação.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }else{
                        try{
                            statement = conn.createStatement();
                            
                            set = statement.executeQuery("SELECT COUNT(*) FROM LOCOMOTIVAS WHERE classe=" + tClasse.getText());
                            
                            while(set.next()){
                                total = set.getInt(1);
                            }
                            
                            statement.close();
                            
                            if(total > 0){
                                statement = conn.createStatement();
                                
                                statement.executeUpdate("DELETE FROM LOCOMOTIVAS WHERE classe=" + tClasse.getText());

                                statement.close();
                            
                                tClasse.setText("");

                                JOptionPane.showMessageDialog(this, "Locomotiva excluída com sucesso.", "Success", JOptionPane.PLAIN_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(this, "Nenhum registro encontrado com as informações dadas.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }catch(SQLException ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                });

		bCancelar.addActionListener((e) -> {
                    dispose();
		});
	}
}
