package com.wb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	ServerSocket st;
	Socket socket;

	BufferedReader br;
	PrintWriter pw;

	public Server() {
		try {
			st = new ServerSocket(7777);
			System.out.println("Server is ready to accecpt connection.");
			System.out.println("Waiting for client to connect.");
			socket = st.accept();

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());

			startReading();
			startWriting();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void startWriting() {

		Runnable r2 = () -> {
			try {
				System.out.println("Writer Thread started!");
				while (!socket.isClosed()) {

					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String msg = br1.readLine();
					pw.println(msg);
					pw.flush();
					if (msg.equalsIgnoreCase("exit")) {
						System.out.println("Server Terminated the chat!");
						socket.close();
						break;
					}
				}
				System.out.println("Connection Closed!");
			} catch (IOException e) {
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r2).start();
	}

	private void startReading() {
		Runnable r1 = () -> {
			System.out.println("Reader Thread started!");
			try {
				while (!socket.isClosed()) {

					String msg = br.readLine();
					if (msg.equalsIgnoreCase("exit")) {
						System.out.println("Client Terminated the chat!");
						socket.close();
						break;
					}

					System.out.println("Client Msg : " + msg);

				}
				System.out.println("Connection Closed!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r1).start();
	}

	public static void main(String[] args) {
		Server server = new Server();
	}
}
