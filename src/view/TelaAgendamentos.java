package view;

import dao.AgendamentoDAO;
import dao.ClienteDAO;
import dao.ProfissionalDAO;
import dao.ServicoDAO;
import dao.ProdutoDAO;
import controller.AgendamentoController;
import model.Agendamento;
import model.Cliente;
import model.Profissional;
import model.Servico;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaAgendamentos extends JFrame {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProfissionalDAO profissionalDAO = new ProfissionalDAO();
    private ServicoDAO servicoDAO = new ServicoDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private AgendamentoController controller = new AgendamentoController();

    private JComboBox<Cliente> comboClientes;
    private JComboBox<Profissional> comboProfissionais;
    private JComboBox<Servico> comboServicos;
    private JTextField campoData;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Agendamento> listaAgendamentos;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaAgendamentos() {
        setTitle("Agendamentos");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboClientes = new JComboBox<>();
        comboProfissionais = new JComboBox<>();
        comboServicos = new JComboBox<>();
        campoData = new JTextField("dd/MM/yyyy HH:mm");

        painelForm.add(new JLabel("Cliente:"));
        painelForm.add(comboClientes);
        painelForm.add(new JLabel("Profissional:"));
        painelForm.add(comboProfissionais);
        painelForm.add(new JLabel("Serviço:"));
        painelForm.add(comboServicos);
        painelForm.add(new JLabel("Data e Hora (dd/MM/yyyy HH:mm):"));
        painelForm.add(campoData);

        JButton btnAgendar = new JButton("Criar Agendamento");
        painelForm.add(btnAgendar);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Cliente", "Profissional", "Serviço", "Data/Hora", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);

        JButton btnConcluir = new JButton("Concluir Selecionado");
        JButton btnCancelar = new JButton("Cancelar Selecionado");

        JPanel painelBotoesTabela = new JPanel(new GridLayout(1, 2, 5, 5));
        painelBotoesTabela.add(btnConcluir);
        painelBotoesTabela.add(btnCancelar);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painelInferior.add(painelBotoesTabela, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(painelForm, BorderLayout.NORTH);
        add(painelInferior, BorderLayout.CENTER);

        btnAgendar.addActionListener(e -> criarAgendamento());
        btnConcluir.addActionListener(e -> concluirSelecionado());
        btnCancelar.addActionListener(e -> cancelarSelecionado());

        carregarCombos();
        carregarTabela();
    }

    private void carregarCombos() {
        comboClientes.removeAllItems();
        for (Cliente c : clienteDAO.listarTodos()) {
            comboClientes.addItem(c);
        }

        comboProfissionais.removeAllItems();
        for (Profissional p : profissionalDAO.listarTodos()) {
            comboProfissionais.addItem(p);
        }

        comboServicos.removeAllItems();
        for (Servico s : servicoDAO.listarTodos()) {
            comboServicos.addItem(s);
        }
    }

    private void criarAgendamento() {
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        Profissional profissional = (Profissional) comboProfissionais.getSelectedItem();
        Servico servico = (Servico) comboServicos.getSelectedItem();

        if (cliente == null || profissional == null || servico == null) {
            JOptionPane.showMessageDialog(this, "Cadastre clientes, profissionais e serviços antes de agendar.");
            return;
        }

        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(campoData.getText().trim(), FORMATO);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy HH:mm");
            return;
        }

        Agendamento agendamento = new Agendamento(cliente, profissional, servico, dataHora);
        agendamentoDAO.inserir(agendamento);

        JOptionPane.showMessageDialog(this, "Agendamento criado com sucesso!");
        carregarTabela();
    }

    private void concluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela.");
            return;
        }

        Agendamento agendamento = listaAgendamentos.get(linha);

        if ("CONCLUIDO".equals(agendamento.getStatus())) {
            JOptionPane.showMessageDialog(this, "Este agendamento já está concluído.");
            return;
        }

        // Pergunta se algum produto do estoque foi utilizado nesse atendimento
        List<Produto> produtos = produtoDAO.listarTodos();
        Produto[] opcoes = new Produto[produtos.size() + 1];
        opcoes[0] = null; // representa "Nenhum produto"
        for (int i = 0; i < produtos.size(); i++) {
            opcoes[i + 1] = produtos.get(i);
        }

        Produto produtoEscolhido = (Produto) JOptionPane.showInputDialog(
                this,
                "Algum produto do estoque foi utilizado neste atendimento?",
                "Uso de Produto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        int quantidadeUsada = 0;
        if (produtoEscolhido != null) {
            String resposta = JOptionPane.showInputDialog(this,
                    "Quantidade utilizada de \"" + produtoEscolhido.getNome() + "\":", "1");
            if (resposta != null) {
                try {
                    quantidadeUsada = Integer.parseInt(resposta.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantidade inválida, nenhum produto será debitado.");
                    produtoEscolhido = null;
                }
            } else {
                produtoEscolhido = null;
            }
        }

        controller.concluirAgendamento(agendamento, produtoEscolhido, quantidadeUsada);

        double comissao = agendamento.getServico().getPreco()
                * (agendamento.getProfissional().getComissaoPercentual() / 100.0);

        String mensagemEstoque = produtoEscolhido != null
                ? "\nProduto debitado: " + quantidadeUsada + "x " + produtoEscolhido.getNome()
                : "";

        JOptionPane.showMessageDialog(this,
                "Agendamento concluído!\n" +
                        "Valor do serviço: R$ " + agendamento.getServico().getPreco() + "\n" +
                        "Comissão de " + agendamento.getProfissional().getNome() + ": R$ "
                        + String.format("%.2f", comissao) +
                        mensagemEstoque + "\n" +
                        "Lançamento gerado automaticamente no Caixa.");

        carregarTabela();
    }

    private void cancelarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela.");
            return;
        }

        Agendamento agendamento = listaAgendamentos.get(linha);
        controller.cancelarAgendamento(agendamento);
        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        listaAgendamentos = agendamentoDAO.listarTodos();

        for (Agendamento a : listaAgendamentos) {
            modeloTabela.addRow(new Object[]{
                    a.getId(),
                    a.getCliente().getNome(),
                    a.getProfissional().getNome(),
                    a.getServico().getNome(),
                    a.getDataHora().format(FORMATO),
                    a.getStatus()
            });
        }
    }
}