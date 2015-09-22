package no.difi.vefa.sbdh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.testng.Assert.assertTrue;

/**
 * @author steinar
 *         Date: 22.09.15
 *         Time: 08.19
 */
public class AsicExtractorTest {

    public static final Logger log = LoggerFactory.getLogger(AsicExtractorTest.class);

    /** Illustrates how to extract base64 encoded ASiC archive */
    @Test(dataProvider = "sampleSbd", dataProviderClass = SampleDataProvider.class)
    public void extractAsicArchiveFromPayload(InputStream inputStream) throws Exception {

        // Creates the extractor
        AsicExtractor asicExtractor = AsicExtractorFactory.defaultAsicExtractor();

        // Creates a temporary file to hold the results
        File asiceFile = File.createTempFile("vefa-sample-asic", ".asice");
        FileOutputStream fileOutputStream = new FileOutputStream(asiceFile);
        BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

        // Performs the actual extraction
        asicExtractor.extractAsic(inputStream, outputStream);

        inputStream.close();
        outputStream.close();
    }


    @Test(dataProvider = "sampleSbd", dataProviderClass = SampleDataProvider.class)
    public void extractSampleAsic(InputStream sbdInputStream) throws Exception {

        AsicExtractor asicExtractor = AsicExtractorFactory.defaultAsicExtractor();

        // Creates a temporary file to hold the results
        File asiceFile = File.createTempFile("vefa-sample-asic", ".asice");
        FileOutputStream fileOutputStream = new FileOutputStream(asiceFile);
        BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

        // Performs the actual extraction
        asicExtractor.extractAsic(sbdInputStream, outputStream);

        sbdInputStream.close();
        outputStream.close();

        // Verifies the contents
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(asiceFile));

        ZipEntry zipEntry;
        boolean asicManifestSeen = false;

        log.debug("Inspecting input file " + asiceFile);

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

            log.debug("ZipEntry: " + zipEntry.getName());
            if (zipEntry.getName().startsWith("META-INF/asicmanifest")) {
                asicManifestSeen = true;
            }
        }

        assertTrue(asicManifestSeen);
    }
}
