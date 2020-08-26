package cn.szh.search.dao;


import cn.szh.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
