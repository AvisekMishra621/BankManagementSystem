package bank.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;

public class MiniStatement extends JFrame implements ActionListener {
    JButton b1;
    JLabel l1, l2, l3, l4;
    String pin;

    MiniStatement(String pin) {
        super("Mini Statement");
        this.pin = pin;
        setLayout(null);

        l1 = new JLabel();
        add(l1);

        l2 = new JLabel("Indian Bank");
        l2.setBounds(150, 20, 200, 20);
        l2.setFont(new Font("System", Font.BOLD, 16));
        add(l2);

        l3 = new JLabel();
        l3.setBounds(20, 60, 350, 20);
        add(l3);

        l4 = new JLabel();
        l4.setBounds(20, 400, 350, 20);
        add(l4);

        try (Conn c = new Conn()) {
            // Get card number from the login table
            PreparedStatement stmt = c.getConnection().prepareStatement("SELECT cardno FROM login WHERE pin = ?");
            stmt.setString(1, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String cardNo = rs.getString("cardno");
                l3.setText("Card Number: " + cardNo.substring(0, 4) + "XXXXXXXX" + cardNo.substring(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Conn c1 = new Conn()) {
            // Fetch transactions from the bank table
            PreparedStatement stmt = c1.getConnection().prepareStatement("SELECT date, type, amount FROM bank WHERE pin = ?");
            stmt.setString(1, pin);
            ResultSet rs = stmt.executeQuery();

            BigDecimal balance = BigDecimal.ZERO;
            StringBuilder statement = new StringBuilder("<html>");

            while (rs.next()) {
                String date = rs.getString("date");
                String type = rs.getString("type");
                BigDecimal amount = rs.getBigDecimal("amount");

                statement.append(date)
                        .append("&nbsp;&nbsp;&nbsp;&nbsp;")
                        .append(type)
                        .append("&nbsp;&nbsp;&nbsp;&nbsp;")
                        .append(amount)
                        .append("<br><br>");

                if (type.equalsIgnoreCase("Deposit")) {
                    balance = balance.add(amount);
                } else {
                    balance = balance.subtract(amount);
                }
            }

            statement.append("</html>");
            l1.setText(statement.toString());
            l4.setText("Your total balance is Rs " + balance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        b1 = new JButton("Exit");
        b1.setBounds(20, 500, 100, 25);
        b1.addActionListener(this);
        add(b1);

        l1.setBounds(20, 120, 400, 250);
        setSize(400, 600);
        setLocation(20, 20);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        this.setVisible(false);
    }

    public static void main(String[] args) {
        new MiniStatement("").setVisible(true);
    }
}
