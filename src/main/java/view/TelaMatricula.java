package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.AlunoDAO;
import dao.CursoDAO;
import dao.MatriculaDAO;
import model.Aluno;
import model.Curso;
import model.Matricula;

// Tela de CRUD de Matricula. A matricula liga um Aluno a um Curso.
public class TelaMatricula extends JFrame {

    private final MatriculaDAO dao = new MatriculaDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private List<Matricula> matriculas = new ArrayList<>();
    private int idSelecionado = 0;

    private JComboBox<Aluno> comboAluno;
    private JComboBox<Curso> comboCurso;
    private JComboBox<String> comboStatus;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaMatricula() {
        setTitle("Matriculas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        initComponents();
        carregarCombos();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados da Matricula"));
        comboAluno = new JComboBox<>();
        comboCurso = new JComboBox<>();
        comboStatus = new JComboBox<>(new String[]{Matricula.ATIVA, Matricula.TRANCADA, Matricula.CANCELADA});
        form.add(new JLabel("Aluno:"));
        form.add(comboAluno);
        form.add(new JLabel("Curso:"));
        form.add(comboCurso);
        form.add(new JLabel("Status:"));
        form.add(comboStatus);
        add(form, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Aluno", "Curso", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinha();
            }
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnCadastrar = new JButton("Matricular");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        btnCadastrar.addActionListener(e -> cadastrar());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());
        botoes.add(btnCadastrar);
        botoes.add(btnAlterar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarCombos() {
        try {
            comboAluno.removeAllItems();
            for (Aluno a : alunoDAO.listar()) {
                comboAluno.addItem(a);
            }
            comboCurso.removeAllItems();
            for (Curso c : cursoDAO.listar()) {
                comboCurso.addItem(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos/cursos: " + e.getMessage());
        }
    }

    private Matricula lerCampos() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        Curso curso = (Curso) comboCurso.getSelectedItem();
        if (aluno == null || curso == null) {
            JOptionPane.showMessageDialog(this, "Cadastre pelo menos um aluno e um curso antes.");
            return null;
        }
        Matricula m = new Matricula();
        m.setIdAluno(aluno.getId());
        m.setIdCurso(curso.getId());
        m.setStatus((String) comboStatus.getSelectedItem());
        return m;
    }

    private void cadastrar() {
        Matricula m = lerCampos();
        if (m == null) {
            return;
        }
        try {
            dao.inserir(m);
            JOptionPane.showMessageDialog(this, "Matricula realizada com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao matricular: " + e.getMessage());
        }
    }

    private void alterar() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma matricula na tabela.");
            return;
        }
        Matricula m = lerCampos();
        if (m == null) {
            return;
        }
        m.setId(idSelecionado);
        try {
            dao.atualizar(m);
            JOptionPane.showMessageDialog(this, "Matricula alterada com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma matricula na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta matricula?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(idSelecionado);
            JOptionPane.showMessageDialog(this, "Matricula excluida com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            matriculas = dao.listar();
            modelo.setRowCount(0);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Matricula m : matriculas) {
                modelo.addRow(new Object[]{m.getId(), m.getDataMatricula().format(formato),
                    m.getNomeAluno(), m.getNomeCurso(), m.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar matriculas: " + e.getMessage());
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Matricula m = matriculas.get(linha);
        idSelecionado = m.getId();
        comboStatus.setSelectedItem(m.getStatus());
        for (int i = 0; i < comboAluno.getItemCount(); i++) {
            if (comboAluno.getItemAt(i).getId() == m.getIdAluno()) {
                comboAluno.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < comboCurso.getItemCount(); i++) {
            if (comboCurso.getItemAt(i).getId() == m.getIdCurso()) {
                comboCurso.setSelectedIndex(i);
            }
        }
    }

    private void limparCampos() {
        idSelecionado = 0;
        comboStatus.setSelectedIndex(0);
        tabela.clearSelection();
    }
}
