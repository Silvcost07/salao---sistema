package dao;

import conexao.ConexaoBD;
import model.Agendamento;
import model.Cliente;
import model.Profissional;
import model.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    public void inserir(Agendamento agendamento) {
        String sql = "INSERT INTO agendamentos (cliente_id, profissional_id, servico_id, data_hora, status) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, agendamento.getCliente().getId());
            stmt.setInt(2, agendamento.getProfissional().getId());
            stmt.setInt(3, agendamento.getServico().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(5, agendamento.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    agendamento.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir agendamento: " + e.getMessage());
        }
    }

    public void atualizarStatus(int id, String novoStatus) {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do agendamento: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM agendamentos WHERE id = ?";
        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir agendamento: " + e.getMessage());
        }
    }

    public List<Agendamento> listarTodos() {
        List<Agendamento> lista = new ArrayList<>();

        String sql = "SELECT a.id, a.data_hora, a.status, " +
                "c.id as cliente_id, c.nome as cliente_nome, c.telefone, c.email, " +
                "p.id as profissional_id, p.nome as profissional_nome, p.comissao_percentual, " +
                "s.id as servico_id, s.nome as servico_nome, s.preco, s.duracao_minutos " +
                "FROM agendamentos a " +
                "JOIN clientes c ON a.cliente_id = c.id " +
                "JOIN profissionais p ON a.profissional_id = p.id " +
                "JOIN servicos s ON a.servico_id = s.id " +
                "ORDER BY a.data_hora";

        Connection con = ConexaoBD.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos: " + e.getMessage());
        }
        return lista;
    }

    private Agendamento mapear(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));

        Profissional profissional = new Profissional();
        profissional.setId(rs.getInt("profissional_id"));
        profissional.setNome(rs.getString("profissional_nome"));
        profissional.setComissaoPercentual(rs.getDouble("comissao_percentual"));

        Servico servico = new Servico();
        servico.setId(rs.getInt("servico_id"));
        servico.setNome(rs.getString("servico_nome"));
        servico.setPreco(rs.getDouble("preco"));
        servico.setDuracaoMinutos(rs.getInt("duracao_minutos"));

        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getInt("id"));
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        agendamento.setStatus(rs.getString("status"));

        return agendamento;
    }
}