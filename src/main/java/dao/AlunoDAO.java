package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import util.Conexao;

// DAO = classe que faz o CRUD (inserir, listar, atualizar, excluir) no banco
public class AlunoDAO {

    public void inserir(Aluno a) throws SQLException {
        String sql = "INSERT INTO tb_aluno (nome, idade, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setInt(2, a.getIdade());
            ps.setString(3, a.getTelefone());
            ps.setString(4, a.getEmail());
            ps.executeUpdate();
        }
    }

    public List<Aluno> listar() throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_aluno ORDER BY nome";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Aluno a = new Aluno();
                a.setId(rs.getInt("id_aluno"));
                a.setNome(rs.getString("nome"));
                a.setIdade(rs.getInt("idade"));
                a.setTelefone(rs.getString("telefone"));
                a.setEmail(rs.getString("email"));
                lista.add(a);
            }
        }
        return lista;
    }

    public void atualizar(Aluno a) throws SQLException {
        String sql = "UPDATE tb_aluno SET nome = ?, idade = ?, telefone = ?, email = ? WHERE id_aluno = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setInt(2, a.getIdade());
            ps.setString(3, a.getTelefone());
            ps.setString(4, a.getEmail());
            ps.setInt(5, a.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_aluno WHERE id_aluno = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
