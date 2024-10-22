package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NextJSAnalyzer extends ReactAnalyzer {

    private static final Pattern API_ROUTE_PATTERN = Pattern.compile("export\\s+default\\s+function\\s+handler\\s*\\(");
    private static final Pattern GET_SERVER_SIDE_PROPS_PATTERN = Pattern.compile("export\\s+async\\s+function\\s+getServerSideProps\\s*\\(");
    private static final Pattern GET_STATIC_PROPS_PATTERN = Pattern.compile("export\\s+async\\s+function\\s+getStaticProps\\s*\\(");

    @Override
    public String getLanguage() {
        return "Next.js";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity componentEntity = super.extractClassInfo(content, filePath);
        componentEntity.setFramework("Next.js");

        componentEntity.setIsApiRoute(isApiRoute(content));
        componentEntity.setHasGetServerSideProps(hasGetServerSideProps(content));
        componentEntity.setHasGetStaticProps(hasGetStaticProps(content));

        return componentEntity;
    }

    private boolean isApiRoute(String content) {
        Matcher matcher = API_ROUTE_PATTERN.matcher(content);
        return matcher.find();
    }

    private boolean hasGetServerSideProps(String content) {
        Matcher matcher = GET_SERVER_SIDE_PROPS_PATTERN.matcher(content);
        return matcher.find();
    }

    private boolean hasGetStaticProps(String content) {
        Matcher matcher = GET_STATIC_PROPS_PATTERN.matcher(content);
        return matcher.find();
    }
}