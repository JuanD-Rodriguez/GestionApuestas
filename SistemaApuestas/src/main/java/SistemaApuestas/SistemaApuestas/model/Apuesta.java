package SistemaApuestas.SistemaApuestas.model;

public class Apuesta {
    private String evento;
    private String equipo;
    private String tipo;
    private double monto;
    private String detalle;
    private boolean ganadora;

    public boolean isGanadora() {
        return ganadora;
    }

    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }


    public Apuesta(String evento, String equipo, String tipo, double monto, String detalle) {
        this.evento = evento;
        this.equipo = equipo;
        this.tipo = tipo;
        this.monto = monto;
        this.detalle = detalle;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
