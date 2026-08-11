package controller;

import dao.AgendamentoDAO;
import dao.CaixaDAO;
import dao.ProdutoDAO;
import model.Agendamento;
import model.MovimentoCaixa;
import model.Produto;

public class AgendamentoController {

    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private CaixaDAO caixaDAO = new CaixaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    /**
     * Conclui um agendamento SEM uso de produto do estoque.
     */
    public void concluirAgendamento(Agendamento agendamento) {
        concluirAgendamento(agendamento, null, 0);
    }

    /**
     * Conclui um agendamento, gera o lançamento no caixa, calcula a comissão
     * e, se um produto e quantidade forem informados, debita do estoque.
     */
    public void concluirAgendamento(Agendamento agendamento, Produto produtoUsado, int quantidadeUsada) {

        // 1. Atualiza o status do agendamento para CONCLUIDO
        agendamentoDAO.atualizarStatus(agendamento.getId(), "CONCLUIDO");

        // 2. Gera o lançamento de entrada no caixa com o valor do serviço
        String descricao = "Serviço: " + agendamento.getServico().getNome()
                + " | Cliente: " + agendamento.getCliente().getNome()
                + " | Profissional: " + agendamento.getProfissional().getNome();

        MovimentoCaixa entrada = new MovimentoCaixa(
                "ENTRADA",
                descricao,
                agendamento.getServico().getPreco(),
                agendamento.getId()
        );

        caixaDAO.inserir(entrada);

        // 3. Se um produto foi utilizado no serviço, baixa do estoque
        if (produtoUsado != null && quantidadeUsada > 0) {
            produtoDAO.baixarEstoque(produtoUsado.getId(), quantidadeUsada);
        }

        // 4. Calcula a comissão do profissional (informativo)
        double comissao = agendamento.getServico().getPreco()
                * (agendamento.getProfissional().getComissaoPercentual() / 100.0);

        System.out.println("Agendamento concluído com sucesso!");
        System.out.println("Valor do serviço: R$" + agendamento.getServico().getPreco());
        System.out.println("Comissão do profissional (" + agendamento.getProfissional().getNome()
                + "): R$" + String.format("%.2f", comissao));
    }

    /**
     * Cancela um agendamento (não gera movimento financeiro).
     */
    public void cancelarAgendamento(Agendamento agendamento) {
        agendamentoDAO.atualizarStatus(agendamento.getId(), "CANCELADO");
        System.out.println("Agendamento cancelado.");
    }
}