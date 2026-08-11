package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaEstoque extends JFrame {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private JTextField campoNome, campoQuantidade, campoPrecoCusto, campoPrecoVenda;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private int idSelecionado = -1;

    public TelaEstoque() {
        setTitle("Controle de Estoque");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoNome = new JTextField();
        campoQuantidade = new JTextField();
        campoPrecoCusto = new JTextField();
        campoPrecoVenda = new JTextField();

        painelForm.add(new JLabel("Nome do Produto:"));
        painelForm.add(campoNome);
        painelForm.add(new JLabel("Quantidade em Estoque:"));
        painelForm.add(campoQuantidade);
        painelForm.add(new JLabel("Preço de Custo:"));
        painelForm.add(campoPrecoCusto);
        painelForm.add(new JLabel("Preço de Venda:"));
        painelForm.add(campoPrecoVenda);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar / Novo");
        painelForm.add(btnSalvar);
        painelForm.add(btnLimpar);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Qtd. Estoque", "Preço Custo", "Preço Venda"}, 0) {
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

        btnSalvar.addActionListener(e -> salvarProduto());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirProdutoSelecionado());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
                campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
                campoQuantidade.setText(String.valueOf(modeloTabela.getValueAt(linha, 2)));
                campoPrecoCusto.setText(String.valueOf(modeloTabela.getValueAt(linha, 3)));
                campoPrecoVenda.setText(String.valueOf(modeloTabela.getValueAt(linha, 4)));
            }
        });

        carregarTabela();
    }

    private void salvarProduto() {
        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do produto é obrigatório.");
            return;
        }

        int quantidade;
        double precoCusto, precoVenda;

        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            precoCusto = Double.parseDouble(campoPrecoCusto.getText().trim().replace(",", "."));
            precoVenda = Double.parseDouble(campoPrecoVenda.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preços devem ser números válidos.");
            return;
        }

        if (idSelecionado == -1) {
            Produto produto = new Produto(nome, quantidade, precoCusto, precoVenda);
            produtoDAO.inserir(produto);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
        } else {
            Produto produto = new Produto();
            produto.setId(idSelecionado);
            produto.setNome(nome);
            produto.setQuantidadeEstoque(quantidade);
            produto.setPrecoCusto(precoCusto);
            produto.setPrecoVenda(precoVenda);
            produtoDAO.atualizar(produto);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
        }

        limparFormulario();
        carregarTabela();
    }

    private void excluirProdutoSelecionado() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este produto?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            produtoDAO.excluir(idSelecionado);
            limparFormulario();
            carregarTabela();
        }
    }

    private void limparFormulario() {
        idSelecionado = -1;
        campoNome.setText("");
        campoQuantidade.setText("");
        campoPrecoCusto.setText("");
        campoPrecoVenda.setText("");
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getQuantidadeEstoque(), p.getPrecoCusto(), p.getPrecoVenda()
            });
        }
    }
}