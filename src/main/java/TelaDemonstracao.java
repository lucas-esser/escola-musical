package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.QuadCurve2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Aluno;
import model.Curso;
import model.Instrumento;
import model.Matricula;
import model.Professor;
import util.Validador;

// Tela de demonstracao da Escola Musical.
// Roda 100% em memoria (nao precisa de MySQL) e usa as classes do pacote model.
public class TelaDemonstracao extends JFrame {

    // ===================== PALETA E FONTES =====================
    private static final Color ROXO_ESCURO = new Color(0x2A1B54);
    private static final Color ROXO = new Color(0x5B3FA0);
    private static final Color ROXO_CLARO = new Color(0xEDE7FA);
    private static final Color DOURADO = new Color(0xF2B33D);
    private static final Color FUNDO = new Color(0xF5F3FB);
    private static final Color TEXTO = new Color(0x2B2540);
    private static final Color TEXTO_SUAVE = new Color(0x5B5472);
    private static final Color BORDA = new Color(0xD9D3EA);
    private static final Color VERDE = new Color(0x2E9E6B);
    private static final Color AMARELO = new Color(0xD99A00);
    private static final Color VERMELHO = new Color(0xD64545);
    private static final Color AZUL = new Color(0x2F7DA8);
    private static final Color NEUTRO = new Color(0x6E6888);

    private static final Font FONTE = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONTE_NEGRITO = new Font("SansSerif", Font.BOLD, 13);

    private static final Locale BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    static {
        // o combo desenha o valor atual com essa cor (por padrao e cinza)
        UIManager.put("ComboBox.background", Color.WHITE);
    }

    // ===================== DADOS (EM MEMORIA) =====================
    private final List<Instrumento> instrumentos = new ArrayList<>();
    private final List<Professor> professores = new ArrayList<>();
    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Curso> cursos = new ArrayList<>();
    private final List<Matricula> matriculas = new ArrayList<>();
    private int proximoIdMatricula = 1;

    // ===================== COMPONENTES =====================
    private final JComboBox<Instrumento> cbCursoInstrumento = comboBox();
    private final JComboBox<Professor> cbCursoProfessor = comboBox();
    private final JComboBox<Aluno> cbMatAluno = comboBox();
    private final JComboBox<Curso> cbMatCurso = comboBox();

    private CrudInstrumento crudInstrumento;
    private CrudProfessor crudProfessor;
    private CrudAluno crudAluno;
    private CrudCurso crudCurso;

    private DefaultTableModel modeloMat;
    private JTable tabelaMat;
    private JLabel lblAvisoMenor;
    private JTextArea areaLog;
    private JLabel lblResumo;

    public TelaDemonstracao() {
        setTitle("Escola Musical - Demonstracao");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1180, tela.width - 40), Math.min(780, tela.height - 60));
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        initComponents();
        carregarExemplo();
    }

    // ===================== MONTAGEM DA TELA =====================
    private void initComponents() {
        getContentPane().setBackground(FUNDO);
        setLayout(new BorderLayout());

        crudInstrumento = new CrudInstrumento();
        crudProfessor = new CrudProfessor();
        crudAluno = new CrudAluno();
        crudCurso = new CrudCurso();

        add(new Cabecalho(), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setUI(new AbasUI());
        abas.setOpaque(false);
        abas.setFont(new Font("SansSerif", Font.BOLD, 13));
        abas.addTab("Alunos", crudAluno.criarAba());
        abas.addTab("Professores", crudProfessor.criarAba());
        abas.addTab("Instrumentos", crudInstrumento.criarAba());
        abas.addTab("Cursos", crudCurso.criarAba());
        abas.addTab("Matriculas", montarAbaMatriculas());
        Runnable pintarAbas = () -> {
            for (int i = 0; i < abas.getTabCount(); i++) {
                abas.setForegroundAt(i, i == abas.getSelectedIndex() ? Color.WHITE : ROXO);
            }
        };
        abas.addChangeListener(e -> pintarAbas.run());
        pintarAbas.run();

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(14, 18, 8, 18));
        centro.add(abas, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        add(montarRodape(), BorderLayout.SOUTH);
    }

    private JPanel montarRodape() {
        JPanel rodape = new JPanel(new BorderLayout(12, 0));
        rodape.setBackground(ROXO_ESCURO);
        rodape.setBorder(new EmptyBorder(10, 18, 10, 18));

        lblResumo = new JLabel(" ");
        lblResumo.setForeground(Color.WHITE);
        lblResumo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        botoes.add(new Botao("Restaurar exemplo", DOURADO, ROXO_ESCURO, e -> carregarExemplo()));
        botoes.add(new Botao("Limpar tudo", VERMELHO, Color.WHITE, e -> limparTudo()));

        rodape.add(lblResumo, BorderLayout.CENTER);
        rodape.add(botoes, BorderLayout.EAST);
        return rodape;
    }

    // ===================== ABA DE MATRICULAS =====================
    private JPanel montarAbaMatriculas() {
        lblAvisoMenor = new JLabel(" ");
        lblAvisoMenor.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblAvisoMenor.setForeground(AMARELO);
        cbMatAluno.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                atualizarAvisoMenor();
            }
        });

        JPanel colAluno = new JPanel(new BorderLayout(0, 3));
        colAluno.setOpaque(false);
        colAluno.add(rotulo("Aluno"), BorderLayout.NORTH);
        colAluno.add(cbMatAluno, BorderLayout.CENTER);
        colAluno.add(lblAvisoMenor, BorderLayout.SOUTH);

        JPanel colCurso = new JPanel(new BorderLayout(0, 3));
        colCurso.setOpaque(false);
        colCurso.add(rotulo("Curso"), BorderLayout.NORTH);
        colCurso.add(cbMatCurso, BorderLayout.CENTER);

        JPanel linha = new JPanel(new GridBagLayout());
        linha.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 14);
        linha.add(colAluno, c);
        linha.add(colCurso, c);
        GridBagConstraints cb = new GridBagConstraints();
        cb.anchor = GridBagConstraints.NORTH;
        cb.insets = new Insets(18, 0, 0, 0);
        linha.add(new Botao("Matricular", ROXO, Color.WHITE, e -> matricular()), cb);

        Cartao nova = new Cartao("Nova matricula");
        nova.add(linha, BorderLayout.CENTER);

        modeloMat = new DefaultTableModel(new Object[]{"ID", "Aluno", "Curso", "Data", "Mensalidade", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int linhaTabela, int coluna) {
                return false;
            }
        };
        tabelaMat = criarTabela(modeloMat);
        tabelaMat.getColumnModel().getColumn(0).setMaxWidth(60);
        tabelaMat.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        JPanel acoes = new JPanel();
        acoes.setLayout(new BoxLayout(acoes, BoxLayout.X_AXIS));
        acoes.setOpaque(false);
        acoes.add(new Botao("Trancar", AMARELO, Color.WHITE, e -> alterarStatus("trancar")));
        acoes.add(Box.createHorizontalStrut(8));
        acoes.add(new Botao("Reativar", VERDE, Color.WHITE, e -> alterarStatus("reativar")));
        acoes.add(Box.createHorizontalStrut(8));
        acoes.add(new Botao("Cancelar matricula", VERMELHO, Color.WHITE, e -> alterarStatus("cancelar")));
        acoes.add(Box.createHorizontalGlue());

        Cartao lista = new Cartao("Matriculas");
        lista.add(rolagem(tabelaMat), BorderLayout.CENTER);
        lista.add(acoes, BorderLayout.SOUTH);

        areaLog = new JTextArea(3, 0);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setForeground(TEXTO);
        areaLog.setBorder(new EmptyBorder(6, 8, 6, 8));
        Cartao historico = new Cartao("Historico de operacoes");
        historico.add(rolagem(areaLog), BorderLayout.CENTER);
        historico.setPreferredSize(new Dimension(0, 130));

        JPanel aba = new JPanel(new BorderLayout(0, 14));
        aba.setOpaque(false);
        aba.setBorder(new EmptyBorder(14, 0, 0, 0));
        aba.add(nova, BorderLayout.NORTH);
        aba.add(lista, BorderLayout.CENTER);
        aba.add(historico, BorderLayout.SOUTH);
        return aba;
    }

    private void matricular() {
        Aluno aluno = selecionado(cbMatAluno);
        Curso curso = selecionado(cbMatCurso);
        if (aluno == null || curso == null) {
            aviso("Cadastre pelo menos um aluno e um curso para matricular.");
            return;
        }
        for (Matricula m : matriculas) {
            if (m.getIdAluno() == aluno.getId() && m.getIdCurso() == curso.getId()
                    && !Matricula.CANCELADA.equals(m.getStatus())) {
                aviso("Este aluno ja possui matricula ativa ou trancada neste curso.");
                return;
            }
        }
        Matricula m = novaMatricula(aluno.getId(), curso.getId());
        registrar("Matricula " + m.getId() + ": " + aluno.getNome() + " em " + curso.getNome()
                + " (" + m.getStatus() + ")");
        atualizarTudo();
        if (aluno.ehMenorDeIdade()) {
            JOptionPane.showMessageDialog(this,
                    aluno.getNome() + " e menor de idade.\nLembre-se de registrar o responsavel.",
                    "Aluno menor de idade", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void alterarStatus(String acao) {
        int linha = tabelaMat.getSelectedRow();
        if (linha < 0 || linha >= matriculas.size()) {
            aviso("Selecione uma matricula na tabela.");
            return;
        }
        Matricula m = matriculas.get(linha);
        switch (acao) {
            case "trancar":
                if (!m.estaAtiva()) {
                    aviso("So e possivel trancar uma matricula ativa.");
                    return;
                }
                m.trancar();
                break;
            case "reativar":
                if (!Matricula.TRANCADA.equals(m.getStatus())) {
                    aviso("So e possivel reativar uma matricula trancada.");
                    return;
                }
                m.reativar();
                break;
            case "cancelar":
                if (Matricula.CANCELADA.equals(m.getStatus())) {
                    aviso("Esta matricula ja esta cancelada.");
                    return;
                }
                m.cancelar();
                break;
            default:
                return;
        }
        registrar("Matricula " + m.getId() + " agora esta " + m.getStatus());
        atualizarTudo();
    }

    private Matricula novaMatricula(int idAluno, int idCurso) {
        Matricula m = new Matricula(proximoIdMatricula++, idAluno, idCurso);
        Aluno a = buscarAluno(idAluno);
        Curso c = buscarCurso(idCurso);
        m.setNomeAluno(a == null ? "?" : a.getNome());
        m.setNomeCurso(c == null ? "?" : c.getNome());
        matriculas.add(m);
        return m;
    }

    private void atualizarAvisoMenor() {
        Aluno a = selecionado(cbMatAluno);
        if (a != null && a.ehMenorDeIdade()) {
            lblAvisoMenor.setText("Aluno menor de idade (" + a.getIdade() + " anos) - exige responsavel");
        } else {
            lblAvisoMenor.setText(" ");
        }
    }

    private void registrar(String texto) {
        areaLog.append("[" + LocalTime.now().format(HORA) + "] " + texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    // ===================== DADOS DE EXEMPLO / LIMPEZA =====================
    private void limparDados() {
        instrumentos.clear();
        professores.clear();
        alunos.clear();
        cursos.clear();
        matriculas.clear();
        crudInstrumento.proximoId = 1;
        crudProfessor.proximoId = 1;
        crudAluno.proximoId = 1;
        crudCurso.proximoId = 1;
        proximoIdMatricula = 1;
        crudInstrumento.limpar();
        crudProfessor.limpar();
        crudAluno.limpar();
        crudCurso.limpar();
        areaLog.setText("");
    }

    private void limparTudo() {
        int r = JOptionPane.showConfirmDialog(this, "Apagar todos os dados da demonstracao?",
                "Limpar tudo", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        limparDados();
        atualizarTudo();
    }

    // Mesmos dados de exemplo do script criacao_banco.sql
    private void carregarExemplo() {
        limparDados();

        crudInstrumento.incluir(new Instrumento(0, "Violao", "CORDAS"));
        crudInstrumento.incluir(new Instrumento(0, "Piano", "TECLAS"));
        crudInstrumento.incluir(new Instrumento(0, "Bateria", "PERCUSSAO"));
        crudInstrumento.incluir(new Instrumento(0, "Flauta", "SOPRO"));

        crudProfessor.incluir(new Professor(0, "Carlos Mendes", "(51) 98888-0001", "Violao e Guitarra"));
        crudProfessor.incluir(new Professor(0, "Beatriz Lima", "(51) 98888-0002", "Piano e Teclado"));
        crudProfessor.incluir(new Professor(0, "Rafael Costa", "(51) 98888-0003", "Bateria"));

        crudAluno.incluir(new Aluno(0, "Maria Silva", 15, "(51) 99999-1111", "maria@email.com"));
        crudAluno.incluir(new Aluno(0, "Joao Souza", 28, "(51) 99999-2222", "joao@email.com"));
        crudAluno.incluir(new Aluno(0, "Ana Pereira", 12, "(51) 99999-3333", "ana@email.com"));

        crudCurso.incluir(new Curso(0, "Violao Iniciante", 150.00, "Segunda 14:00", 1, 1));
        crudCurso.incluir(new Curso(0, "Piano Basico", 200.00, "Terca 15:00", 2, 2));
        crudCurso.incluir(new Curso(0, "Bateria para Jovens", 180.00, "Quinta 16:00", 3, 3));

        novaMatricula(1, 1);
        novaMatricula(2, 2);
        novaMatricula(3, 3).trancar();

        registrar("Dados de exemplo carregados.");
        atualizarTudo();
    }

    // ===================== ATUALIZACAO DA TELA =====================
    private void atualizarTudo() {
        crudInstrumento.recarregarTabela();
        crudProfessor.recarregarTabela();
        crudAluno.recarregarTabela();
        crudCurso.recarregarTabela();
        recarregarTabelaMatriculas();
        recarregar(cbCursoInstrumento, instrumentos, Instrumento::getId);
        recarregar(cbCursoProfessor, professores, Professor::getId);
        recarregar(cbMatAluno, alunos, Aluno::getId);
        recarregar(cbMatCurso, cursos, Curso::getId);
        atualizarAvisoMenor();
        atualizarResumo();
    }

    private void recarregarTabelaMatriculas() {
        modeloMat.setRowCount(0);
        for (Matricula m : matriculas) {
            Aluno a = buscarAluno(m.getIdAluno());
            Curso c = buscarCurso(m.getIdCurso());
            modeloMat.addRow(new Object[]{
                m.getId(),
                a == null ? "?" : a.getNome(),
                c == null ? "?" : c.getNome(),
                m.getDataMatricula().format(DATA),
                c == null ? "-" : moeda(c.getValorMensalidade()),
                m.getStatus()
            });
        }
    }

    private void atualizarResumo() {
        int ativas = 0;
        double receita = 0;
        for (Matricula m : matriculas) {
            if (m.estaAtiva()) {
                ativas++;
                Curso c = buscarCurso(m.getIdCurso());
                if (c != null) {
                    receita += c.getValorMensalidade();
                }
            }
        }
        lblResumo.setText(String.format(BR,
                "Alunos: %d  |  Professores: %d  |  Instrumentos: %d  |  Cursos: %d"
                + "  |  Matriculas ativas: %d  |  Receita mensal: %s",
                alunos.size(), professores.size(), instrumentos.size(), cursos.size(), ativas, moeda(receita)));
    }

    // Reconstroi um combo mantendo o item que estava selecionado (se ainda existir)
    private <T> void recarregar(JComboBox<T> combo, List<T> itens, ToIntFunction<T> idDe) {
        int indice = combo.getSelectedIndex();
        int idSelecionado = indice < 0 ? -1 : idDe.applyAsInt(combo.getItemAt(indice));
        combo.removeAllItems();
        T escolhido = null;
        for (T item : itens) {
            combo.addItem(item);
            if (idDe.applyAsInt(item) == idSelecionado) {
                escolhido = item;
            }
        }
        if (escolhido != null) {
            combo.setSelectedItem(escolhido);
        }
    }

    // ===================== BUSCAS =====================
    private Aluno buscarAluno(int id) {
        for (Aluno a : alunos) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    private Curso buscarCurso(int id) {
        for (Curso c : cursos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private Instrumento buscarInstrumento(int id) {
        for (Instrumento i : instrumentos) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    private Professor buscarProfessor(int id) {
        for (Professor p : professores) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private <T> T selecionado(JComboBox<T> combo) {
        int i = combo.getSelectedIndex();
        return i < 0 ? null : combo.getItemAt(i);
    }

    private String moeda(double valor) {
        return String.format(BR, "R$ %.2f", valor);
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    // ===================== CRUD GENERICO (uma aba por cadastro) =====================
    private abstract class Crud<T> {
        final List<T> lista;
        final String entidade;
        final DefaultTableModel modelo;
        final JTable tabela;
        int proximoId = 1;

        Crud(List<T> lista, String entidade, String... colunas) {
            this.lista = lista;
            this.entidade = entidade;
            this.modelo = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int linha, int coluna) {
                    return false;
                }
            };
            this.tabela = criarTabela(modelo);
            this.tabela.getColumnModel().getColumn(0).setMaxWidth(60);
            this.tabela.getSelectionModel().addListSelectionListener(e -> {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                int linha = tabela.getSelectedRow();
                if (linha >= 0 && linha < lista.size()) {
                    preencher(lista.get(linha));
                }
            });
        }

        // Le os campos da tela (retorna null se algum campo estiver invalido)
        abstract T ler();

        abstract void preencher(T item);

        abstract void limparCampos();

        abstract int getId(T item);

        abstract void setId(T item, int id);

        abstract Object[] linha(T item);

        // Retorna uma mensagem se o item nao puder ser excluido (ou null se puder)
        String bloqueioExclusao(T item) {
            return null;
        }

        void incluir(T item) {
            setId(item, proximoId++);
            lista.add(item);
        }

        void adicionar() {
            T novo = ler();
            if (novo == null) {
                return;
            }
            incluir(novo);
            limpar();
            atualizarTudo();
        }

        void alterar() {
            int linha = tabela.getSelectedRow();
            if (linha < 0 || linha >= lista.size()) {
                aviso("Selecione um registro na tabela para alterar.");
                return;
            }
            T novo = ler();
            if (novo == null) {
                return;
            }
            setId(novo, getId(lista.get(linha)));
            lista.set(linha, novo);
            limpar();
            atualizarTudo();
        }

        void excluir() {
            int linha = tabela.getSelectedRow();
            if (linha < 0 || linha >= lista.size()) {
                aviso("Selecione um registro na tabela para excluir.");
                return;
            }
            String bloqueio = bloqueioExclusao(lista.get(linha));
            if (bloqueio != null) {
                aviso(bloqueio);
                return;
            }
            int r = JOptionPane.showConfirmDialog(TelaDemonstracao.this,
                    "Deseja realmente excluir este registro?", "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
            lista.remove(linha);
            limpar();
            atualizarTudo();
        }

        void limpar() {
            tabela.clearSelection();
            limparCampos();
        }

        void recarregarTabela() {
            modelo.setRowCount(0);
            for (T item : lista) {
                modelo.addRow(linha(item));
            }
        }

        // Monta a aba: cartao do formulario (esquerda) + cartao da tabela (direita)
        JPanel montarAba(String tituloForm, String tituloLista, JPanel form) {
            JPanel botoes = new JPanel(new GridLayout(2, 2, 8, 8));
            botoes.setOpaque(false);
            botoes.add(new Botao("Adicionar", ROXO, Color.WHITE, e -> adicionar()));
            botoes.add(new Botao("Alterar", AZUL, Color.WHITE, e -> alterar()));
            botoes.add(new Botao("Excluir", VERMELHO, Color.WHITE, e -> excluir()));
            botoes.add(new Botao("Limpar", NEUTRO, Color.WHITE, e -> limpar()));

            JPanel pilha = new JPanel(new BorderLayout(0, 6));
            pilha.setOpaque(false);
            pilha.add(form, BorderLayout.NORTH);
            pilha.add(botoes, BorderLayout.CENTER);

            JPanel corpo = new JPanel(new BorderLayout());
            corpo.setOpaque(false);
            corpo.add(pilha, BorderLayout.NORTH);

            JScrollPane rolagemForm = new JScrollPane(corpo,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            rolagemForm.setBorder(null);
            rolagemForm.setOpaque(false);
            rolagemForm.getViewport().setOpaque(false);
            rolagemForm.getVerticalScrollBar().setUnitIncrement(16);

            Cartao esquerda = new Cartao(tituloForm);
            esquerda.add(rolagemForm, BorderLayout.CENTER);
            esquerda.setPreferredSize(new Dimension(350, 0));

            Cartao direita = new Cartao(tituloLista);
            direita.add(rolagem(tabela), BorderLayout.CENTER);

            JPanel aba = new JPanel(new BorderLayout(16, 0));
            aba.setOpaque(false);
            aba.setBorder(new EmptyBorder(14, 0, 0, 0));
            aba.add(esquerda, BorderLayout.WEST);
            aba.add(direita, BorderLayout.CENTER);
            return aba;
        }
    }

    // ----- Instrumento -----
    private class CrudInstrumento extends Crud<Instrumento> {
        private final JTextField txtNome = campo();
        private final JComboBox<String> cbTipo = comboBox();

        CrudInstrumento() {
            super(instrumentos, "Instrumento", "ID", "Nome", "Tipo");
            for (String tipo : new String[]{"CORDAS", "SOPRO", "PERCUSSAO", "TECLAS"}) {
                cbTipo.addItem(tipo);
            }
        }

        JPanel criarAba() {
            JPanel form = formulario();
            linhaForm(form, "Nome", txtNome);
            linhaForm(form, "Tipo", cbTipo);
            return montarAba("Dados do instrumento", "Instrumentos cadastrados", form);
        }

        @Override
        Instrumento ler() {
            if (Validador.campoVazio(txtNome.getText())) {
                aviso("Informe o nome do instrumento.");
                return null;
            }
            return new Instrumento(0, txtNome.getText().trim(), (String) cbTipo.getSelectedItem());
        }

        @Override
        void preencher(Instrumento i) {
            txtNome.setText(i.getNome());
            cbTipo.setSelectedItem(i.getTipo());
        }

        @Override
        void limparCampos() {
            txtNome.setText("");
            cbTipo.setSelectedIndex(0);
        }

        @Override
        int getId(Instrumento i) {
            return i.getId();
        }

        @Override
        void setId(Instrumento i, int id) {
            i.setId(id);
        }

        @Override
        Object[] linha(Instrumento i) {
            return new Object[]{i.getId(), i.getNome(), i.getTipo()};
        }

        @Override
        String bloqueioExclusao(Instrumento i) {
            for (Curso c : cursos) {
                if (c.getIdInstrumento() == i.getId()) {
                    return "Existem cursos usando este instrumento.";
                }
            }
            return null;
        }
    }

    // ----- Professor -----
    private class CrudProfessor extends Crud<Professor> {
        private final JTextField txtNome = campo();
        private final JTextField txtTelefone = campo();
        private final JTextField txtEspecialidade = campo();

        CrudProfessor() {
            super(professores, "Professor", "ID", "Nome", "Telefone", "Especialidade");
        }

        JPanel criarAba() {
            JPanel form = formulario();
            linhaForm(form, "Nome", txtNome);
            linhaForm(form, "Telefone", txtTelefone);
            linhaForm(form, "Especialidade", txtEspecialidade);
            return montarAba("Dados do professor", "Professores cadastrados", form);
        }

        @Override
        Professor ler() {
            if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtTelefone.getText())
                    || Validador.campoVazio(txtEspecialidade.getText())) {
                aviso("Preencha o nome, o telefone e a especialidade.");
                return null;
            }
            return new Professor(0, txtNome.getText().trim(), txtTelefone.getText().trim(),
                    txtEspecialidade.getText().trim());
        }

        @Override
        void preencher(Professor p) {
            txtNome.setText(p.getNome());
            txtTelefone.setText(p.getTelefone());
            txtEspecialidade.setText(p.getEspecialidade());
        }

        @Override
        void limparCampos() {
            txtNome.setText("");
            txtTelefone.setText("");
            txtEspecialidade.setText("");
        }

        @Override
        int getId(Professor p) {
            return p.getId();
        }

        @Override
        void setId(Professor p, int id) {
            p.setId(id);
        }

        @Override
        Object[] linha(Professor p) {
            return new Object[]{p.getId(), p.getNome(), p.getTelefone(), p.getEspecialidade()};
        }

        @Override
        String bloqueioExclusao(Professor p) {
            for (Curso c : cursos) {
                if (c.getIdProfessor() == p.getId()) {
                    return "Este professor ministra cursos. Troque o professor dos cursos antes de excluir.";
                }
            }
            return null;
        }
    }

    // ----- Aluno -----
    private class CrudAluno extends Crud<Aluno> {
        private final JTextField txtNome = campo();
        private final JTextField txtIdade = campo();
        private final JTextField txtTelefone = campo();
        private final JTextField txtEmail = campo();

        CrudAluno() {
            super(alunos, "Aluno", "ID", "Nome", "Idade", "Telefone", "E-mail");
        }

        JPanel criarAba() {
            JPanel form = formulario();
            linhaForm(form, "Nome", txtNome);
            linhaForm(form, "Idade", txtIdade);
            linhaForm(form, "Telefone", txtTelefone);
            linhaForm(form, "E-mail", txtEmail);
            return montarAba("Dados do aluno", "Alunos cadastrados", form);
        }

        @Override
        Aluno ler() {
            if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtTelefone.getText())) {
                aviso("Preencha o nome e o telefone.");
                return null;
            }
            if (!Validador.ehInteiro(txtIdade.getText()) || Integer.parseInt(txtIdade.getText().trim()) <= 0) {
                aviso("A idade precisa ser um numero inteiro maior que zero.");
                return null;
            }
            if (!Validador.emailValido(txtEmail.getText())) {
                aviso("Digite um e-mail valido.");
                return null;
            }
            return new Aluno(0, txtNome.getText().trim(), Integer.parseInt(txtIdade.getText().trim()),
                    txtTelefone.getText().trim(), txtEmail.getText().trim());
        }

        @Override
        void preencher(Aluno a) {
            txtNome.setText(a.getNome());
            txtIdade.setText(String.valueOf(a.getIdade()));
            txtTelefone.setText(a.getTelefone());
            txtEmail.setText(a.getEmail());
        }

        @Override
        void limparCampos() {
            txtNome.setText("");
            txtIdade.setText("");
            txtTelefone.setText("");
            txtEmail.setText("");
        }

        @Override
        int getId(Aluno a) {
            return a.getId();
        }

        @Override
        void setId(Aluno a, int id) {
            a.setId(id);
        }

        @Override
        Object[] linha(Aluno a) {
            return new Object[]{a.getId(), a.getNome(), a.getIdade(), a.getTelefone(), a.getEmail()};
        }

        @Override
        String bloqueioExclusao(Aluno a) {
            for (Matricula m : matriculas) {
                if (m.getIdAluno() == a.getId()) {
                    return "Nao e possivel excluir: o aluno possui matriculas.";
                }
            }
            return null;
        }
    }

    // ----- Curso -----
    private class CrudCurso extends Crud<Curso> {
        private final JTextField txtNome = campo();
        private final JTextField txtMensalidade = campo();
        private final JTextField txtDiaHorario = campo();
        private final JSpinner spMeses = new JSpinner(new SpinnerNumberModel(6, 1, 60, 1));
        private final JLabel lblSimulacao = new JLabel(" ");

        CrudCurso() {
            super(cursos, "Curso", "ID", "Curso", "Instrumento", "Professor", "Dia/Horario", "Mensalidade");
            spMeses.setFont(FONTE);
            spMeses.setPreferredSize(new Dimension(70, 32));
            lblSimulacao.setFont(FONTE_NEGRITO);
            lblSimulacao.setForeground(ROXO);
            spMeses.addChangeListener(e -> atualizarSimulacao());
        }

        JPanel criarAba() {
            JPanel simulacao = new JPanel(new BorderLayout(10, 0));
            simulacao.setOpaque(false);
            simulacao.add(spMeses, BorderLayout.WEST);
            simulacao.add(lblSimulacao, BorderLayout.CENTER);

            JPanel form = formulario();
            linhaForm(form, "Nome do curso", txtNome);
            linhaForm(form, "Mensalidade (R$)", txtMensalidade);
            linhaForm(form, "Dia e horario (ex: Segunda 14:00)", txtDiaHorario);
            linhaForm(form, "Instrumento", cbCursoInstrumento);
            linhaForm(form, "Professor", cbCursoProfessor);
            linhaForm(form, "Simular valor total (meses)", simulacao);
            atualizarSimulacao();
            return montarAba("Dados do curso", "Cursos cadastrados", form);
        }

        // Usa Curso.calcularValorTotal(meses) para o curso selecionado na tabela
        private void atualizarSimulacao() {
            int linha = tabela.getSelectedRow();
            if (linha < 0 || linha >= lista.size()) {
                lblSimulacao.setText("Selecione um curso");
                return;
            }
            int meses = (Integer) spMeses.getValue();
            lblSimulacao.setText(meses + " meses: " + moeda(lista.get(linha).calcularValorTotal(meses)));
        }

        @Override
        Curso ler() {
            Instrumento instrumento = selecionado(cbCursoInstrumento);
            Professor professor = selecionado(cbCursoProfessor);
            if (instrumento == null || professor == null) {
                aviso("Cadastre um instrumento e um professor antes de cadastrar o curso.");
                return null;
            }
            if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtDiaHorario.getText())) {
                aviso("Preencha o nome do curso e o dia/horario.");
                return null;
            }
            if (!Validador.ehDecimal(txtMensalidade.getText())
                    || Validador.paraDecimal(txtMensalidade.getText()) <= 0) {
                aviso("Informe uma mensalidade valida (maior que zero).");
                return null;
            }
            return new Curso(0, txtNome.getText().trim(), Validador.paraDecimal(txtMensalidade.getText()),
                    txtDiaHorario.getText().trim(), instrumento.getId(), professor.getId());
        }

        @Override
        void preencher(Curso c) {
            txtNome.setText(c.getNome());
            txtMensalidade.setText(String.format(BR, "%.2f", c.getValorMensalidade()));
            txtDiaHorario.setText(c.getDiaHorario());
            cbCursoInstrumento.setSelectedItem(buscarInstrumento(c.getIdInstrumento()));
            cbCursoProfessor.setSelectedItem(buscarProfessor(c.getIdProfessor()));
            atualizarSimulacao();
        }

        @Override
        void limparCampos() {
            txtNome.setText("");
            txtMensalidade.setText("");
            txtDiaHorario.setText("");
            atualizarSimulacao();
        }

        @Override
        int getId(Curso c) {
            return c.getId();
        }

        @Override
        void setId(Curso c, int id) {
            c.setId(id);
        }

        @Override
        Object[] linha(Curso c) {
            Instrumento i = buscarInstrumento(c.getIdInstrumento());
            Professor p = buscarProfessor(c.getIdProfessor());
            return new Object[]{c.getId(), c.getNome(), i == null ? "?" : i.getNome(),
                p == null ? "?" : p.getNome(), c.getDiaHorario(), moeda(c.getValorMensalidade())};
        }

        @Override
        String bloqueioExclusao(Curso c) {
            for (Matricula m : matriculas) {
                if (m.getIdCurso() == c.getId()) {
                    return "Nao e possivel excluir: o curso possui matriculas.";
                }
            }
            return null;
        }
    }

    // ===================== FABRICAS DE COMPONENTES ESTILIZADOS =====================
    private JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(FONTE);
        t.setForeground(TEXTO);
        t.setBorder(bordaCampo(false));
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                t.setBorder(bordaCampo(true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                t.setBorder(bordaCampo(false));
            }
        });
        return t;
    }

    private Border bordaCampo(boolean foco) {
        return new CompoundBorder(
                new LineBorder(foco ? ROXO : BORDA, foco ? 2 : 1, true),
                new EmptyBorder(foco ? 5 : 6, foco ? 7 : 8, foco ? 5 : 6, foco ? 7 : 8));
    }

    private <T> JComboBox<T> comboBox() {
        JComboBox<T> c = new JComboBox<>();
        c.setUI(new ComboUI());
        c.setFont(FONTE);
        c.setBackground(Color.WHITE);
        c.setForeground(TEXTO);
        c.setBorder(new LineBorder(BORDA, 1, true));
        c.setPreferredSize(new Dimension(100, 34));
        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                    boolean selecionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, selecionado, false);
                setBorder(new EmptyBorder(4, 8, 4, 8));
                setBackground(selecionado && indice >= 0 ? ROXO : Color.WHITE);
                setForeground(selecionado && indice >= 0 ? Color.WHITE : TEXTO);
                return this;
            }
        });
        return c;
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    private JPanel formulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        return p;
    }

    // Adiciona "rotulo em cima + campo embaixo" no formulario
    private void linhaForm(JPanel form, String texto, java.awt.Component campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridy = form.getComponentCount();
        c.insets = new Insets(0, 0, 3, 0);
        form.add(rotulo(texto), c);
        c.gridy = form.getComponentCount();
        c.insets = new Insets(0, 0, 8, 0);
        form.add(campo, c);
    }

    private JScrollPane rolagem(Component componente) {
        JScrollPane s = new JScrollPane(componente);
        s.setBorder(new LineBorder(BORDA));
        s.getViewport().setBackground(Color.WHITE);
        return s;
    }

    private JTable criarTabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(30);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFont(FONTE);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setDefaultRenderer(Object.class, new LinhaRenderer());
        JTableHeader cab = t.getTableHeader();
        cab.setDefaultRenderer(new CabecalhoTabelaRenderer());
        cab.setReorderingAllowed(false);
        cab.setPreferredSize(new Dimension(100, 34));
        return t;
    }

    // ===================== CLASSES VISUAIS =====================

    // Faixa do topo: degrade roxo, icone de nota musical e teclas de piano
    private static class Cabecalho extends JPanel {

        Cabecalho() {
            setPreferredSize(new Dimension(100, 92));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            g2.setPaint(new GradientPaint(0, 0, ROXO_ESCURO, w, h, ROXO));
            g2.fillRect(0, 0, w, h);

            // teclas de piano (so desenha se tiver espaco)
            int teclas = 14;
            int larg = 30;
            int x0 = w - teclas * larg - 28;
            int topo = 28;
            if (x0 > 560) {
                g2.setColor(new Color(255, 255, 255, 225));
                for (int i = 0; i < teclas; i++) {
                    g2.fillRoundRect(x0 + i * larg + 1, topo, larg - 2, h - topo + 8, 8, 8);
                }
                g2.setColor(new Color(0x1B1238));
                for (int i = 0; i < teclas - 1; i++) {
                    int m = i % 7;
                    if (m != 2 && m != 6) {
                        g2.fillRoundRect(x0 + (i + 1) * larg - 9, topo, 18, 34, 5, 5);
                    }
                }
            }

            // icone: circulo dourado com nota musical
            int d = 56;
            int bx = 28;
            int by = (h - 4 - d) / 2;
            g2.setColor(DOURADO);
            g2.fillOval(bx, by, d, d);
            g2.setColor(ROXO_ESCURO);
            g2.fillOval(bx + 14, by + 32, 16, 11);
            g2.fillRect(bx + 28, by + 13, 3, 26);
            g2.setStroke(new BasicStroke(3f));
            g2.draw(new QuadCurve2D.Float(bx + 30, by + 13, bx + 39, by + 20, bx + 40, by + 31));

            // titulo e subtitulo
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            g2.drawString("Escola Musical", bx + d + 18, 44);
            g2.setColor(new Color(0xD8CCF5));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString("Gestao de alunos, professores, cursos e matriculas", bx + d + 20, 66);

            // linha dourada embaixo
            g2.setColor(DOURADO);
            g2.fillRect(0, h - 4, w, 4);
            g2.dispose();
        }
    }

    // Painel branco com cantos arredondados, sombra leve e titulo
    private static class Cartao extends JPanel {

        Cartao(String titulo) {
            super(new BorderLayout(0, 12));
            setOpaque(false);
            setBorder(new EmptyBorder(16, 18, 16, 18));
            JLabel l = new JLabel(titulo);
            l.setFont(new Font("SansSerif", Font.BOLD, 16));
            l.setForeground(ROXO_ESCURO);
            l.setBorder(new CompoundBorder(new javax.swing.border.MatteBorder(0, 0, 2, 0, DOURADO),
                    new EmptyBorder(0, 0, 6, 0)));
            JPanel faixa = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            faixa.setOpaque(false);
            faixa.add(l);
            add(faixa, BorderLayout.NORTH);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(2, 3, w - 4, h - 4, 20, 20);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 3, h - 4, 20, 20);
            g2.setColor(BORDA);
            g2.drawRoundRect(0, 0, w - 4, h - 5, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Botao arredondado com efeito ao passar o mouse
    private static class Botao extends JButton {
        private final Color base;
        private boolean sobre = false;

        Botao(String texto, Color base, Color corTexto, ActionListener acao) {
            super(texto);
            this.base = base;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(corTexto);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(9, 16, 9, 16));
            addActionListener(acao);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    sobre = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    sobre = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color cor = base;
            if (getModel().isPressed()) {
                cor = base.darker();
            } else if (sobre) {
                cor = base.brighter();
            }
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Combo com seta simples (sem o visual 3D padrao)
    private static class ComboUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            BasicArrowButton b = new BasicArrowButton(SwingConstants.SOUTH, ROXO_CLARO, ROXO_CLARO, ROXO, ROXO_CLARO);
            b.setBorder(new EmptyBorder(0, 0, 0, 0));
            return b;
        }
    }

    // Abas planas e arredondadas
    private static class AbasUI extends BasicTabbedPaneUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabInsets = new Insets(9, 20, 9, 20);
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            tabAreaInsets = new Insets(0, 0, 4, 0);
            contentBorderInsets = new Insets(0, 0, 0, 0);
        }

        @Override
        protected Insets getContentBorderInsets(int tabPlacement) {
            return new Insets(0, 0, 0, 0);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isSelected ? ROXO : ROXO_CLARO);
            g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 14, 14);
            g2.dispose();
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                int x, int y, int w, int h, boolean isSelected) {
            // sem borda
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // sem borda
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, java.awt.Rectangle[] rects,
                int tabIndex, java.awt.Rectangle iconRect, java.awt.Rectangle textRect, boolean isSelected) {
            // sem indicador de foco
        }
    }

    // Linhas da tabela: zebrado e selecao em lilas
    private static class LinhaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean selecionada,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(t, valor, selecionada, false, linha, coluna);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setForeground(TEXTO);
            setBackground(selecionada ? new Color(0xDCD0F7)
                    : (linha % 2 == 0 ? Color.WHITE : new Color(0xF8F6FD)));
            return this;
        }
    }

    // Coluna de status da matricula com cores
    private static class StatusRenderer extends LinhaRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean selecionada,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(t, valor, selecionada, foco, linha, coluna);
            String status = String.valueOf(valor);
            setFont(FONTE_NEGRITO);
            if (Matricula.ATIVA.equals(status)) {
                setForeground(VERDE);
            } else if (Matricula.TRANCADA.equals(status)) {
                setForeground(AMARELO);
            } else {
                setForeground(VERMELHO);
            }
            return this;
        }
    }

    private static class CabecalhoTabelaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean selecionada,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(t, valor, false, false, linha, coluna);
            setBackground(ROXO);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setHorizontalAlignment(LEFT);
            return this;
        }
    }
}