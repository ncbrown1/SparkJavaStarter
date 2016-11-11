package me.nickbrown.sparkjavastarter.http.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;


/**
 * Created by ncbrown on 11/10/16.
 */
public class LoggingMiddleware {
    private static final Logger logger = LoggerFactory.getLogger(LoggingMiddleware.class);
    
    public static void log(Request request, Response response) {
        String method = request.requestMethod();
        String route = request.uri();
        logger.info(LocalDateTime.now() + "\t" + method + "\t" + route);
    }
}
