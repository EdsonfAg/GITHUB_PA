package Vista;

import Modelo.Rol;
import java.awt.*;
import javax.swing.*;

/**
 * Panel para registrar un usuario nuevo y asignar un rol.
 */
public class RegistrarUsuarioPanel extends JPanel {
    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cbRol;
    private JButton btnRegistrar;

    public RegistrarUsuarioPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        // Contraseña
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Rol:"), gbc);
        cbRol = new JComboBox<>(Rol.values());
        gbc.gridx = 1;
        add(cbRol, gbc);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar Usuario");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnRegistrar, gbc);
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtUsername() { return txtUsername; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JComboBox<Rol> getCbRol() { return cbRol; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
}
