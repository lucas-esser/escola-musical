package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Instrumento;
import util.Conexao;

public class InstrumentoDAO {

    public void inserir(Instrumento i) throws SQLException {
        String sql = "INSERT INTO tb_instrumento (nome, tipo) VALUES (?, ?)";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getTipo());
            ps.executeUpdate();
        }
    }

    public List<Instrumento> listar() throws SQLException {
        List<Instrumento> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_instrumento ORDER BY nome";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Instrumento i = new Instrumento();
                i.setId(rs.getInt("id_instrumento"));
                i.setNome(rs.getString("nome"));
                i.setTipo(rs.getString("tipo"));
                lista.add(i);
            }
        }
        return lista;
    }

    public void atualizar(Instrumento i) throws SQLException {
        String sql = "UPDATE tb_instrumento SET nome = ?, tipo = ? WHERE id_instrumento = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getTipo());
            ps.setInt(3, i.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_instrumento WHERE id_instrumento = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
