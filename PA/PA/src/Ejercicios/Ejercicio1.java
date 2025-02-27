package Ejercicios;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import java.awt.Color;

public class Ejercicio1 extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Ejercicio1() {
		setBorder(new TitledBorder(null, "calculadora", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JComboBox comboBox = new JComboBox();
		add(comboBox);

	}

}
