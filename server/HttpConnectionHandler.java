import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class HttpConnectionHandler implements ConnectionHandler {
    public static final int PORT = 8000;
    HttpServer server;

    
    public HttpConnectionHandler() throws IOException {
        // Create http server bound to port 8000
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // add context to the server
        server.createContext("/", new RootHandler());

        

    }

    @Override
    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    @Override
    public void stop() {
        server.stop(0);
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Define the path of your index.html file
            Path filepath = Paths.get("index.html");

            // Read the file content as bytes
            byte[] fileBytes = Files.readAllBytes(filepath);

            // Set a Content-Type header
            exchange.getResponseHeaders().set("Content-Type", "html");

            // Send the response headers with the correct content length
            exchange.sendResponseHeaders(200, fileBytes.length);

            // Write the file bytes to the response body
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        }
    }

    private static Map<String, String> parseQuery(String query) throws IOException {
        Map<String, String> queryParams = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return queryParams;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()) : "";
            queryParams.put(key, value);
        }
        return queryParams;
    }
}
