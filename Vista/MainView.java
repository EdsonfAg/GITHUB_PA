package Vista;

import Controlador.*;
import Modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Panels individuales
    private final LoginPanel loginPanel;
    private final ProductosPanel productosPanel;
    private final VentasPanel ventasPanel;
    private final EstadisticasPanel estadisticasPanel;
    private final CajaPanel cajaPanel;
    private final DevolucionesPanel devolucionesPanel;
    private final RegistrarUsuarioPanel registrarUsuarioPanel;
    private final ReportePanel reportePanel;

    // Controladores
    private VentaController ventaController;
    private CajaController cajaController;

    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel appPanel = new JPanel(new BorderLayout());
    private final JLabel lblWelcome = new JLabel("", SwingConstants.CENTER);

    public MainView() {
        super("Sistema de Gestión ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // 1) Creamos los panels
        loginPanel             = new LoginPanel();
        productosPanel         = new ProductosPanel();
        ventasPanel            = new VentasPanel();
        estadisticasPanel      = new EstadisticasPanel();
        cajaPanel              = new CajaPanel();
        devolucionesPanel      = new DevolucionesPanel();
        registrarUsuarioPanel  = new RegistrarUsuarioPanel();
        reportePanel           = new ReportePanel();

        // 2) Montamos CardLayout (LOGIN y luego APP)
        cards.add(loginPanel, "LOGIN");
        cards.add(appPanel,   "APP");
        getContentPane().add(cards);

        // 3) Montamos appPanel con menú + welcome + pestañas
        buildMenuBar();
        menuBar.setVisible(false);
        lblWelcome.setFont(lblWelcome.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        appPanel.add(lblWelcome, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Productos", productosPanel);
        tabs.addTab("Ventas",    ventasPanel);
        tabs.addTab("Estadísticas", estadisticasPanel);
        tabs.addTab("Caja",      cajaPanel);
        tabs.addTab("Devoluciones", devolucionesPanel);
        tabs.addTab("Usuarios",   registrarUsuarioPanel);
        tabs.addTab("Reportes",   reportePanel);
        appPanel.add(tabs, BorderLayout.CENTER);

        // 4) Arrancamos solo LOGIN + su controller
        new LoginController(loginPanel, this);
        cardLayout.show(cards, "LOGIN");
    }

    private void buildMenuBar() {
        JMenu menu;
        JMenuItem item;

        menu = new JMenu("Productos");
        menuBar.add(menu);

        menu = new JMenu("Ventas");
        menuBar.add(menu);

        menu = new JMenu("Estadísticas");
        menuBar.add(menu);

        menu = new JMenu("Caja");
        menuBar.add(menu);

        menu = new JMenu("Devoluciones");
        menuBar.add(menu);

        menu = new JMenu("Administración");
        menuBar.add(menu);

        menu = new JMenu("Reportes");
        menuBar.add(menu);

        //setJMenuBar(menuBar);
    }

    /**
     * Invocado desde LoginController cuando el login fue correcto.
     */
    public void loginSuccess(Usuario usuario) {
        // 1) muestro el menú y actualizo título
        setTitle("Sistema de Gestión  – " + usuario.getNombre());
        menuBar.setVisible(true);

        // 2) mensaje de bienvenida
        lblWelcome.setText("¡Bienvenido, " + usuario.getNombre() + "!");

        // 3) instancio controladores que requieren usuario
        // Caja primero para pasarla luego al VentaController
        cajaController = new CajaController(cajaPanel, usuario);
        ventaController = new VentaController(usuario, ventasPanel, cajaController,productosPanel);
        new EstadisticasController(estadisticasPanel);
        new DevolucionesController(usuario, devolucionesPanel);
        new RegistrarUsuarioController(registrarUsuarioPanel);
        new ReporteController(reportePanel);

        // 4) cambio a la vista APP
        cardLayout.show(cards, "APP");
    }

    /**
     * Para que el Main arranque el login.
     */
    public LoginPanel getLoginPanel() {
        return loginPanel;
    }
}
