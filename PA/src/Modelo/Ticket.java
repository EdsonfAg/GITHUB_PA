package Modelo;

public class Ticket {
    public static String[][] CrearTicket() {
        return new String[20][4];
    }

    public static String UltimoTicket(int pos, String[][] mventa) {
        return pos > -1 ? mventa[pos][0] : "000";
    }

    public static int ExisteTicketCodigo(String[][] mticket, String codigo) {
        int pos = Principal.ObtenerUltimaPosicion(mticket);
        for (int i = 0; i <= pos; i++) {
            if (mticket[i][0].equals(codigo.trim())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean InsertarProductoTicket(String[][] mticket, String[] datos, int tamticket) {
        int posticket = Principal.ObtenerUltimaPosicion(mticket);
        int enc = ExisteTicketCodigo(mticket, datos[0]);
        if (posticket < tamticket) {
            if (enc > -1) {
                int cantidadactual = Integer.parseInt(mticket[enc][3]);
                mticket[enc][3] = String.valueOf(cantidadactual + 1);
            } else {
                posticket++;
                mticket[posticket][0] = datos[0];
                mticket[posticket][1] = datos[1];
                mticket[posticket][2] = datos[2];
                mticket[posticket][3] = datos[3];
            }
            return true;
        }
        return false;
    }

    public static double IvaTicket(String[][] mticket) {
        double subtotal = SubTotalTicket(mticket);
        return subtotal > 0 ? 0.16 * subtotal : -1;
    }

    public static double TotalTicket(String[][] mticket) {
        double total = SubTotalTicket(mticket);
        return total > 0 ? IvaTicket(mticket) + total : -1;
    }

    public static void RemoverProductoTicket(String[][] mticket, int pos) {
        int tam = Principal.ObtenerUltimaPosicion(mticket);
        if (tam > pos) {
            for (int i = pos; i < tam; i++) {
                mticket[i] = mticket[i + 1];
            }
            mticket[tam][0] = null;
        } else {
            mticket[pos][0] = null;
        }
    }
    

    public static void EliminarProductoTicket(String[][] mticket, int pos) {
        int cantidad = Integer.parseInt(mticket[pos][3]);
        if (cantidad > 1) {
            mticket[pos][3] = String.valueOf(cantidad - 1);
        } else {
            RemoverProductoTicket(mticket, pos);
        }
    }

    public static double SubTotalTicket(String[][] mticket) {
        double subtotal = 0;
        int pos = Principal.ObtenerUltimaPosicion(mticket);
        for (int i = 0; i <= pos; i++) {
            subtotal += Double.parseDouble(Principal.TotalProducto(mticket[i][2], mticket[i][3]));
        }
        return subtotal;
    }

    public static String IdTicketSiguiente(String idticket) {
        int num = Integer.parseInt(idticket) + 1;
        if (num < 10) {
            return "00" + num;
        } else if (num < 100) {
            return "0" + num;
        } else {
            return String.valueOf(num);
        }
    }
}