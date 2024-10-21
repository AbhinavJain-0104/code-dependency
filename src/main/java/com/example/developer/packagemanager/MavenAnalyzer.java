package com.example.developer.packagemanager;

import com.example.developer.model.ExternalDependency;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class MavenAnalyzer implements PackageManagerAnalyzer {

    @Override
    public List<ExternalDependency> analyzePackageFile(String filePath) {
        List<ExternalDependency> dependencies = new ArrayList<>();
        try {
            File pomFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(pomFile);
            doc.getDocumentElement().normalize();

            NodeList dependencyList = doc.getElementsByTagName("dependency");
            for (int i = 0; i < dependencyList.getLength(); i++) {
                Element dependency = (Element) dependencyList.item(i);
                String groupId = dependency.getElementsByTagName("groupId").item(0).getTextContent();
                String artifactId = dependency.getElementsByTagName("artifactId").item(0).getTextContent();
                String version = dependency.getElementsByTagName("version").item(0).getTextContent();
                String name = groupId + ":" + artifactId;
                dependencies.add(new ExternalDependency(name, version, getPackageManager(), "runtime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencies;
    }

    @Override
    public String getPackageManager() {
        return "maven";
    }
}