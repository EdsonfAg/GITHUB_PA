package Ejercicios;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ejemploJframe_05_a2 extends JFrame implements ActionListener {
    static JLabel La, Lb, Lc;
    static JButton Bcalcular, Bsalir;
    static JTextField Ta, Th, Tb;

    public ejemploJframe_05_a2() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        this.setTitle("Layout FlowLayout JPanel");
        this.setBounds(10, 10, 550, 100);

        JPanel panel = new JPanel();
        La = new JLabel("Altura -->");
        Lb = new JLabel("Base -->");
        Lc = new JLabel("El area -->");
        Th = new JTextField(10); // Tamaño del campo de texto
        Tb = new JTextField(10); // Tamaño del campo de texto
        Ta = new JTextField(10); // Tamaño del campo de texto
        Ta.setEditable(false);

        Bcalcular = new JButton("Calcular");
        Bcalcular.addActionListener(this);
        Bsalir = new JButton("Salir");
        Bsalir.addActionListener(this);

        panel.add(La);
        panel.add(Th);
        panel.add(Lb);
        panel.add(Tb);
        panel.add(Bcalcular);
        panel.add(Lc);
        panel.add(Ta);
        panel.add(Bsalir);

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bcalcular) {
            double h, b, a;
            String Sh, Sb;
            Sh = Th.getText();
            Sb = Tb.getText();

            if (!Sh.isEmpty() && !Sb.isEmpty()) {
                h = Double.parseDouble(Sh);
                b = Double.parseDouble(Sb);
                a = (h * b) / 2;
                Ta.setText(String.valueOf(a));
            } else {
                JOptionPane.showMessageDialog(this, "Lo siento, uno o dos textos están vacíos");
            }
        } else if (e.getSource() == Bsalir) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        ejemploJframe_05_a2 ventana = new ejemploJframe_05_a2();
        ventana.setVisible(true);
    }
}
