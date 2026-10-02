package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Professor;
import util.Conexao;

public class ProfessorDAO {

    public void inserir(Professor p) throws SQLException {
        String sql = "INSERT INTO tb_professor (nome, telefone, especialidade) VALUES (?, ?, ?)";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getEspecialidade());
            ps.executeUpdate();
        }
    }

    public List<Professor> listar() throws SQLException {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_professor ORDER BY nome";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Professor p = new Professor();
                p.setId(rs.getInt("id_professor"));
                p.setNome(rs.getString("nome"));
                p.setTelefone(rs.getString("telefone"));
                p.setEspecialidade(rs.getString("especialidade"));
                lista.add(p);
            }
        }
        return lista;
    }

    public void atualizar(Professor p) throws SQLException {
        String sql = "UPDATE tb_professor SET nome = ?, telefone = ?, especialidade = ? WHERE id_professor = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getTelefone());
            ps.setString(3, p.getEspecialidade());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_professor WHERE id_professor = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
