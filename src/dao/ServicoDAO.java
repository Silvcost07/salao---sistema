package dao;

import conexao.ConexaoBD;
import model.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public void inserir(Servico servico) {
        String sql = "INSERT INTO servicos (nome, preco, duracao_minutos) VALUES (?, ?, ?)";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setInt(3, servico.getDuracaoMinutos());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    servico.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir serviço: " + e.getMessage());
        }
    }

    public void atualizar(Servico servico) {
        String sql = "UPDATE servicos SET nome = ?, preco = ?, duracao_minutos = ? WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setInt(3, servico.getDuracaoMinutos());
            stmt.setInt(4, servico.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM servicos WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir serviço: " + e.getMessage());
        }
    }

    public List<Servico> listarTodos() {
        List<Servico> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicos ORDER BY nome";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar serviços: " + e.getMessage());
        }
        return lista;
    }

    private Servico mapear(ResultSet rs) throws SQLException {
        Servico s = new Servico();
        s.setId(rs.getInt("id"));
        s.setNome(rs.getString("nome"));
        s.setPreco(rs.getDouble("preco"));
        s.setDuracaoMinutos(rs.getInt("duracao_minutos"));
        return s;
    }
}