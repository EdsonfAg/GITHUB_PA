package Ejercicios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreferenciasUsuario extends JFrame implements ActionListener {

    // Componentes
    private JCheckBox cbMusica, cbDeportes, cbLectura;
    private JRadioButton rbManana, rbTarde, rbNoche;
    private JComboBox<String> comboDias;
    private JButton btnMostrar;
    private JLabel lblResultado;

    public PreferenciasUsuario() {
        setTitle("Preferencias de Usuario");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Casillas de verificación
        cbMusica = new JCheckBox("Música");
        cbDeportes = new JCheckBox("Deportes");
        cbLectura = new JCheckBox("Lectura");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Intereses:"), gbc);
        gbc.gridy++;
        add(cbMusica, gbc);
        gbc.gridy++;
        add(cbDeportes, gbc);
        gbc.gridy++;
        add(cbLectura, gbc);

        // Botones de opción
        rbManana = new JRadioButton("Mañana");
        rbTarde = new JRadioButton("Tarde");
        rbNoche = new JRadioButton("Noche");

        ButtonGroup grupoHorario = new ButtonGroup();
        grupoHorario.add(rbManana);
        grupoHorario.add(rbTarde);
        grupoHorario.add(rbNoche);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(new JLabel("Horario preferido:"), gbc);
        gbc.gridy++;
        add(rbManana, gbc);
        gbc.gridy++;
        add(rbTarde, gbc);
        gbc.gridy++;
        add(rbNoche, gbc);

        // ComboBox
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        comboDias = new JComboBox<>(dias);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(new JLabel("Día preferido:"), gbc);
        gbc.gridy++;
        add(comboDias, gbc);

        // Botón y etiqueta de resultado
        btnMostrar = new JButton("Mostrar Selección");
        btnMostrar.addActionListener(this);
        lblResultado = new JLabel(" ");

        gbc.gridy++;
        add(btnMostrar, gbc);
        gbc.gridy++;
        add(lblResultado, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder resultado = new StringBuilder("Has seleccionado: ");

        // Intereses
        if (cbMusica.isSelected()) resultado.append("Música, ");
        if (cbDeportes.isSelected()) resultado.append("Deportes, ");
        if (cbLectura.isSelected()) resultado.append("Lectura, ");

        // Horario preferido
        if (rbManana.isSelected()) resultado.append("Mañana, ");
        else if (rbTarde.isSelected()) resultado.append("Tarde, ");
        else if (rbNoche.isSelected()) resultado.append("Noche, ");

        // Día preferido
        resultado.append("Día: ").append(comboDias.getSelectedItem());

        lblResultado.setText(resultado.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PreferenciasUsuario().setVisible(true);
        });
    }
}
