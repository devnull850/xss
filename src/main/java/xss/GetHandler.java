package xss;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

import static java.util.stream.Collectors.joining;

public class GetHandler implements HttpHandler {

	private final String fileName;

	public GetHandler(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().equals("GET")) {
			try (OutputStream out = exchange.getResponseBody()) {
				String fileContents = getFileContents();
				exchange.sendResponseHeaders(200,fileContents.length());
				out.write(fileContents.getBytes());
				out.flush();
			}
		}
	}

	private String getFileContents() throws IOException {
		try (BufferedReader in =
			new BufferedReader(new FileReader(getFileName()))) {
			return in.lines().collect(joining());
		}
	}

	public String getFileName() {
		return fileName;
	}

}
