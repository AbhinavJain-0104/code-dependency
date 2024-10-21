package com.example.developer.language;

import java.nio.file.Path;

public interface LanguageDetector {
    boolean isLanguage(Path filePath);

    String getLanguageName();
}
