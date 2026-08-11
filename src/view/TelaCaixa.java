package view;

import dao.CaixaDAO;
import model.MovimentoCaixa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCaixa extends JFrame {

    private CaixaDAO caixaDAO = new CaixaDAO();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel labelSaldo;

    public TelaCaixa() {
        setTitle("Controle de Caixa");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        labelSaldo = new JLabel("Saldo Total: R$ 0,00", SwingConstants.CENTER);
        labelSaldo.setFont(new Font("Arial", Font.BOLD, 18));
        labelSaldo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Tipo", "Descrição", "Valor", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);

        JButton btnAtualizar = new JButton("Atualizar Extrato");
        btnAtualizar.addActionListener(e -> carregarTabela());

        setLayout(new BorderLayout());
        add(labelSaldo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<MovimentoCaixa> movimentos = caixaDAO.listarTodos();
        for (MovimentoCaixa m : movimentos) {
            modeloTabela.addRow(new Object[]{
                    m.getId(), m.getTipo(), m.getDescricao(), m.getValor(), m.getDataMovimento()
            });
        }

        double saldo = caixaDAO.calcularSaldoTotal();
        labelSaldo.setText(String.format("Saldo Total: R$ %.2f", saldo));
    }
}