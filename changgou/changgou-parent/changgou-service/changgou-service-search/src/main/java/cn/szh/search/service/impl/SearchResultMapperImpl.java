package cn.szh.search.service.impl;

import cn.szh.search.pojo.SkuInfo;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultMapperImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

        List<T> content = new ArrayList<>();
        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() <= 0) {
            return new AggregatedPageImpl<T>(content);
        }
        for (SearchHit searchHit : searchResponse.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("name");
            if (highlightField != null) {
                StringBuffer buffer = new StringBuffer();
                for (Text text : highlightField.getFragments()) {
                    String string = text.string();
                    buffer.append(string);
                }
                skuInfo.setName(buffer.toString());
            }
            content.add((T) skuInfo);
        }

        //2.创建分页的对象 已有

        //3.获取总个记录数
        long totalHits = searchResponse.getHits().getTotalHits();

        //4.获取所有聚合函数的结果
        Aggregations aggregations = searchResponse.getAggregations();

        //5.深度分页的ID
        String scrollId = searchResponse.getScrollId();

        return new AggregatedPageImpl<T>(content, pageable, totalHits, aggregations, scrollId);
    }
}
