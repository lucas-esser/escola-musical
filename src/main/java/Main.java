import javax.swing.SwingUtilities;

import view.TelaDemonstracao;

// Inicia a tela de demonstracao da Escola Musical (nao precisa de banco de dados)
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaDemonstracao tela = new TelaDemonstracao();
            tela.setVisible(true);
        });
    }
}