package xss;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class HelloHandler implements HttpHandler {

	private static final String HTML =
		"<!DOCTYPE html>" +
		"<html>" +
		"<head>" +
		"<title>Hello, %s!</title>" +
		"</head>" +
		"<body>" +
		"<h1>Hello, %s!</h1>" +
		"</body>" +
		"</html>";

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().equals("POST")) {
			try (OutputStream out = exchange.getResponseBody();
    			     InputStream in = exchange.getRequestBody()) {
				String name = getName(in);
				String resp =
					String.format(Locale.US,HTML,name,name);
				exchange.sendResponseHeaders(200,resp.length());
				out.write(resp.getBytes());
				out.flush();
			}
		}
	}

	private String getName(InputStream in) {
		String body = new BufferedReader(new InputStreamReader(in))
					.lines()
					.collect(joining());

		return Optional.ofNullable(body)
				.map(this::utf8Decode)
				.map(s -> s.replace("name=",""))
				.orElseGet(() -> "World!");
	}

	private String utf8Decode(String body) {
		try {
			return URLDecoder.decode(body, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}
