package Ejercicios;
import java.awt.*;
import javax.swing.*;
public class Ejemplo01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*JFrame frame = new JFrame("FlowLayout Example");
		frame.setLayout(new FlowLayout());

		frame.add(new JButton("Botón 1"));
		frame.add(new JButton("Botón 2"));
		frame.add(new JButton("Botón 3"));

		frame.setSize(300, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
		JFrame frame = new JFrame("BorderLayout Example");
		frame.setLayout(new BorderLayout());

		frame.add(new JButton("Norte"), BorderLayout.NORTH);
		frame.add(new JButton("Sur"), BorderLayout.SOUTH);
		frame.add(new JButton("Este"), BorderLayout.EAST);
		frame.add(new JButton("Oeste"), BorderLayout.WEST);
		frame.add(new JButton("Centro"), BorderLayout.CENTER);

		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		
	}

}
