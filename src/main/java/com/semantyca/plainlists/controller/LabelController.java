package com.semantyca.plainlists.controller;

import com.semantyca.plainlists.ModuleConst;
import com.semantyca.plainlists.api.Label;
import com.semantyca.plainlists.dao.ILabelDAO;
import com.toonext.EnvConst;
import com.toonext.dto.ViewPage;
import com.toonext.util.NumberUtil;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LabelController {
    private ILabelDAO dao;
    private RestHighLevelClient elasticClient;

    public LabelController(ILabelDAO dao,RestHighLevelClient elasticClient) {
        this.dao = dao;
        this.elasticClient = elasticClient;
    }

    public ViewPage getAll(String pageNumAsText, String pageSizeAsText, long reader){
        long count = dao.getCountOfAll();
        int pageSize = NumberUtil.stringToInt(pageSizeAsText, EnvConst.DEFAULT_PAGE_SIZE);
        int pageNum = NumberUtil.stringToInt(pageNumAsText, 1);
        List<Label> result = dao.findAll(pageSize, NumberUtil.calcStartEntry(pageNum, pageSize));
        return new ViewPage(result, count, NumberUtil.countMaxPage(count, pageSize), pageNum, pageSize);

    }

    public ViewPage search(String pageNumAsText, String pageSizeAsText, String keyword) {
        int pageSize = NumberUtil.stringToInt(pageSizeAsText, EnvConst.DEFAULT_PAGE_SIZE);
        int pageNum = NumberUtil.stringToInt(pageNumAsText, 1);

        SearchRequest searchRequest = new SearchRequest(ModuleConst.ELASTIC_INDEX_NAME);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.queryStringQuery(keyword));
        sourceBuilder.from(NumberUtil.calcStartEntry(pageNum, pageSize));
        sourceBuilder.size(pageSize);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        ViewPage viewPage = new ViewPage();
        try {
            SearchResponse searchResponse = elasticClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            TotalHits totalHits = hits.getTotalHits();
            long numHits = totalHits.value;
            List list = new ArrayList();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                list.add(hit);
            }
            return new ViewPage(list, numHits, 10, 1, 10, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return viewPage;
    }
}
