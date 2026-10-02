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

import dao.InstrumentoDAO;
import model.Instrumento;
import util.Validador;

// Tela de CRUD de Instrumento
public class TelaInstrumento extends JFrame {

    private final InstrumentoDAO dao = new InstrumentoDAO();
    private List<Instrumento> instrumentos = new ArrayList<>();
    private int idSelecionado = 0;

    private JTextField txtNome;
    private JComboBox<String> comboTipo;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaInstrumento() {
        setTitle("Cadastro de Instrumentos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Instrumento"));
        txtNome = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"CORDAS", "SOPRO", "PERCUSSAO", "TECLAS"});
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Tipo:"));
        form.add(comboTipo);
        add(form, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo"}, 0) {
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

    private Instrumento lerCampos() {
        if (Validador.campoVazio(txtNome.getText())) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do instrumento.");
            return null;
        }
        Instrumento i = new Instrumento();
        i.setNome(txtNome.getText().trim());
        i.setTipo((String) comboTipo.getSelectedItem());
        return i;
    }

    private void cadastrar() {
        Instrumento i = lerCampos();
        if (i == null) {
            return;
        }
        try {
            dao.inserir(i);
            JOptionPane.showMessageDialog(this, "Instrumento cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void alterar() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um instrumento na tabela.");
            return;
        }
        Instrumento i = lerCampos();
        if (i == null) {
            return;
        }
        i.setId(idSelecionado);
        try {
            dao.atualizar(i);
            JOptionPane.showMessageDialog(this, "Instrumento alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um instrumento na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este instrumento?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(idSelecionado);
            JOptionPane.showMessageDialog(this, "Instrumento excluido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel excluir. Verifique se algum curso usa este instrumento.\n" + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            instrumentos = dao.listar();
            modelo.setRowCount(0);
            for (Instrumento i : instrumentos) {
                modelo.addRow(new Object[]{i.getId(), i.getNome(), i.getTipo()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar instrumentos: " + e.getMessage());
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Instrumento i = instrumentos.get(linha);
        idSelecionado = i.getId();
        txtNome.setText(i.getNome());
        comboTipo.setSelectedItem(i.getTipo());
    }

    private void limparCampos() {
        idSelecionado = 0;
        txtNome.setText("");
        comboTipo.setSelectedIndex(0);
        tabela.clearSelection();
    }
}
