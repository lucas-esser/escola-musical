package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Matricula;
import util.Conexao;

public class MatriculaDAO {

    public void inserir(Matricula m) throws SQLException {
        String sql = "INSERT INTO tb_matricula (data_matricula, status, id_aluno, id_curso) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(m.getDataMatricula()));
            ps.setString(2, m.getStatus());
            ps.setInt(3, m.getIdAluno());
            ps.setInt(4, m.getIdCurso());
            ps.executeUpdate();
        }
    }

    // O JOIN serve para trazer tambem o nome do aluno e do curso
    public List<Matricula> listar() throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.*, a.nome AS nome_aluno, c.nome AS nome_curso "
                + "FROM tb_matricula m "
                + "JOIN tb_aluno a ON m.id_aluno = a.id_aluno "
                + "JOIN tb_curso c ON m.id_curso = c.id_curso "
                + "ORDER BY m.id_matricula";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matricula m = new Matricula();
                m.setId(rs.getInt("id_matricula"));
                m.setDataMatricula(rs.getDate("data_matricula").toLocalDate());
                m.setStatus(rs.getString("status"));
                m.setIdAluno(rs.getInt("id_aluno"));
                m.setIdCurso(rs.getInt("id_curso"));
                m.setNomeAluno(rs.getString("nome_aluno"));
                m.setNomeCurso(rs.getString("nome_curso"));
                lista.add(m);
            }
        }
        return lista;
    }

    public void atualizar(Matricula m) throws SQLException {
        String sql = "UPDATE tb_matricula SET status = ?, id_aluno = ?, id_curso = ? WHERE id_matricula = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getStatus());
            ps.setInt(2, m.getIdAluno());
            ps.setInt(3, m.getIdCurso());
            ps.setInt(4, m.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_matricula WHERE id_matricula = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
