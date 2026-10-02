package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Tela inicial (menu) do sistema. Cada botao abre uma tela de cadastro.
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Escola de Musica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Escola de Musica", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton btnAluno = new JButton("Alunos");
        JButton btnProfessor = new JButton("Professores");
        JButton btnInstrumento = new JButton("Instrumentos");
        JButton btnCurso = new JButton("Cursos");
        JButton btnMatricula = new JButton("Matriculas");

        btnAluno.addActionListener(e -> new TelaAluno().setVisible(true));
        btnProfessor.addActionListener(e -> new TelaProfessor().setVisible(true));
        btnInstrumento.addActionListener(e -> new TelaInstrumento().setVisible(true));
        btnCurso.addActionListener(e -> new TelaCurso().setVisible(true));
        btnMatricula.addActionListener(e -> new TelaMatricula().setVisible(true));

        menu.add(btnAluno);
        menu.add(btnProfessor);
        menu.add(btnInstrumento);
        menu.add(btnCurso);
        menu.add(btnMatricula);
        add(menu, BorderLayout.CENTER);
    }
}
