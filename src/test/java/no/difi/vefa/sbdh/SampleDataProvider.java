package no.difi.vefa.sbdh;

import org.testng.annotations.DataProvider;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocumentHeader;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.testng.Assert.assertNotNull;

/**
 * @author steinar
 *         Date: 10.09.15
 *         Time: 15.01
 */
public class SampleDataProvider {

    public static final String SAMPLE_ASIC = "sample-asic.asice";
    public static final String SAMPLE_SBD_XML = "sample-sbd.xml";

    @DataProvider(name = "sampleData")
    public static Object[][] creatAsicArchive() {


        InputStream asicInputStream = SampleDataProvider.class.getClassLoader().getResourceAsStream(SAMPLE_ASIC);
        assertNotNull(asicInputStream, "Unable to locate " + SAMPLE_ASIC + " in class path");

        InputStream sbdhAsStream = SampleDataProvider.class.getClassLoader().getResourceAsStream("sample-sbdh.xml");
        assertNotNull(asicInputStream, "Unable to locate sample-sbdh.xml in class path");

        SbdhParser sbdhParser = SbdhParserFactory.sbdhParserAndExtractor();

        StandardBusinessDocumentHeader sbdh = sbdhParser.parse(sbdhAsStream);

        return new Object[][]{{asicInputStream, sbdh}};
    }

    @DataProvider(name = "sampleSbd")
    public static Object[][] sampleSbd() {
        InputStream sbdStream = SampleDataProvider.class.getClassLoader().getResourceAsStream(SAMPLE_SBD_XML);
        assertNotNull(sbdStream, "Unable to locate " + SAMPLE_SBD_XML + " in class path");


        return new Object[][]{ { sbdStream } };
    }

    @DataProvider(name="sampleAsicFile")
    public static Object[][] sampleAsicFile() {
        URL url = SampleDataProvider.class.getClassLoader().getResource(SAMPLE_ASIC);
        assertNotNull(url);

        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to convert URI to File object.", e);
        }
        return new Object[][]{{file}};
    }

}

