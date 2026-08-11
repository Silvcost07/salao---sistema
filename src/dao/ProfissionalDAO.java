package dao;

import conexao.ConexaoBD;
import model.Profissional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalDAO {

    public void inserir(Profissional profissional) {
        String sql = "INSERT INTO profissionais (nome, comissao_percentual) VALUES (?, ?)";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, profissional.getNome());
            stmt.setDouble(2, profissional.getComissaoPercentual());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    profissional.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir profissional: " + e.getMessage());
        }
    }

    public void atualizar(Profissional profissional) {
        String sql = "UPDATE profissionais SET nome = ?, comissao_percentual = ? WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, profissional.getNome());
            stmt.setDouble(2, profissional.getComissaoPercentual());
            stmt.setInt(3, profissional.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar profissional: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM profissionais WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir profissional: " + e.getMessage());
        }
    }

    public List<Profissional> listarTodos() {
        List<Profissional> lista = new ArrayList<>();
        String sql = "SELECT * FROM profissionais ORDER BY nome";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar profissionais: " + e.getMessage());
        }
        return lista;
    }

    private Profissional mapear(ResultSet rs) throws SQLException {
        Profissional p = new Profissional();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setComissaoPercentual(rs.getDouble("comissao_percentual"));
        return p;
    }
}