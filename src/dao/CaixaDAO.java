package dao;

import conexao.ConexaoBD;
import model.MovimentoCaixa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CaixaDAO {

    public void inserir(MovimentoCaixa movimento) {
        String sql = "INSERT INTO caixa (tipo, descricao, valor, agendamento_id) VALUES (?, ?, ?, ?)";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movimento.getTipo());
            stmt.setString(2, movimento.getDescricao());
            stmt.setDouble(3, movimento.getValor());

            if (movimento.getAgendamentoId() != null) {
                stmt.setInt(4, movimento.getAgendamentoId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movimento.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir movimento de caixa: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM caixa WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir movimento de caixa: " + e.getMessage());
        }
    }

    public List<MovimentoCaixa> listarTodos() {
        List<MovimentoCaixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM caixa ORDER BY data_movimento DESC";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar movimentos de caixa: " + e.getMessage());
        }
        return lista;
    }

    public double calcularSaldoTotal() {
        String sql = "SELECT SUM(CASE WHEN tipo = 'ENTRADA' THEN valor ELSE -valor END) as saldo FROM caixa";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("saldo");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular saldo: " + e.getMessage());
        }
        return 0.0;
    }

    private MovimentoCaixa mapear(ResultSet rs) throws SQLException {
        MovimentoCaixa m = new MovimentoCaixa();
        m.setId(rs.getInt("id"));
        m.setTipo(rs.getString("tipo"));
        m.setDescricao(rs.getString("descricao"));
        m.setValor(rs.getDouble("valor"));
        m.setDataMovimento(rs.getTimestamp("data_movimento").toLocalDateTime());

        int agendamentoId = rs.getInt("agendamento_id");
        if (!rs.wasNull()) {
            m.setAgendamentoId(agendamentoId);
        }

        return m;
    }
}