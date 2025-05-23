package vn.edu.iuh.fit.backEnd.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders; // ✅ ĐÚNG

import java.util.List;
import java.util.Map;

@Service
public class LocalEmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public float[] getEmbedding(String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of("texts", List.of(text));
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<List> response = restTemplate.postForEntity(
                    "http://localhost:5001/embed",
                    request,
                    List.class
            );

            List<Double> embedding = (List<Double>) response.getBody().get(0);
            float[] vector = new float[embedding.size()];
            for (int i = 0; i < embedding.size(); i++) {
                vector[i] = embedding.get(i).floatValue();
            }
            return vector;
        } catch (Exception e) {
            e.printStackTrace();
            return new float[0];
        }
    }
}
