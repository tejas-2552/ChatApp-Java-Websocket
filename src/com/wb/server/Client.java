package com.wb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	BufferedReader br;
	PrintWriter pw;

	Socket socket;

	public Client() {
		try {
			System.out.println("Sending connection request to server");
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connection Eastablished!");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());

			startReading();
			startWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startWriting() {

		Runnable r2 = () -> {
			System.out.println("Writer Thread started!");
			try {
				while (!socket.isClosed()) {

					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String msg = br1.readLine();
					pw.println(msg);
					pw.flush();
					if (msg.equalsIgnoreCase("exit")) {
						System.out.println("Client Terminated the chat!");
						socket.close();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r2).start();
	}

	private void startReading() {
		Runnable r1 = () -> {
			try {
				System.out.println("Reader Thread started!");
				while (!socket.isClosed()) {

					String msg = br.readLine();
					if (msg.equalsIgnoreCase("exit")) {
						System.out.println("Server Terminated the chat!");
						socket.close();
						break;
					}

					System.out.println("Server Msg : " + msg);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection Closed!");
			}
		};
		new Thread(r1).start();
	}

	public static void main(String[] args) {
		System.out.println("This is client");
		Client server = new Client();
	}
}
