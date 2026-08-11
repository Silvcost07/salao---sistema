package view;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaClientes extends JFrame {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private JTextField campoNome, campoTelefone, campoEmail;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private int idSelecionado = -1;

    public TelaClientes() {
        setTitle("Cadastro de Clientes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel de formulário
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoNome = new JTextField();
        campoTelefone = new JTextField();
        campoEmail = new JTextField();

        painelForm.add(new JLabel("Nome:"));
        painelForm.add(campoNome);
        painelForm.add(new JLabel("Telefone:"));
        painelForm.add(campoTelefone);
        painelForm.add(new JLabel("Email:"));
        painelForm.add(campoEmail);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar / Novo");
        painelForm.add(btnSalvar);
        painelForm.add(btnLimpar);

        // Tabela de clientes
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Telefone", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);

        JButton btnExcluir = new JButton("Excluir Selecionado");

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painelInferior.add(btnExcluir, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(painelForm, BorderLayout.NORTH);
        add(painelInferior, BorderLayout.CENTER);

        // Ações dos botões
        btnSalvar.addActionListener(e -> salvarCliente());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirClienteSelecionado());

        // Ao clicar numa linha da tabela, carrega os dados no formulário (para editar)
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
                campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
                campoTelefone.setText((String) modeloTabela.getValueAt(linha, 2));
                campoEmail.setText((String) modeloTabela.getValueAt(linha, 3));
            }
        });

        carregarTabela();
    }

    private void salvarCliente() {
        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email = campoEmail.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.");
            return;
        }

        if (idSelecionado == -1) {
            // Novo cliente
            Cliente cliente = new Cliente(nome, telefone, email);
            clienteDAO.inserir(cliente);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
        } else {
            // Atualiza cliente existente
            Cliente cliente = new Cliente();
            cliente.setId(idSelecionado);
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);
            clienteDAO.atualizar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
        }

        limparFormulario();
        carregarTabela();
    }

    private void excluirClienteSelecionado() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela primeiro.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este cliente?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            clienteDAO.excluir(idSelecionado);
            limparFormulario();
            carregarTabela();
        }
    }

    private void limparFormulario() {
        idSelecionado = -1;
        campoNome.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getEmail()});
        }
    }
}