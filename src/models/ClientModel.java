package models;

import common.Writer;

import java.net.Socket;

// client model used by server
public class ClientModel {

    public String name;
    public Socket socket;

    public Writer writer;

    public boolean isNull = true;

    public ClientModel(String name, Socket socket, Writer writer) {
        this.name = name;
        this.socket = socket;
        this.writer = writer;
        this.isNull = false;
    }


    @Override
    public String toString() {
        return "ClientModel{" +
                "name='" + name + '\'' +
                ", socket=" + socket +
                ", writer=" + writer +
                '}';
    }
}
