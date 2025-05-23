package vn.edu.iuh.fit.backEnd.services;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backEnd.models.Job;
import vn.edu.iuh.fit.backEnd.repositories.JobRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private LocalEmbeddingService embeddingService;

//    public List<Job> recommendJobsForCandidate() {
//        try {
//            // 1. Đọc từ file keyword
//            String keyword = Files.lines(Paths.get("search_keywords.txt"))
//                    .filter(line -> !line.isBlank())
//                    .reduce((first, second) -> second)  // lấy dòng cuối cùng
//                    .orElse("");
//
//            if (keyword.isBlank()) return List.of();
//
//            // 2. Lấy embedding keyword
//            float[] keywordVector = embeddingService.getEmbedding(keyword);
//
//            // 3. Tính độ tương đồng với tất cả job_name
//            List<Job> allJobs = jobRepository.findAll();
//            List<Pair<Job, Double>> scoredJobs = new ArrayList<>();
//
//            for (Job job : allJobs) {
//                float[] jobVector = embeddingService.getEmbedding(job.getJobName());
//                double score = cosineSimilarity(keywordVector, jobVector);
//                scoredJobs.add(Pair.of(job, score));
//            }
//
//            // 4. Sắp xếp và chọn top 5
//            return scoredJobs.stream()
//                    .sorted((a, b) -> Double.compare(b.getRight(), a.getRight()))
//                    .limit(5)
//                    .map(Pair::getLeft)
//                    .toList();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return List.of();
//        }
//    }
    public List<Job> recommendJobsForCandidate() {
        try {
            String keyword = Files.lines(Paths.get("search_keywords.txt"))
                    .filter(line -> !line.isBlank())
                    .reduce((first, second) -> second)  // lấy dòng cuối
                    .orElse("");

            if (keyword.isBlank()) return List.of();

            float[] keywordVector = embeddingService.getEmbedding(keyword);
            if (keywordVector.length == 0) return List.of();

            double THRESHOLD = 0.60; // 👉 chỉ lấy nếu similarity >= 60%

            List<Job> allJobs = jobRepository.findAll();
            List<Pair<Job, Double>> scoredJobs = new ArrayList<>();

            for (Job job : allJobs) {
                float[] jobVector = embeddingService.getEmbedding(job.getJobName());
                double score = cosineSimilarity(keywordVector, jobVector);
                if (score >= THRESHOLD) {
                    scoredJobs.add(Pair.of(job, score));
                }
            }

            return scoredJobs.stream()
                    .sorted((a, b) -> Double.compare(b.getRight(), a.getRight()))
                    .limit(5)
                    .map(Pair::getLeft)
                    .toList();

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private double cosineSimilarity(float[] vec1, float[] vec2) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
