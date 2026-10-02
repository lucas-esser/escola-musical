import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import util.Conexao;
import view.TelaPrincipal;

// Classe que inicia o programa
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Antes de abrir o sistema, verifica se o banco esta ligado
            if (!Conexao.testarConexao()) {
                JOptionPane.showMessageDialog(null,
                        "Nao foi possivel conectar ao banco de dados.\n"
                        + "Verifique se o MySQL esta ligado e se o script\n"
                        + "04_Banco_de_Dados/criacao_banco.sql foi executado.",
                        "Erro de conexao", JOptionPane.ERROR_MESSAGE);
                return;
            }
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
