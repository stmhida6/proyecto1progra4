package org.proyecto.cdrprocessor;

class CuentaInfo {
    int totalLlamadas;
    int totalDuracion;
    double costoTotal;

    public CuentaInfo(int totalLlamadas, int totalDuracion, double costoTotal) {
        this.totalLlamadas = totalLlamadas;
        this.totalDuracion = totalDuracion;
        this.costoTotal = costoTotal;
    }

    public void agregarLlamada(int duracion, double costo) {
        this.totalLlamadas++;
        this.totalDuracion += duracion;
        this.costoTotal += costo;
    }
}
