package Controlador;

import Modelo.Usuario;
import Persistencia.ConexionBD;
import Persistencia.UsuarioDAO;
import Utils.AuthUtils;
import Vista.LoginPanel;
import Vista.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginController {
    private final LoginPanel  view;
    private final MainView    main;

    public LoginController(LoginPanel view, MainView main) {
    	   ConexionBD.establecerCredenciales("nuevo_user", "nueva123");
    	this.view = view;
        this.main = main;

        view.getBtnLogin().addActionListener(this::onLogin);
    }

    private void onLogin(ActionEvent e) {
        String u = view.getTxtUsuario().getText().trim();
        String p = new String(view.getTxtContrasena().getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Completa ambos campos");
            return;
        }
        Usuario usuario = UsuarioDAO.buscarPorUsername(u);
        if (usuario == null || !AuthUtils.verificar(p, usuario.getPasswordHash())) {
            JOptionPane.showMessageDialog(view, "Usuario o contraseña incorrectos");
            return;
        }
        // Éxito: delegamos en MainView
        main.loginSuccess(usuario);
    }
}
