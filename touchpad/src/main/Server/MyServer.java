
import java.net.*;
import java.io.*;
import java.util.*;

public class MyServer {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(30000);
		while (true) {
			Socket s = ss.accept();
			new Thread(new ServerThread(s)).start();
			System.out.println("connect to "+s.getRemoteSocketAddress().toString());
		}
	}
}
