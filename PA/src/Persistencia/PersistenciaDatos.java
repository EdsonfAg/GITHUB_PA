package Persistencia;
import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDatos<T> {
    private final String archivoCSV;
    private final String archivoJSON;
    private final Gson gson = new Gson();

    public PersistenciaDatos(String archivoCSV, String archivoJSON) {
        this.archivoCSV = archivoCSV;
        this.archivoJSON = archivoJSON;
    }

    public void guardarCSV(List<T> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCSV))) {
            for (T item : lista) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> cargarCSV() {
        List<T> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Aquí necesitarías un parser específico para cada tipo de dato
                // Esto lo puedes adaptar según el formato de tus datos
                System.out.println("Cargando: " + linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardarJSON(List<T> lista) {
        try (FileWriter writer = new FileWriter(archivoJSON)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> cargarJSON(Type tipoLista) {
        try {
            File file = new File(archivoJSON);
            if (!file.exists()) { 
                // 🔥 Si el archivo no existe, lo creamos antes de intentar abrirlo
                System.out.println("El archivo " + archivoJSON + " no existe. Creando archivo vacío...");
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]"); // 🔥 Escribimos una lista vacía en el JSON
                }
            }

            // 🔥 Ahora que estamos seguros de que el archivo existe, lo leemos
            try (Reader reader = new FileReader(archivoJSON);
                 BufferedReader br = new BufferedReader(reader)) {
                String json = br.readLine(); // 🔥 Leer la primera línea del JSON
                System.out.println("Leyendo JSON: " + json); // 🔍 Verificar el contenido
                
                return gson.fromJson(json, tipoLista); // 🔥 Usamos json en lugar de reader
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
  }


