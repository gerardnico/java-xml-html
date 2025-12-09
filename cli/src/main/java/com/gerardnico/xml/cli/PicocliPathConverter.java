package com.gerardnico.xml.cli;

import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PicocliPathConverter implements CommandLine.ITypeConverter<Path> {
    @Override
    public Path convert(String s) throws Exception {
        return Paths.get(s);
    }
}
