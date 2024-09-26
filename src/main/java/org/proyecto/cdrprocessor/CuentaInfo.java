package org.proyecto.cdrprocessor;

public class CuentaInfo {
    int totalMinutos;
    double costoTotal;

    public CuentaInfo() {
        this.totalMinutos = 0;
        this.costoTotal = 0.0;
    }

    //public void agregarLlamada(int minutos, double tarifaMinuto) {
    public void agregarLlamada(String duracionLlamada, String tarifaMinuto) {
        int minutos = Integer.parseInt(duracionLlamada);
        double tarifaMinutoD = Double.parseDouble(tarifaMinuto);


        this.totalMinutos += minutos;
        this.costoTotal += minutos * tarifaMinutoD;
    }
}
