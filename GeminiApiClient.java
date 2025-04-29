package com.example.ainotegenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class GeminiApiClient<CloseableHttpClient> {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public GeminiApiClient(String apiKey) {
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
    }

    public String getResponse(String userQuestion, String transcript) throws IOException {
        String requestUrl = API_URL + "?key=" + apiKey;

        Map<String, Object> part = new HashMap<>();
        part.put("text", "You are an AI note generator. Answer the following question based solely on the provided transcript. " +
                "If the question is unrelated to the transcript, respond with 'I'm sorry, I can only answer questions related to the provided content.'\n\n" +
                "Transcript:\n" + transcript + "\n\nQuestion: " + userQuestion);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        String jsonRequest = objectMapper.writeValueAsString(requestBody);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonRequest));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                Map<String, Object> jsonResponse = objectMapper.readValue(response.getEntity().getContent(), Map.class);
                if (jsonResponse.containsKey("candidates")) {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) jsonResponse.get("candidates");
                    if (!candidates.isEmpty()) {
                        Map<String, Object> firstCandidate = candidates.get(0);
                        if (firstCandidate.containsKey("content")) {
                            Map<String, Object> contentMap = (Map<String, Object>) firstCandidate.get("content");
                            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
                            if (!parts.isEmpty()) {
                                Map<String, Object> firstPart = parts.get(0);
                                return (String) firstPart.get("text");
                            }
                        }
                    }
                }
                return "No response from Gemini API.";
            }
        }
    }
}
