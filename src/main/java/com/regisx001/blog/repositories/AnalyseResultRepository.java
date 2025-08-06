package com.regisx001.blog.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.regisx001.blog.domain.entities.AnalyseResult;
import com.regisx001.blog.domain.entities.Enums.AnalyseDecision;

@Repository
public interface AnalyseResultRepository extends JpaRepository<AnalyseResult, UUID> {

    // Use Spring Data JPA method naming convention with First to get only one
    // result
    Optional<AnalyseResult> findFirstByArticleIdOrderByAnalyzedAtDesc(UUID articleId);

    // Keep the original method for backward compatibility, but rename it to
    // indicate it may return multiple
    @Query("SELECT ar FROM AnalyseResult ar WHERE ar.article.id = ?1")
    List<AnalyseResult> findAllByArticleId(UUID articleId);

    List<AnalyseResult> findByDecision(AnalyseDecision decision);

    @Query("SELECT AVG(ar.confidenceScore) FROM AnalyseResult ar WHERE ar.analyzedAt >= ?1")
    Double getAverageConfidenceScore(LocalDateTime since);

    @Query("SELECT ar FROM AnalyseResult ar WHERE ar.confidenceScore < ?1 AND ar.decision = 'APPROVED'")
    List<AnalyseResult> findLowConfidenceApprovals(Double threshold);
}