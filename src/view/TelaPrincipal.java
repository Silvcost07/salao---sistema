package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Gestão - Florescer Studio de Beleza");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Florescer Studio de Beleza", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnClientes = new JButton("Clientes");
        JButton btnAgendamentos = new JButton("Agendamentos");
        JButton btnEstoque = new JButton("Estoque");
        JButton btnCaixa = new JButton("Caixa");

        btnClientes.addActionListener(e -> new TelaClientes().setVisible(true));
        btnAgendamentos.addActionListener(e -> new TelaAgendamentos().setVisible(true));
        btnEstoque.addActionListener(e -> new TelaEstoque().setVisible(true));
        btnCaixa.addActionListener(e -> new TelaCaixa().setVisible(true));

        JPanel painelBotoes = new JPanel(new GridLayout(4, 1, 10, 10));
        painelBotoes.add(btnClientes);
        painelBotoes.add(btnAgendamentos);
        painelBotoes.add(btnEstoque);
        painelBotoes.add(btnCaixa);

        setLayout(new BorderLayout(10, 10));
        add(titulo, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }
}