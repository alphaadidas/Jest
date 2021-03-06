package io.searchbox.internal.core;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.http.HttpClient;
import io.searchbox.client.http.JestHttpClient;
import junit.framework.Assert;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

//@RunWith(ElasticsearchRunner.class)
//@ElasticsearchNode
public class PercolateTest {

    //@Test
    public void percolate() throws IOException {

        JestHttpClient httpClient = (JestHttpClient) new JestClientFactory().getObject();

        Client client = new HttpClient(httpClient);

        //This is the query we're registering in the percolator
        QueryBuilder qb = termQuery("content", "amazing");

        //Index the query = register it in the percolator
        IndexResponse response = client.prepareIndex("_percolator", "myIndexName", "myDesignatedQueryName")
                .setSource(qb.toString())
                .setRefresh(true) //Needed when the query shall be available immediately
                .execute().actionGet();

        Assert.assertNotNull(response);


        //Build a document to check against the percolator
        XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
        docBuilder.field("doc").startObject(); //This is needed to designate the document
        docBuilder.field("content", "This is amazing!");
        docBuilder.endObject(); //End of the doc field
        docBuilder.endObject(); //End of the JSON root object
        //Percolate
        PercolateResponse percolateResponse =
                client.preparePercolate("myIndexName", "myDocumentType").setSource(docBuilder).execute().actionGet();
        //Iterate over the results
        for (String result : percolateResponse) {
            //Handle the result which is the name of
            //the query in the percolator
        }

        Assert.assertNotNull(percolateResponse);
    }

}
