package com.example.developer.dependency;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DependencyResolverFactory {

    private final Map<String, DependencyResolver> resolvers;

    public DependencyResolverFactory(Map<String, DependencyResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public DependencyResolver getResolver(String language) {
        return resolvers.get(language);
    }
}