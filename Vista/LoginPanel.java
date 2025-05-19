package Vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para el login de usuario.
 */
public class LoginPanel extends JPanel {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    public LoginPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);

        // Campo Usuario
        txtUsuario = new JTextField(20);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        // Etiqueta Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);

        // Campo Contraseña
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        // Botón Login
        btnLogin = new JButton("Ingresar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtContrasena() {
        return txtContrasena;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public void dispose() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
