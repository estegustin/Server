/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author 666
 */
public class Client implements Runnable{
    String usuario,grupo;
    Thread hilo;
    Socket client;
    ServerRPCB server;
    ObjectInputStream in;
    ObjectOutputStream out;

    public Client(Socket cliente, ServerRPCB server) throws IOException {
        grupo="";
        this.client = cliente;
        this.server=server;
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        hilo=new Thread(this);
        hilo.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object dCliente=in.readObject();
                if(dCliente instanceof DataCliente){
                    actions((DataCliente)dCliente);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void actions(DataCliente dCliente) throws IOException{
        String comand=dCliente.getComando();
        switch(comand){
            case "Conex":
                System.out.println("Conectado");
                usuario=dCliente.getUsuario();
                server.clients.add(this);
                break;
            case "All":
                usuario=dCliente.getUsuario();
                grupo=dCliente.getGrupo();
                enviarMensajeGrupo(dCliente.getMensaje());
                break;
            case "User":                        
                break;
            case "Disconect":
                break;
            case "newGroup":
                grupo=dCliente.getGrupo();
                notificarNuevoGrupo(dCliente.getGrupo());
                break;
            case "joinGroup":
                usuario=dCliente.getUsuario();
                grupo="";
                unirseGrupo(dCliente.getGrupo());
                break;
             case "leaveGroup":
                usuario=dCliente.getUsuario();
                grupo=dCliente.getGrupo();
                abandonarGrupo(dCliente.getGrupo());
                break;
            default:
                break;
        }
    }
    
    public void enviarMensajeGrupo(String mensaje) throws IOException{
        for (Client c:server.clients){
            if(grupo.equals(c.grupo)){
                DataCliente dCliente = new DataCliente(usuario, "All", mensaje, grupo);
                c.out.writeObject(dCliente);
                c.out.flush();
            }
        }
    }
    
    public void abandonarGrupo(String grupo) throws IOException{
        for (Client c:server.clients){
            if(usuario.equals(c.usuario)){
                c.grupo = "";
            }else{
                if(grupo.equals(c.grupo)){
                    DataCliente dCliente = new DataCliente(usuario, "leaveGroup", "ha abandonado el grupo", grupo);
                    c.out.writeObject(dCliente);
                    c.out.flush();
                }
            }
        }
    }
    
    public void unirseGrupo(String grupo) throws IOException{
        for (Client c:server.clients){
            if(usuario.equals(c.usuario)){
                c.grupo = grupo;
            }else{
                if(grupo.equals(c.grupo)){
                    DataCliente dCliente = new DataCliente(usuario, "joinGroup", "se ha unido al grupo", grupo);
                    c.out.writeObject(dCliente);
                    c.out.flush();
                }
            }
        }
    }
    
    public void notificarNuevoGrupo(String grupo) throws IOException{
        for (Client c:server.clients){
            if(!usuario.equals(c.usuario)){
                DataCliente dCliente = new DataCliente(usuario, "newGroup", "Nuevo grupo", grupo);
                c.out.writeObject(dCliente);
                c.out.flush();
            }
        }
    }

    
    
}
