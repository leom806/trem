package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import database.DB;
import helpers.Helper;
import java.awt.IllegalComponentStateException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "unused"})
public class Menu extends Window {

    private int total = 0;
    private int count = 0;
    private ResultSet set = null;
    private Statement statement = null;
    private static Connection conn = DB.getInstance().getConnection();
    private static Menu instance;

    private JTabbedPane tab; // abas da janela de menu

    private Font fAbas = new Font("Calibri", Font.BOLD, 22),
            fBotoes = new Font("Calibri", Font.BOLD, 20),
            fTitulo = new Font("Arial", Font.BOLD, 24); // fontes

    private JPanel panel1,
            panel2,
            panel3,
            panel4,
            panel5,
            panel6,
            panel7,
            panel8,
            panel9; // paineis das abas

    private JButton bCadastroVagao,
            bRemoverVagao,
            bAlterarVagao,
            bExportarV; // aba 02 - vagões

    private JButton bCadastroLocomotiva,
            bRemoverLocomotiva,
            bAlterarLocomotiva,
            bExportarL; // aba 03 - locomotivas
    
    private DefaultTableModel mcomps = new DefaultTableModel();
    private JTable listaComposicoes;
    private JScrollPane scrollPaneComposicoes;
    private JButton bCadastroComp,
                    bRemoverComp,
                    bAlterarComp,
                    bExportarC; // aba 04 - composições 

    private JButton bRelVagao,
                    bRelLocomotiva,
                    bRelComp,
                    bRelUsuario; // aba 05 - relatórios

    private JTextArea tRelatorios; // aba 05 - relatórios

    private JButton bPesquisar; // aba 07 - pesquisa
    private JTextField tPesquisar; // aba 07 - pesquisa

    private JRadioButton rbDefault,
                         rbAluminium,
                         rbFast,
                         rbLuna,
                         rbTexture,
                         rbTamanho1,
                         rbTamanho2;// aba 08 - Configurações

    private JPanel pBotoesV,
                   pBotoesL,
                   pBotoesC; // paineis de botoes

    private static CellConstraints cc = new CellConstraints(); // pode.

    // Resources
    Icon home, vagao, locomotiva, composicao, rel, informacoes, pesquisar, config;

    private Menu() {
        initComp();
    }

    public static Menu getInstance(){
        return (instance==null) ? new Menu() : instance;
    }
    
    public static void deleteInstance(){
        instance = null;
        System.gc();
    }

    public void loadResources(){
        try{
            home = new ImageIcon(getClass().getResource("../resources/images/home.png"));   
            vagao = new ImageIcon(getClass().getResource("../resources/images/vagao.png"));
            locomotiva = new ImageIcon(getClass().getResource("../resources/images/locomotiva.png"));
            composicao = new ImageIcon(getClass().getResource("../resources/images/composicao.png"));
            rel = new ImageIcon(getClass().getResource("../resources/images/form.png")); 
            informacoes = new ImageIcon(getClass().getResource("../resources/images/informações.png")); 
            pesquisar = new ImageIcon(getClass().getResource("../resources/images/pesquisar.png"));
            config = new ImageIcon(getClass().getResource("../resources/images/configurações.png")); 
        }catch(NullPointerException ex){
            JOptionPane.showMessageDialog(null, "Não foi possível carregar as imagens.");
        }
    }
    
    private void initComp() {
        setTitle("VLC - Menu");

        tab = new JTabbedPane(JTabbedPane.RIGHT);

        aba01();// Aba 01 - Home

        // As abas a seguir interagem com o banco de dados e precisam estar dentro de um bloco try catch.
        try {
            aba02();// Aba 02 - Vagões
            aba03();// Aba 03 - Locomotivas
            aba04();// Aba 04 - Composições
        } catch (SQLException sql) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro na conexão com o banco de dados. \n"
                    + sql.getMessage());
        }

        aba05();// Aba 05 - Relatórios

        aba06();// Aba 06 - Informações

        aba07();// Aba 07 - Pesquisa

        aba08();// Aba 08 - Configurações

        tab.setTabPlacement(JTabbedPane.LEFT);// colocando as abas do lado esquerdo

        // container da classe menu onde irï¿½ a aplicaï¿½ï¿½o
        Container cp = getContentPane();
        cp.add(tab);

        tab.setFont(fAbas); // setando fonte das abas
        tab.setFocusable(false); // remove o outline da aba selecionada
        setSize(800, 600); // tamanho da janela
        setLocationRelativeTo(null); // opção para fazer a janela abrir no centro da tela
    }

    private void aba01() {
        panel1 = new JPanel(); // panel da aba 01
        panel1.setLayout(new BorderLayout()); // layout
    
        addTab(tab, panel1, "Home               ", home);// Fim da Aba 01 - Homes

        // titulo da página incial
        JLabel titPagInicial = new JLabel("Página Inicial");
        titPagInicial.setFont(fTitulo); // fonte do titulo
        titPagInicial.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento do texto

        JPanel info = new JPanel(); // panel com as informações da pagina inicial
        info.setBorder(BorderFactory.createTitledBorder("")); // borda

        String data = "dd/MM/yyyy";
        String horario = "hh:mm";

        SimpleDateFormat sdfData = new SimpleDateFormat(data); // data
        SimpleDateFormat sdfHora = new SimpleDateFormat(horario); // hora

        JLabel usuario = new JLabel("Usuário: " + "GiroPiroka"); // troca aqui hein
        JLabel nome = new JLabel("Nome: " + "giro"); // troca aqui 2
        JLabel numFuncionario = new JLabel("Nº do funcionário: " + "696969");// troca aqui tambem
        JLabel setor = new JLabel("Setor: " + "putaria"); // troca aqui hein, n vai esquecer
        JLabel cargo = new JLabel("Cargo: " + "gigolô"); // esse tb

        JLabel hora = new JLabel("Hora: " + sdfHora.format(new Date()));
        JLabel dia = new JLabel("Data: " + sdfData.format(new Date()));

        FormLayout infoLayout = new FormLayout("3dlu, 2dlu, pref, 3dlu", // colunas
                "60dlu, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu"); // linhas
        info.setLayout(infoLayout);

        // colocando os campos no formlayout
        info.add(usuario, cc.xy(3, 3));
        info.add(nome, cc.xy(3, 5));
        info.add(numFuncionario, cc.xy(3, 7));
        info.add(setor, cc.xy(3, 9));
        info.add(cargo, cc.xy(3, 11));
        info.add(hora, cc.xy(3, 13));
        info.add(dia, cc.xy(3, 15));

        // fonte dos campos
        usuario.setFont(fAbas);
        nome.setFont(fAbas);
        numFuncionario.setFont(fAbas);
        setor.setFont(fAbas);
        cargo.setFont(fAbas);
        hora.setFont(fAbas);
        dia.setFont(fAbas);

        // adicionando ao panel1
        panel1.add(BorderLayout.NORTH, titPagInicial); // adicionando titulo no panel
        panel1.add(BorderLayout.CENTER, info); // Fim da Aba 01 - Home
    } // Fim da Aba 01 - Home

    private void aba02() throws SQLException {
        panel2 = new JPanel(); // painel
        panel2.setLayout(new BorderLayout()); // layout

        addTab(tab, panel2, "Vagões             ", vagao); // Adicionando no panel de abas

        // titulo
        JLabel titV = new JLabel("Vagões");
        titV.setFont(fTitulo);
        titV.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(BorderLayout.NORTH, titV);

        // lista de vagÃµes
        statement = conn.createStatement();

        set = statement.executeQuery("SELECT COUNT(*) FROM vagoes");

        while (set.next()) {
            total = set.getInt(1);
        }

        statement.close();

        Object[][] vagoes = new Object[total][6];

        Object[] vagoes_colunas = {"ID", "Tipo", "Comprimento", "Peso", "Bitola", "Proprieário"};

        count = 0;

        statement = conn.createStatement();

        set = statement.executeQuery("SELECT * FROM vagoes");

        while (set.next()) {
            vagoes[count][0] = set.getInt("id");
            vagoes[count][1] = set.getString("tipo");
            vagoes[count][2] = set.getString("comprimento");
            vagoes[count][3] = set.getInt("peso");
            vagoes[count][4] = set.getInt("bitola");
            vagoes[count][5] = set.getString("proprietario");

            count++;
        }

        statement.close();

        JTable listaVagoes = new JTable(vagoes, vagoes_colunas);
        JScrollPane scrollPaneVagoes = new JScrollPane(listaVagoes);

        panel2.add(BorderLayout.CENTER, scrollPaneVagoes);

        // adicionando botoes ao JPanel
        pBotoesV = new JPanel();
        pBotoesV.add(bCadastroVagao = new JButton("Cadastrar"));
        pBotoesV.add(bRemoverVagao = new JButton("Remover"));
        pBotoesV.add(bAlterarVagao = new JButton("Alterar"));
        pBotoesV.add(bExportarV = new JButton("Exportar dados"));

        // seta a fonte dos botï¿½es da aba vagï¿½o
        bCadastroVagao.setFont(fBotoes);
        bRemoverVagao.setFont(fBotoes);
        bAlterarVagao.setFont(fBotoes);
        bExportarV.setFont(fBotoes);

        // evento dos botï¿½es
        eventoVagoes();

        panel2.add(BorderLayout.SOUTH, pBotoesV); // panel final
        panel2 = new JPanel(); // painel
        panel2.setLayout(new BorderLayout()); // layout

    } // Fim da Aba 02 - Vagões

    private void aba03() throws SQLException {
        panel3 = new JPanel();
        panel3.setLayout(new BorderLayout());
        tab.addTab("Locomotivas    ", locomotiva, panel3); // adicionando no panel de abas

        // titulo
        JLabel titL = new JLabel("Locomotivas");
        titL.setFont(fTitulo);
        titL.setHorizontalAlignment(SwingConstants.CENTER);
        panel3.add(BorderLayout.NORTH, titL);

        // COLOCAR JTABLE
        statement = conn.createStatement();

        set = statement.executeQuery("SELECT COUNT(*) FROM locomotivas");

        while (set.next()) {
            total = set.getInt(1);
        }

        statement.close();

        Object[][] locomotivas = new Object[total][5];

        Object[] locomotivas_colunas = {"Classe", "Descrição", "PMR", "Comprimento", "Bitola"};

        count = 0;

        statement = conn.createStatement();

        set = statement.executeQuery("SELECT * FROM locomotivas");

        while (set.next()) {
            locomotivas[count][0] = set.getInt("classe");
            locomotivas[count][1] = set.getString("descricao");
            locomotivas[count][2] = set.getString("pmr");
            locomotivas[count][3] = set.getString("comprimento");
            locomotivas[count][4] = set.getString("bitola");

            count++;
        }

        statement.close();

        JTable listaLocomotivas = new JTable(locomotivas, locomotivas_colunas);
        JScrollPane scrollPaneLocomotivas = new JScrollPane(listaLocomotivas);

        panel3.add(BorderLayout.CENTER, scrollPaneLocomotivas);

        // adicionando botoes ao JPanel
        pBotoesL = new JPanel();
        pBotoesL.add(bCadastroLocomotiva = new JButton("Cadastrar"));
        pBotoesL.add(bRemoverLocomotiva = new JButton("Remover"));
        pBotoesL.add(bAlterarLocomotiva = new JButton("Alterar"));
        pBotoesL.add(bExportarL = new JButton("Exportar dados"));

        // seta a fonte dos botoes da aba locomotiva
        bCadastroLocomotiva.setFont(fBotoes);
        bRemoverLocomotiva.setFont(fBotoes);
        bAlterarLocomotiva.setFont(fBotoes);
        bExportarL.setFont(fBotoes);

        // evento dos botï¿½es
        eventoLocomotiva();

        panel3.add(BorderLayout.SOUTH, pBotoesL); // panel final
        // Fim da Aba 03 - Locomotivas
    } // Fim da Aba 03 - Locomotivas

    private void aba04() throws SQLException {
        
        panel4 = new JPanel();
        panel4.setLayout(new BorderLayout());
        
        addTab(tab, panel4, "Composições   ", composicao);

        JLabel titC = new JLabel("Composições");// titulo
        titC.setFont(fTitulo);
        titC.setHorizontalAlignment(SwingConstants.CENTER);
        panel4.add(BorderLayout.NORTH, titC);
        
        listaComposicoes = new JTable();
        listaComposicoes.setModel(mcomps);            
        scrollPaneComposicoes = new JScrollPane(listaComposicoes);          
        panel4.add(BorderLayout.CENTER, scrollPaneComposicoes);
        
        Object[] composicoes_colunas = {"ID", "Nome", "Peso Total", "Peso Rebocável", "Comprimento"};
        mcomps.setColumnIdentifiers(composicoes_colunas);
        
        // Refresh
        carregarComposicoes();                                 

        // adicionando botoes ao JPanel
        pBotoesC = new JPanel();
        pBotoesC.add(bCadastroComp = new JButton("Cadastrar"));
        bCadastroComp.setFont(fBotoes);
        pBotoesC.add(bRemoverComp = new JButton("Remover"));
        bRemoverComp.setFont(fBotoes);
        pBotoesC.add(bAlterarComp = new JButton("Alterar"));
        bAlterarComp.setFont(fBotoes);
        pBotoesC.add(bExportarC = new JButton("Exportar dados"));
        bExportarC.setFont(fBotoes);

        eventoComposicao(); // Adicionando os Eventos de Composicoes

        panel4.add(BorderLayout.SOUTH, pBotoesC);

    } // Fim da Aba 04 - Composições

    private void aba05() {
        panel5 = new JPanel();
        panel5.setLayout(new BorderLayout());

        addTab(tab, panel5, "Relatórios       ", rel);// adicionando no panel de abas

        JLabel titRel = new JLabel("Relatórios"); // titulo
        titRel.setFont(fTitulo); // fonte do titulo
        titRel.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento de texto

        JPanel relatorio = new JPanel(); // panel com as informações da pagina inicial
        relatorio.setBorder(BorderFactory.createTitledBorder("")); // borda

        JScrollPane sp = new JScrollPane(tRelatorios = new JTextArea()); // adicionando barras de rolagem no textarea
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // barra de rolagem vertical
        sp.setHorizontalScrollBarPolicy((JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)); // barra de rolagem horizontal

        FormLayout relLayout = new FormLayout("1dlu, 1dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu", // colunas
                "1dlu, 1dlu, pref, 5dlu, fill:275dlu, 2dlu"); // linas
        relatorio.setLayout(relLayout);

        // colocando os campos no formlayout
        relatorio.add(bRelVagao = new JButton("<html> Relatório de<br/> Vagões </html>"), cc.xy(3, 3));
        relatorio.add(bRelLocomotiva = new JButton("<html> Relatório de <br/> Locomotivas </html>"), cc.xy(5, 3));
        relatorio.add(bRelComp = new JButton("<html> Relatório de<br/> Composições </html>"), cc.xy(7, 3));
        relatorio.add(bRelUsuario = new JButton("<html> Relatório de<br/> Usuários </html>"), cc.xy(9, 3));
        relatorio.add(sp, cc.xyw(3, 5, 7));

        // redefinindo tamanho dos botões
        bRelVagao.setPreferredSize(new Dimension(142, 75));
        bRelLocomotiva.setPreferredSize(new Dimension(142, 75));
        bRelComp.setPreferredSize(new Dimension(142, 75));
        bRelUsuario.setPreferredSize(new Dimension(142, 75));

        // fontes
        bRelVagao.setFont(fBotoes);
        bRelLocomotiva.setFont(fBotoes);
        bRelComp.setFont(fBotoes);
        bRelUsuario.setFont(fBotoes);

        // desativando o setfocusable dos botões
        bRelVagao.setFocusable(false);
        bRelLocomotiva.setFocusable(false);
        bRelComp.setFocusable(false);
        bRelUsuario.setFocusable(false);

        eventoRelatorio(); // eventos dos botões

        panel5.add(BorderLayout.NORTH, titRel); // adicionando titulo
        panel5.add(BorderLayout.CENTER, relatorio);
    } // Fim da Aba 05 - Relatórios

    private void aba06() {
        panel6 = new JPanel();
        panel6.setLayout(new BorderLayout()); // layout do panel

        addTab(tab, panel6, "Informações    ", informacoes); // fim da aba 06

        JLabel titInfo = new JLabel("Informações sobre vagões");
        titInfo.setFont(fTitulo); // fonte do titulo
        titInfo.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento de texto

        JPanel tnc = new JPanel();
        JLabel teste = new JLabel();
        JScrollPane spInfo = new JScrollPane();

        teste.setIcon(new ImageIcon("../resources/images/info.png"));
        spInfo.setViewportView(teste);

        tnc.add(spInfo);
        panel6.add(BorderLayout.NORTH, titInfo);
        panel6.add(BorderLayout.CENTER, tnc);// fim da aba 06 - informacoes

    } // Fim da Aba 06 - Informações

    private void aba07() {
        panel7 = new JPanel();
        addTab(tab, panel7, "Pesquisa          ", pesquisar);
        panel7.setLayout(new BorderLayout()); // setando o layout do panel7

        //		JTable listaComposicoesPesquisa = new JTable(composicoes, composicoes_colunas);
        //		JScrollPane scrollPaneComposicoesPesquisa = new JScrollPane(listaComposicoes);
        JPanel pesquisa = new JPanel();

        // conteudo.setBorder(BorderFactory.createTitledBorder("Resultado da
        // pesquisa"));
        FormLayout formPesquisa = new FormLayout("300dlu, 3dlu, pref, 3dlu", // colunas
                "pref, 3dlu");// linhas
        CellConstraints cc = new CellConstraints();

        pesquisa.setLayout(formPesquisa);
        pesquisa.add(tPesquisar = new JTextField(), cc.xy(1, 1));
        pesquisa.add(bPesquisar = new JButton("Pesquisar"), cc.xy(3, 1));

        panel7.add(BorderLayout.NORTH, pesquisa);
        //		panel7.add(BorderLayout.CENTER, scrollPaneComposicoesPesquisa);// adicionando o panel conteudo no panel7;;

        bPesquisar.addActionListener(e -> { // actionListener do botÃ£o de Remover
            SwingUtilities.invokeLater(() -> {
                BorderLayout layout = (BorderLayout) panel7.getLayout();

                if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                    panel7.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                }

                // Faz a pesquisa no BD
                try {
                    count = 0;
                    Object[] composicoes_colunas = {"Id", "Nome", "Peso Total", "PMR", "Comprimento"};
                    int ctcomp = 0; // ! Falta DOC !

                    statement = conn.createStatement();

                    String queryCount = "SELECT COUNT(*) FROM COMPOSICOES";
                    String queryCountNome = "SELECT COUNT(*) FROM COMPOSICOES WHERE LOWER(nome) LIKE LOWER('%"+
                                            tPesquisar.getText()+"%')";
                    
                    String queryBusca = "SELECT * FROM COMPOSICOES";
                    String queryBuscaNome = "SELECT * FROM COMPOSICOES WHERE LOWER(nome) LIKE LOWER('%"+
                                            tPesquisar.getText()+"%')";
                    
                    ResultSet ct = statement.executeQuery( 
                                   (tPesquisar.getText().isEmpty()) ? queryCount : queryCountNome 
                                );
                    
                    while (ct.next()) {
                        ctcomp = ct.getInt(1);
                    }

                    statement.close();                    

                    statement = conn.createStatement();

                    Object[][] values = new Object[ctcomp][5];

                    ResultSet res = statement.executeQuery( 
                                   (tPesquisar.getText().isEmpty()) ? queryBusca : queryBuscaNome 
                                );

                    
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    while (res.next()) {
                        values[count][0] = res.getInt("id");
                        values[count][1] = res.getString("nome");
                        values[count][2] = df.format(res.getDouble("pesototal"));
                        values[count][3] = df.format(res.getDouble("pesorebocavel"));
                        values[count][4] = df.format(res.getDouble("comprimento"));
                                                
                        count++;
                    }                    
                    
                    statement.close();

                    JTable j = new JTable(values, composicoes_colunas);
                    JScrollPane new_table = new JScrollPane(j);
                    panel7.add(new_table, BorderLayout.CENTER);
                    
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

                panel7.revalidate();
                panel7.repaint();
            });
        });
    } // Fim da Aba 07 - Pesquisar

    private void aba08() {
        panel8 = new JPanel();
        panel8.setLayout(new BorderLayout());

        addTab(tab, panel8, "Configurações ", config);

        JLabel configuracoes = new JLabel("Configuraçoes"); // titulo
        configuracoes.setFont(fTitulo); // fonte do titulo
        configuracoes.setHorizontalAlignment(SwingConstants.CENTER); // alinhamento do texto

        JPanel tudo = new JPanel();
        tudo.setLayout(new GridLayout(4, 1)); // layout

        JPanel tema = new JPanel();
        tema.setBorder(BorderFactory.createTitledBorder("Temas")); // titulo
        tema.setLayout(new GridLayout(1, 4)); // layout

        // opcoes de temas
        tema.add(rbDefault = new JRadioButton("Default"));
        tema.add(rbAluminium = new JRadioButton("Aluminium"));
        tema.add(rbFast = new JRadioButton("Fast"));
        tema.add(rbLuna = new JRadioButton("Luna"));
        tema.add(rbTexture = new JRadioButton("Texture")); 
        rbDefault.setSelected(true);

        // grupo de radioButtons
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbDefault);
        bg.add(rbAluminium);
        bg.add(rbFast);
        bg.add(rbLuna);
        bg.add(rbTexture);

        JPanel tamanho = new JPanel();
        tamanho.setBorder(BorderFactory.createTitledBorder("Resolução"));
        tamanho.setLayout(new GridLayout(1, 2));

        tamanho.add(rbTamanho1 = new JRadioButton("800 x 600"));
        tamanho.add(rbTamanho2 = new JRadioButton("Full Screen"));

        ButtonGroup bgTamanho = new ButtonGroup();
        bgTamanho.add(rbTamanho1);
        bgTamanho.add(rbTamanho2);
        rbTamanho1.setSelected(true);

        // tema.add(btn);
        tudo.add(tema);
        tudo.add(tamanho);
        
        eventosConfiguracoes();

        panel8.add(BorderLayout.NORTH, configuracoes);
        panel8.add(BorderLayout.CENTER, tudo); // fim da aba 08
    } // Fim da Aba 08 - Configurações

    /*
    * Eventos
    */

    private void eventoVagoes() {
        bCadastroVagao.addActionListener((e) -> { // actionListener do botï¿½o de cadastrar
            SwingUtilities.invokeLater(() -> {
                CadastroVagao v1 = new CadastroVagao();
                v1.setVisible(true);
            });
        });

        bRemoverVagao.addActionListener((e) -> {// actionListener do botï¿½o de remover
            SwingUtilities.invokeLater(() -> {
                RemoveVagao ll = new RemoveVagao();
                ll.setVisible(true);
            });
        });

        bAlterarVagao.addActionListener((e) -> {// actionListener do botï¿½o de alterar
            SwingUtilities.invokeLater(() -> {

            });
        });

        bExportarV.addActionListener((e) -> {// actionListener do botï¿½o de exportar
            SwingUtilities.invokeLater(() -> {

            });
        });
    }

    private void eventoLocomotiva() {
        bCadastroLocomotiva.addActionListener((e) -> { // actionListener do botï¿½o de Cadastrar
            SwingUtilities.invokeLater(() -> {
                CadastroLocomotiva l1 = new CadastroLocomotiva();
                l1.setVisible(true);
            });
        });

        bRemoverLocomotiva.addActionListener((e) -> { // actionListener do botÃ£o de Remover
            SwingUtilities.invokeLater(() -> {
                RemoveLocomotiva ll = new RemoveLocomotiva();
                ll.setVisible(true);
            });
        });

        bAlterarLocomotiva.addActionListener((e) -> { // actionListener do botï¿½o de Alterar
            SwingUtilities.invokeLater(() -> {
            });
        });

        bExportarL.addActionListener((e) -> { // actionListener do botÃ£o de Exportar
            SwingUtilities.invokeLater(() -> {
            });
        });
    }

    private void eventoComposicao() {
        bCadastroComp.addActionListener((e) -> { // actionListener do botÃ£o de Cadastrar
            SwingUtilities.invokeLater(() -> {
                CadastroComposicao l1 = new CadastroComposicao();
                l1.setVisible(true);                  
            });
        });

        bRemoverComp.addActionListener((e) -> { // actionListener do botÃ£o de Remover
            SwingUtilities.invokeLater(() -> {
            });
        });

        bAlterarComp.addActionListener((e) -> { // actionListener do botÃ£o de cadastrar
            SwingUtilities.invokeLater(() -> {
            });
        });

        bExportarC.addActionListener((e) -> { // actionListener do botÃ£o de cadastrar
            SwingUtilities.invokeLater(() -> {                
            });
        });
    }

    private void eventoRelatorio() {
        bRelVagao.addActionListener((e) -> { // actionListener do botão de exportar
            // coloca o codigo aqui viado
        });

        bRelLocomotiva.addActionListener((e) -> { // actionListener do botão de exportar
            // coloca o codigo aqui viado
        });

        bRelComp.addActionListener((e) -> { // actionListener do botão de exportar
            // coloca o codigo aqui viado
        });

        bRelUsuario.addActionListener((e) -> { // actionListener do botão de exportar
            // coloca o codigo aqui viado
        });
    }

    private void eventosConfiguracoes(){
        
        rbDefault.addActionListener((e) -> {
            updateUI(Helper.DEFAULT);
        });
                
        rbAluminium.addActionListener((e) -> {
            updateUI(Helper.ALUMINIUM);
        });
        
        rbFast.addActionListener((e) -> {
            updateUI(Helper.FAST);
        });
        
        rbLuna.addActionListener((e) -> {
            updateUI(Helper.LUNA);
        });
        
        rbTexture.addActionListener((e) -> {
            updateUI(Helper.TEXTURE);
        });
        
  
        rbTamanho1.addActionListener((e) -> {
            changeSize(1); // 800x600
        });
        
        rbTamanho2.addActionListener((e) -> {
            changeSize(0); // Fullscreen
        });
    }
    
    private void updateUI(String look){
        Helper.switchUI(this, look);
    }
    
    private void changeSize(int mode){
        try{
            if(mode==0){
                this.setExtendedState(MAXIMIZED_BOTH); 
                this.setUndecorated(true);
            }else{
                this.setExtendedState(NORMAL); 
                this.setUndecorated(false);
                this.setSize(800, 600);
            }
        }catch(IllegalComponentStateException ex){
            // Do nothing..
        }
        
        this.revalidate();
        this.repaint();
    }
    
    /*
    * Métodos auxiliares
    */

    private void addTab(JTabbedPane tabbedPane, Component tab, String title, Icon icon) {
        tabbedPane.add(tab);

        JLabel lbl = new JLabel(title);
        lbl.setIcon(icon);
        lbl.setFont(this.fAbas);
        lbl.setIconTextGap(5);
        lbl.setHorizontalTextPosition(SwingConstants.RIGHT);

        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, lbl);
    }
    
    // Leo
    public void atualizarComposicoes() {
        carregarComposicoes();
        Login.reload();
    } 
    
    private void carregarComposicoes(){
        try{                        
            statement = conn.createStatement();
            set = statement.executeQuery("SELECT COUNT(*) FROM composicoes");

            while (set.next()) {
                total = set.getInt(1);
            }

            statement.close();
            Object[][] composicoes = new Object[total][6];       
            count = 0;
            statement = conn.createStatement();
            set = statement.executeQuery("SELECT * FROM composicoes");

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            while (set.next()) {
                composicoes[count][0] = set.getInt("id");
                composicoes[count][1] = set.getString("nome");
                composicoes[count][2] = df.format(set.getDouble("pesototal"));
                composicoes[count][3] = df.format(set.getDouble("pesorebocavel"));
                composicoes[count][4] = df.format(set.getDouble("comprimento"));
                count++;
            }

            statement.close();        
            
            // Limpa o modelo            
            mcomps.setRowCount(0);           
            
            // Reescreve o modelo
            for(int i=0; i<composicoes.length; i++){
                mcomps.addRow(composicoes[i]);
            }
            
            listaComposicoes.invalidate();
            scrollPaneComposicoes.repaint();
                        
            // POR QUE DIABOS QUE O JTABLE NÃO ATUALIZA?????   
            
                        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar composições.");
        }
    }

}
