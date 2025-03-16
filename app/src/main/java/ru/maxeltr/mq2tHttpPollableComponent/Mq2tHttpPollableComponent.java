/*
 * The MIT License
 *
 * Copyright 2025 Dev.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.maxeltr.mq2tHttpPollableComponent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import ru.maxeltr.mq2tLib.Mq2tPollableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maxim Eltratov <<Maxim.Eltratov@ya.ru>>
 */
public class Mq2tHttpPollableComponent implements Mq2tPollableComponent {

    private static final Logger logger = LoggerFactory.getLogger(Mq2tPollableComponent.class);

    static String COMPONENT_NAME = "mq2tHttpPollableComponent";

    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String getData() {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://" + ConfigLoader.loadConfig().getProperty("url", ""))) //TODO several urls
                    .GET()
                    .build();
        } catch (URISyntaxException ex) {
            logger.debug("Could not create request.", ex);
            return "";
        }

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.debug("Received non-200 response: {}", response.statusCode());
                return "";
            }
            return response.body();
        } catch (IOException ex) {
            logger.debug("Could not send request.", ex);
            return "";
        } catch (InterruptedException ex) {
            logger.debug("Request was interrupted.", ex);
            Thread.currentThread().interrupt();
        }

        return "";
    }

    @Override
    public String getName() {
        return COMPONENT_NAME;
    }

    @Override
    public void shutdown() {
        logger.info("Shutting down mq2tHttpPollableComponent.");
    }
}
