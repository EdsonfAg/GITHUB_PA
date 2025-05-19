
// RegistrarUsuarioController.java
package Controlador;

import Modelo.Usuario;
import Modelo.Rol;
import Persistencia.UsuarioDAO;
import Utils.AuthUtils;
import Vista.RegistrarUsuarioPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Collections;

public class RegistrarUsuarioController {
    private final RegistrarUsuarioPanel view;

    public RegistrarUsuarioController(RegistrarUsuarioPanel view) {
        this.view = view;
        view.getBtnRegistrar().addActionListener(this::onRegistrar);
    }

    private void onRegistrar(ActionEvent e) {
        String nombre = view.getTxtNombre().getText().trim();
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword()).trim();
        Rol rol = (Rol) view.getCbRol().getSelectedItem();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Completa todos los campos.");
            return;
        }
        try {
            if (UsuarioDAO.buscarPorUsername(username)!=null) {
                JOptionPane.showMessageDialog(view, "Usuario ya existe.");
                return;
            }
            Usuario nuevo = new Usuario(nombre, username, AuthUtils.hashear(password), Collections.singletonList(rol));
            if (UsuarioDAO.insertar(nuevo)) JOptionPane.showMessageDialog(view, "Usuario creado!");
            else JOptionPane.showMessageDialog(view, "Error al crear usuario.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "DB error: "+ex.getMessage());
        }
    }
}