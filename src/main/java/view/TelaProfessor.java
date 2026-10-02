package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dao.ProfessorDAO;
import model.Professor;
import util.Validador;

// Tela de CRUD de Professor
public class TelaProfessor extends JFrame {

    private final ProfessorDAO dao = new ProfessorDAO();
    private List<Professor> professores = new ArrayList<>();
    private int idSelecionado = 0;

    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEspecialidade;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaProfessor() {
        setTitle("Cadastro de Professores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 470);
        setLocationRelativeTo(null);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Professor"));
        txtNome = new JTextField();
        txtTelefone = new JTextField();
        txtEspecialidade = new JTextField();
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Telefone:"));
        form.add(txtTelefone);
        form.add(new JLabel("Especialidade:"));
        form.add(txtEspecialidade);
        add(form, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Telefone", "Especialidade"}, 0) {
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

    private Professor lerCampos() {
        if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtTelefone.getText())
                || Validador.campoVazio(txtEspecialidade.getText())) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return null;
        }
        Professor p = new Professor();
        p.setNome(txtNome.getText().trim());
        p.setTelefone(txtTelefone.getText().trim());
        p.setEspecialidade(txtEspecialidade.getText().trim());
        return p;
    }

    private void cadastrar() {
        Professor p = lerCampos();
        if (p == null) {
            return;
        }
        try {
            dao.inserir(p);
            JOptionPane.showMessageDialog(this, "Professor cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void alterar() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um professor na tabela.");
            return;
        }
        Professor p = lerCampos();
        if (p == null) {
            return;
        }
        p.setId(idSelecionado);
        try {
            dao.atualizar(p);
            JOptionPane.showMessageDialog(this, "Professor alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um professor na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este professor?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(idSelecionado);
            JOptionPane.showMessageDialog(this, "Professor excluido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel excluir. Verifique se o professor ministra algum curso.\n" + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            professores = dao.listar();
            modelo.setRowCount(0);
            for (Professor p : professores) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getTelefone(), p.getEspecialidade()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar professores: " + e.getMessage());
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Professor p = professores.get(linha);
        idSelecionado = p.getId();
        txtNome.setText(p.getNome());
        txtTelefone.setText(p.getTelefone());
        txtEspecialidade.setText(p.getEspecialidade());
    }

    private void limparCampos() {
        idSelecionado = 0;
        txtNome.setText("");
        txtTelefone.setText("");
        txtEspecialidade.setText("");
        tabela.clearSelection();
    }
}
