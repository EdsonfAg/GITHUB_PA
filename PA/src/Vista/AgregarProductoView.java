package Vista;


	import javax.swing.*;
	import java.awt.*;

	public class AgregarProductoView extends JFrame {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField txtCodigo, txtNombre, txtPrecio, txtStock;
	    private JButton btnAgregar;

	    public AgregarProductoView() {
	        setTitle("Agregar Producto");
	        setSize(400, 300);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	        // Panel para agregar productos
	        JPanel panelAgregar = new JPanel(new GridLayout(4, 2));
	        panelAgregar.add(new JLabel("Código:"));
	        txtCodigo = new JTextField();
	        panelAgregar.add(txtCodigo);

	        panelAgregar.add(new JLabel("Nombre:"));
	        txtNombre = new JTextField();
	        panelAgregar.add(txtNombre);

	        panelAgregar.add(new JLabel("Precio:"));
	        txtPrecio = new JTextField();
	        panelAgregar.add(txtPrecio);

	        panelAgregar.add(new JLabel("Stock:"));
	        txtStock = new JTextField();
	        panelAgregar.add(txtStock);

	        btnAgregar = new JButton("Agregar Producto");
	        panelAgregar.add(btnAgregar);

	        // Agregar el panel a la ventana
	        add(panelAgregar, BorderLayout.CENTER);

	        setVisible(true);
	    }
	}

