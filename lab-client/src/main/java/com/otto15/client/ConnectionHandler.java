package com.otto15.client;


import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.io.DataReader;
import com.otto15.common.state.PerformanceState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public final class ConnectionHandler {

    private final PerformanceState performanceState;
    private Socket socket;
    private boolean isOpen = false;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String lastAddress;
    private int lastPort = 0;


    public ConnectionHandler(PerformanceState performanceState) {
        this.performanceState = performanceState;
    }

    public int getLastPort() {
        return lastPort;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void openConnection() {
        try {
            while (true) {
                if (openConnection(inputAddress(), inputPort()) > 0) {
                    return;
                }
            }
        } catch (IOException | EndOfStreamException e) {
            performanceState.switchPerformanceStatus();
        }
    }

    public int openConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            isOpen = true;
            lastAddress = String.valueOf(socket.getRemoteSocketAddress());
            lastPort = socket.getPort();
            System.out.println("Connected.");
            return 1;
        } catch (IOException | IllegalArgumentException e) {
            isOpen = false;
            System.out.println("Invalid host or port");
            return -1;
        }
    }

    private int inputPort() throws EndOfStreamException, IOException {
        try {
            DataReader reader = new DataReader(new InputStreamReader(System.in));
            System.out.println("Enter port:");
            return Integer.parseInt(reader.inputLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String inputAddress() throws EndOfStreamException, IOException {
        DataReader reader = new DataReader(new InputStreamReader(System.in));
        System.out.println("Enter address:");
        return reader.inputLine();

    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            isOpen = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}


