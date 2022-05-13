/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.Serializable;
import javax.swing.DefaultListModel;

/**
 *
 * @author 666
 */
public class DataCliente implements Serializable{
    private String usuario,comando,mensaje,grupo;

    public DataCliente(String usuario, String comando, String mensaje, String grupo) {
        this.usuario = usuario;
        this.comando = comando;
        this.mensaje = mensaje;
        this.grupo = grupo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
