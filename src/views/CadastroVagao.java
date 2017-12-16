package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import helpers.Helper;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import models.Subtipo;
import models.Tipo;
import models.Vagao;

@SuppressWarnings({"serial", "unused"})

public class CadastroVagao extends JDialog {

    private Helper helper = new Helper();

    private JButton bSalvar, bLimpar, bFechar, bNovo; // bot�es do formul�rio de cadastro
    private JComboBox<String> tipo; // comboBox para tipo de vag�o
    private JComboBox<String> subTipo; // comboBox para suTtipo de vag�o
    private JComboBox<String> bitola; // comboBox para suTtipo de vag�o
    private JTextField proprietario;
    private JTextField serie, digito;

    private Font fBotoes = new Font("Calibri", Font.BOLD, 20),
            fCombo = new Font("Calibri", Font.BOLD, 18),
            fInfo = new Font("Calibri", Font.BOLD, 16); // fontes

    public CadastroVagao() {// construtor
        initComp();
    }

    public void initComp() {
        setTitle("VLC - Cadastro de Vagão"); // titulo da janela

        // Preenchendo os valores com as informações já especificadas
        String[] tipoVagao = helper.getTipos();

        String[] subTipoVagao = {};

        String[] bitolaTipos = helper.getPmas();

        // paineis para setar a localiza��o dos componentes
        JPanel cima = new JPanel();
        JPanel centro = new JPanel();
        JPanel baixo = new JPanel();

        // titulo da janela
        JLabel tit = new JLabel("Cadastro");
        tit.setFont(fBotoes);
        tit.setHorizontalAlignment(SwingConstants.CENTER);// alinhamento do texto
        cima.add(tit);// panel de cima

        // panel do centro
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(2, 1)); // gridlayout para setar os paneis

        centro.setBorder(BorderFactory.createTitledBorder("Cadastro de vagão"));
        centro.add(tipo = new JComboBox<>(tipoVagao)); // combobox de opções

        FormLayout form = new FormLayout(
                "65dlu, 3dlu, 80dlu, 1dlu, 40dlu, 1dlu, 40dlu, 20dlu, 50dlu, 5dlu, 20dlu, 1dlu", // colunas
                "17dlu, 3dlu, 20dlu, pref, 3dlu"); // linhas
        centro.setLayout(form);
        CellConstraints cc = new CellConstraints();

        
        centro.add(tipo = new JComboBox<>(tipoVagao), cc.xy(3, 3));
        tipo.setFont(fCombo);
        tipo.setToolTipText("Tipo do vagão");
        tipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tipo value = helper.getTipoByName(tipo.getSelectedItem().toString());
                ArrayList<Subtipo> subtipos = helper.getSubtipos(value);

                if (subtipos != null) {
                    subTipo.removeAllItems();

                    for (Subtipo element : subtipos) {
                        subTipo.addItem(element.getLetra() + "");
                    }
                }
            }
        });

        centro.add(subTipo = new JComboBox<>(subTipoVagao), cc.xy(5, 3));
        subTipo.setFont(fCombo);
        subTipo.setToolTipText("SubTipo do vagão");        

        centro.add(bitola = new JComboBox<>(bitolaTipos), cc.xy(7, 3));
        bitola.setFont(fCombo);
        bitola.setToolTipText("Peso Max./Bitola");

        centro.add(serie = new JTextField(), cc.xy(9, 3));
        serie.setFont(fCombo);
        serie.setToolTipText("Digite o número serial do vagão");

        centro.add(digito = new JTextField(), cc.xy(11, 3));
        digito.setFont(fCombo);
        digito.setToolTipText("Digite o dígito verificador do vagão");        

        // formlayout das mensagens de informação
        FormLayout informacoes = new FormLayout("pref, 3dlu", // colunas
                "12dlu, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu"); // linhas

        // adicionando o painel do centro no gridlayout
        grid.add(centro);
        // bot�es inferiores
        baixo.add(bNovo = new JButton("Novo")); // bot�o novo
        baixo.add(bSalvar = new JButton("Salvar")); // bot�o de salvar o cadastro
        baixo.add(bLimpar = new JButton("Limpar")); // bot�o limpar campos
        baixo.add(bFechar = new JButton("Cancelar"));// bot�o cancelar

        // evento dos bot�es
        eventoBotoes();

        // setando a fonte dos bot�es
        bSalvar.setFont(fBotoes);
        bLimpar.setFont(fBotoes);
        bNovo.setFont(fBotoes);
        bFechar.setFont(fBotoes);

        bSalvar.setFocusable(false);
        bLimpar.setFocusable(false);
        bNovo.setFocusable(false);
        bFechar.setFocusable(false);

        // panel final
        JPanel tudo = new JPanel();
        tudo.setLayout(new BorderLayout());
        tudo.add(BorderLayout.NORTH, cima);
        tudo.add(BorderLayout.CENTER, grid);
        tudo.add(BorderLayout.SOUTH, baixo);

        // container
        Container cp = getContentPane();
        cp.add(tudo);

        setModal(true);
        setFocusable(false);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setSize(650, 400);
        setLocationRelativeTo(null);
    }

    public void eventoBotoes() {

        bSalvar.addActionListener((e) -> { // Salvar vagão, alterar
            SwingUtilities.invokeLater(() -> {
                if (!temErroNoVagao()) {
                    JOptionPane.showMessageDialog(this,
                            "É necessário preencher todos os campos para prosseguir.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    String letras = (helper.getLetraPorTipo(
                            tipo.getSelectedItem().toString())
                            + subTipo.getSelectedItem().toString()
                            + bitola.getSelectedItem().toString());

                    Vagao vagao = new Vagao(
                            letras,
                            serie.getText(),
                            Integer.parseInt(digito.getText())
                    );

                    vagao.save();
                    limpar();

                    JOptionPane.showMessageDialog(this,
                            "Vagão adicionado com sucesso.", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            });
        });

        bNovo.addActionListener((e) -> { // Criar novo vagão
            SwingUtilities.invokeLater(() -> {
                if (!temErroNoVagao()) {

                    String letras = (helper.getLetraPorTipo(
                            tipo.getSelectedItem().toString())
                            + subTipo.getSelectedItem().toString()
                            + bitola.getSelectedItem().toString());

                    Vagao vagao = new Vagao(letras, serie.getText(), Integer.parseInt(digito.getText()));

                    vagao.save();
                    limpar();

                    JOptionPane.showMessageDialog(this,
                            "Vagão adicionado com sucesso.", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            });
        });

        bLimpar.addActionListener((e) -> { // actionListener do bot�o limparCampos
            SwingUtilities.invokeLater(() -> {
                limpar();
            });
        });

        bFechar.addActionListener((e) -> { // Fechar
            dispose();
        }); // fim dos eventos
    }

    /**
     *
     * @return
     */
    private boolean temErroNoVagao() {
        String erro = "";

        if (tipo.getSelectedIndex() == -1) {
            erro = "É preciso selecionar um tipo.";
        } else if (subTipo.getSelectedIndex() == -1) {
            erro = "É preciso selecionar um subtipo.";
        } else if (bitola.getSelectedIndex() == -1) {
            erro = "É preciso selecionar uma PMA.";
        } else if (serie.getText().equals("")) {
            erro = "A série não pode estar vazia.";
        } else if (serie.getText().length() != 6) {
            erro = "O comprimento da série deve ser de exatamente 6 caracteres";
        } else if (digito.getText().equals("")) {
            erro = "O dígito não pode estar vazio.";
        } else if (digito.getText().length() != 1) {
            erro = "O dígito precisa ser apenas um algarismo.";
        } else if (serie.getText().matches(".*\\D.*")) {
            erro = "A série deve ser composta apenas de números.";
        } else if (digito.getText().matches("\\D")) {
            erro = "O dígito deve ser um número.";
        }

        if (erro.isEmpty()) {
            return false; // Não tem erro no vagão
        }

        JOptionPane.showMessageDialog(this, erro, "Error", JOptionPane.ERROR_MESSAGE);
        return true;
    }

    /**
     * Limpa a tela
     */
    private void limpar() {
        tipo.setSelectedItem("");
        subTipo.setSelectedItem("");
        serie.setText("");
        digito.setText("");
    }

}
