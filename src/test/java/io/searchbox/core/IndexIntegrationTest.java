package io.searchbox.core;

import fr.tlrx.elasticsearch.test.annotations.ElasticsearchNode;
import fr.tlrx.elasticsearch.test.support.junit.runners.ElasticsearchRunner;
import io.searchbox.client.JestResult;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * @author Dogukan Sonmez
 */

@RunWith(ElasticsearchRunner.class)
@ElasticsearchNode
public class IndexIntegrationTest extends AbstractIntegrationTest {

    Map source = new HashMap<Object, Object>();

    @Test
    public void indexDocumentWithValidParametersAndWithoutSettings() throws IOException {
        try {

            source.put("user", "searchbox");
            executeTestCase(new Index.Builder(source).index("twitter").type("tweet").id("1").build());
        } catch (Exception e) {
            fail("Failed during the create index with valid parameters. Exception:" + e.getMessage());
        }
    }

    @Test
    public void automaticIdGeneration() {
        try {
            source.put("user", "jest");
            executeTestCase(new Index.Builder(source).index("twitter").type("tweet").build());
        } catch (Exception e) {
            fail("Failed during the create index with valid parameters. Exception:" + e.getMessage());
        }
    }

    private void executeTestCase(Index index) throws RuntimeException, IOException {
        JestResult result = client.execute(index);
        assertNotNull(result);
        assertTrue(result.isSucceeded());
        assertEquals(true, result.getValue("ok"));
    }
}
