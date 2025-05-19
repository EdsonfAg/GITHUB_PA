package App;

import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme;

import Persistencia.ConexionBD;
import Vista.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1) Look & Feel oscuro
        try {
            UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
        } catch (Exception ignored) {}
         ConexionBD.establecerCredenciales("nuevo_user", "nueva123");
        // 2) Abrir UI
        SwingUtilities.invokeLater(() -> {
            MainView mw = new MainView();
            mw.setVisible(true);
        });
    }
}
