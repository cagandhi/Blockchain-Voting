package com.main;

import java.util.Scanner;

import com.network.ClientManager;
import com.network.ServerManager;

/**
 * PMMain
 * 
 * Main class of a tiny framework to simulate voting using BlockChain via P2P network.
 * 
 * A bi-directional migration between server and client is supported using JAVA Serialization/Reflection and Socket.
 * Detailed system design, user case and limitations are elaborated in report.
 */
public class Main {

	private static final String DEFAULT_SERVER_ADDR = "localhost";
	private static final int DEFAULT_PORT = 6777;
	
	/*
	 * Everything starts from here!
	 */
	public static void main(String[] args) {
		
		System.out.println("Please choose a role you want to be: server or client.");
		System.out.println("server PORT - The port to listen to. 6777 is set default if not specified.");
		System.out.println("client SERVER_ADDRESS PORT - The server address and port to connect to. localhost:6777 is set default if not specified.");
		System.out.println("Make sure run the server first and then run client to connect to it.");
		System.out.println("> ");

		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		String[] cmd = line.split("\\s+");
		
		if (cmd[0].contains("s")) {
			/* work as server */
			int port = DEFAULT_PORT;
			if (cmd.length > 1) {
				try {
					port = Integer.parseInt(cmd[1]);
				} catch(NumberFormatException e) {
					System.out.println("Error: port is not a number!");
					in.close();
					return;
				}
			}
			
			ServerManager _svrMgr =new ServerManager(port);
			new Thread(_svrMgr).start();


		} else if (cmd[0].contains("c")) {//client selected
			/* work as client */
			String svrAddr = DEFAULT_SERVER_ADDR;
			int port = DEFAULT_PORT;
			if (cmd.length > 2) {
				try {
					svrAddr = cmd[1];
					port = Integer.parseInt(cmd[2]);
				} catch(NumberFormatException e) {
					System.out.println("Error: port is not a number!");
					in.close();
					return;
				}
			}
			
			ClientManager _cltMgr = new ClientManager(svrAddr, port);
			
			/* new thread to receive msg */
			new Thread(_cltMgr).start();
			
			_cltMgr.startClient();
		} else {
			showHelp();
			in.close();
			return;
		}
		in.close();
	}
	
	public static void showHelp() {
		System.out.println("Restart and selct role as server or client.");
		System.exit(0);
	}
}
