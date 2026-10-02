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

import dao.AlunoDAO;
import model.Aluno;
import util.Validador;

// Tela de CRUD de Aluno: cadastrar, listar, alterar e excluir
public class TelaAluno extends JFrame {

    private final AlunoDAO dao = new AlunoDAO();
    private List<Aluno> alunos = new ArrayList<>();
    private int idSelecionado = 0; // 0 = nenhum aluno selecionado na tabela

    private JTextField txtNome;
    private JTextField txtIdade;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaAluno() {
        setTitle("Cadastro de Alunos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario (parte de cima)
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Aluno"));
        txtNome = new JTextField();
        txtIdade = new JTextField();
        txtTelefone = new JTextField();
        txtEmail = new JTextField();
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Idade:"));
        form.add(txtIdade);
        form.add(new JLabel("Telefone:"));
        form.add(txtTelefone);
        form.add(new JLabel("E-mail:"));
        form.add(txtEmail);
        add(form, BorderLayout.NORTH);

        // Tabela (meio)
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Idade", "Telefone", "E-mail"}, 0) {
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

        // Botoes (parte de baixo)
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

    // Le os campos da tela e monta um objeto Aluno (retorna null se tiver erro)
    private Aluno lerCampos() {
        if (Validador.campoVazio(txtNome.getText()) || Validador.campoVazio(txtTelefone.getText())) {
            JOptionPane.showMessageDialog(this, "Preencha o nome e o telefone.");
            return null;
        }
        if (!Validador.ehInteiro(txtIdade.getText())) {
            JOptionPane.showMessageDialog(this, "A idade precisa ser um numero inteiro.");
            return null;
        }
        if (!Validador.emailValido(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Digite um e-mail valido.");
            return null;
        }
        Aluno a = new Aluno();
        a.setNome(txtNome.getText().trim());
        a.setIdade(Integer.parseInt(txtIdade.getText().trim()));
        a.setTelefone(txtTelefone.getText().trim());
        a.setEmail(txtEmail.getText().trim());
        return a;
    }

    private void cadastrar() {
        Aluno a = lerCampos();
        if (a == null) {
            return;
        }
        try {
            dao.inserir(a);
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void alterar() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }
        Aluno a = lerCampos();
        if (a == null) {
            return;
        }
        a.setId(idSelecionado);
        try {
            dao.atualizar(a);
            JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este aluno?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(idSelecionado);
            JOptionPane.showMessageDialog(this, "Aluno excluido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel excluir. Verifique se o aluno possui matriculas.\n" + e.getMessage());
        }
    }

    // Busca os alunos no banco e coloca na tabela
    private void carregarTabela() {
        try {
            alunos = dao.listar();
            modelo.setRowCount(0);
            for (Aluno a : alunos) {
                modelo.addRow(new Object[]{a.getId(), a.getNome(), a.getIdade(), a.getTelefone(), a.getEmail()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + e.getMessage());
        }
    }

    // Quando clica numa linha da tabela, os dados vao para os campos
    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Aluno a = alunos.get(linha);
        idSelecionado = a.getId();
        txtNome.setText(a.getNome());
        txtIdade.setText(String.valueOf(a.getIdade()));
        txtTelefone.setText(a.getTelefone());
        txtEmail.setText(a.getEmail());
    }

    private void limparCampos() {
        idSelecionado = 0;
        txtNome.setText("");
        txtIdade.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        tabela.clearSelection();
    }
}
