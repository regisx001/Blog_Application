package com.regisx001.blog.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.regisx001.blog.services.AIService;
import lombok.RequiredArgsConstructor;

record TagOutput(List<String> tags) {
}

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
}
