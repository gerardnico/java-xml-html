package com.gerardnico.xml.cli;


import com.combostrap.exception.NoManifestException;
import com.combostrap.fs.Fs;
import com.combostrap.java.JarManifest;
import com.gerardnico.xml.Xmls;
import picocli.CommandLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class XmlVersionProvider implements CommandLine.IVersionProvider {


    /**
     * The property that starts with the following prefixes are shown
     * when version is asked
     */
    static private final List<String> attributeVersionPrefixes = List.of("git", "java", "os", "package");


    @Override
    public String[] getVersion() {
        List<String> versionString = getVersionsInfo();
        if (versionString == null) {
            // array of size zero
            return new String[0];
        }
        return versionString.toArray(new String[0]);
    }

    /**
     * @return all version infos
     */
    private static List<String> getVersionsInfo() {
        try {
            return JarManifest.createFor(XmlVersionProvider.class)
                    .getMap()
                    .entrySet()
                    .stream()
                    .filter(entry -> attributeVersionPrefixes.stream().anyMatch(entry.getKey().toLowerCase()::startsWith))
                    .map(entry -> entry.getKey() + " : " + entry.getValue())
                    .sorted()
                    .collect(Collectors.toList());
        } catch (NoManifestException e) {
            // Not in a jar
            Path pomXml;
            try {
                pomXml = Fs.closest(Paths.get(""), ".git").getParent().resolve("pom.xml");
            } catch (FileNotFoundException ex) {
                return null;
            }
            InputStream inputStream;
            try {
                inputStream = Files.newInputStream(pomXml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String version = Xmls.get(inputStream, "/project/version/text()");
            return List.of(version);
        }
    }
}
