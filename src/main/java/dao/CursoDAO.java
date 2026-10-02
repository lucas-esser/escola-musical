package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Curso;
import util.Conexao;

public class CursoDAO {

    public void inserir(Curso c) throws SQLException {
        String sql = "INSERT INTO tb_curso (nome, valor_mensalidade, dia_horario, id_instrumento, id_professor) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setDouble(2, c.getValorMensalidade());
            ps.setString(3, c.getDiaHorario());
            ps.setInt(4, c.getIdInstrumento());
            ps.setInt(5, c.getIdProfessor());
            ps.executeUpdate();
        }
    }

    // O JOIN serve para trazer tambem o nome do instrumento e do professor
    public List<Curso> listar() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT c.*, i.nome AS nome_instrumento, p.nome AS nome_professor "
                + "FROM tb_curso c "
                + "JOIN tb_instrumento i ON c.id_instrumento = i.id_instrumento "
                + "JOIN tb_professor p ON c.id_professor = p.id_professor "
                + "ORDER BY c.nome";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Curso c = new Curso();
                c.setId(rs.getInt("id_curso"));
                c.setNome(rs.getString("nome"));
                c.setValorMensalidade(rs.getDouble("valor_mensalidade"));
                c.setDiaHorario(rs.getString("dia_horario"));
                c.setIdInstrumento(rs.getInt("id_instrumento"));
                c.setIdProfessor(rs.getInt("id_professor"));
                c.setNomeInstrumento(rs.getString("nome_instrumento"));
                c.setNomeProfessor(rs.getString("nome_professor"));
                lista.add(c);
            }
        }
        return lista;
    }

    public void atualizar(Curso c) throws SQLException {
        String sql = "UPDATE tb_curso SET nome = ?, valor_mensalidade = ?, dia_horario = ?, "
                + "id_instrumento = ?, id_professor = ? WHERE id_curso = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setDouble(2, c.getValorMensalidade());
            ps.setString(3, c.getDiaHorario());
            ps.setInt(4, c.getIdInstrumento());
            ps.setInt(5, c.getIdProfessor());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_curso WHERE id_curso = ?";
        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
