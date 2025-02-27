package Modelo;
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
        try (Reader reader = new FileReader(archivoJSON)) {
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}


