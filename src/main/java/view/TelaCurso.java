package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.CursoDAO;
import dao.InstrumentoDAO;
import dao.ProfessorDAO;
import model.Curso;
import model.Instrumento;
import model.Professor;
import util.Validador;

// Tela de CRUD de Curso. Cada curso escolhe um Instrumento e um Professor.
public class TelaCurso extends JFrame {

    private final CursoDAO dao = new CursoDAO();
    private final InstrumentoDAO instrumentoDAO = new InstrumentoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private List<Curso> cursos = new ArrayList<>();
    private int idSelecionado = 0;

    private JTextField txtNome;
    private JTextField txtValor;
    private JTextField txtDiaHorario;
    private JComboBox<Instrumento> comboInstrumento;
    private JComboBox<Professor> comboProfessor;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaCurso() {
        setTitle("Cadastro de Cursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        initComponents();
        carregarCombos();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Curso"));
        txtNome = new JTextField();
        txtValor = new JTextField();
        txtDiaHorario = new JTextField();
        comboInstrumento = new JComboBox<>();
        comboProfessor = new JComboBox<>();
        form.add(new JLabel("Nome do curso:"));
        form.add(txtNome);
        form.add(new JLabel("Mensalidade (R$):"));
        form.add(txtValor);
        form.add(new JLabel("Dia e horario (ex: Segunda 14:00):"));
        form.add(txtDiaHorario);
        form.add(new JLabel("Instrumento:"));
        form.add(comboInstrumento);
        form.add(new JLabel("Professor:"));
        form.add(comboProfessor);
        add(form, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Curso", "Mensalidade", "Dia/Horario", "Instrumento", "Professor"}, 0) {
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
        JButton btnCadastrar = new JButton("Cadastrar");
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

    // Preenche os dois JComboBox com os dados que ja existem no banco
    private void carregarCombos() {
        try {
            comboInstrumento.removeAllItems();
            for (Instrumento i : instrumentoDAO.listar()) {
                comboInstrumento.addItem(i);
            }
            comboProfessor.removeAllItems();
            for (Professor p : professorDAO.listar()) {
                comboProfessor.addItem(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar instrumentos/professores: " + e.getMessage());
        }
    }

    private Curso lerCampos() {
        if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtDiaHorario.getText())) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do curso e o dia/horario.");
            return null;
        }
        if (!Validador.ehDecimal(txtValor.getText()) || Validador.paraDecimal(txtValor.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "A mensalidade precisa ser um numero maior que zero.");
            return null;
        }
        Instrumento instrumento = (Instrumento) comboInstrumento.getSelectedItem();
        Professor professor = (Professor) comboProfessor.getSelectedItem();
        if (instrumento == null || professor == null) {
            JOptionPane.showMessageDialog(this, "Cadastre pelo menos um instrumento e um professor antes.");
            return null;
        }
        Curso c = new Curso();
        c.setNome(txtNome.getText().trim());
        c.setValorMensalidade(Validador.paraDecimal(txtValor.getText()));
        c.setDiaHorario(txtDiaHorario.getText().trim());
        c.setIdInstrumento(instrumento.getId());
        c.setIdProfessor(professor.getId());
        return c;
    }

    private void cadastrar() {
        Curso c = lerCampos();
        if (c == null) {
            return;
        }
        try {
            dao.inserir(c);
            JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void alterar() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um curso na tabela.");
            return;
        }
        Curso c = lerCampos();
        if (c == null) {
            return;
        }
        c.setId(idSelecionado);
        try {
            dao.atualizar(c);
            JOptionPane.showMessageDialog(this, "Curso alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um curso na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este curso?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(idSelecionado);
            JOptionPane.showMessageDialog(this, "Curso excluido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel excluir. Verifique se o curso possui matriculas.\n" + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            cursos = dao.listar();
            modelo.setRowCount(0);
            for (Curso c : cursos) {
                modelo.addRow(new Object[]{c.getId(), c.getNome(),
                    String.format("R$ %.2f", c.getValorMensalidade()),
                    c.getDiaHorario(), c.getNomeInstrumento(), c.getNomeProfessor()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage());
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Curso c = cursos.get(linha);
        idSelecionado = c.getId();
        txtNome.setText(c.getNome());
        txtValor.setText(String.valueOf(c.getValorMensalidade()));
        txtDiaHorario.setText(c.getDiaHorario());
        // procura nos combos o item com o mesmo id do curso
        for (int i = 0; i < comboInstrumento.getItemCount(); i++) {
            if (comboInstrumento.getItemAt(i).getId() == c.getIdInstrumento()) {
                comboInstrumento.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < comboProfessor.getItemCount(); i++) {
            if (comboProfessor.getItemAt(i).getId() == c.getIdProfessor()) {
                comboProfessor.setSelectedIndex(i);
            }
        }
    }

    private void limparCampos() {
        idSelecionado = 0;
        txtNome.setText("");
        txtValor.setText("");
        txtDiaHorario.setText("");
        tabela.clearSelection();
    }
}
