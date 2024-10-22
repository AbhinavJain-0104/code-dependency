package com.example.developer.language;

import com.example.developer.framework.FrameworkDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalyzerFactory {

    @Autowired
    private List<LanguageSpecificAnalyzer> analyzers;

    @Autowired
    private FrameworkDetector frameworkDetector;

    public LanguageSpecificAnalyzer getAnalyzer(String filePath, String projectRoot) {
        String detectedFramework = frameworkDetector.detectFramework(projectRoot);

        for (LanguageSpecificAnalyzer analyzer : analyzers) {
            if (analyzer.canHandle(filePath)) {
                if (detectedFramework != null && analyzer.getLanguage().equals(detectedFramework)) {
                    return analyzer;
                }
                if (detectedFramework == null && analyzer instanceof JavaScriptLanguageSpecificAnalyzer) {
                    return analyzer;
                }
            }
        }

        throw new UnsupportedOperationException("No suitable analyzer found for file: " + filePath);
    }
}