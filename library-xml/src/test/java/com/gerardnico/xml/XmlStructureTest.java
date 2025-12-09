package com.gerardnico.xml;

import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * Created by gerard on 08-10-2016.
 */
public class XmlStructureTest {



    @Test
    public void testPrintNodeWikiTest() throws Exception {

        Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/wikipedia/with_attributes.xml")));
        new XmlStructure(reader).printNodeNames();
        reader.close();

    }
}
