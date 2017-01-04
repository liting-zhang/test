package test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


@RestController
@PropertySource("classpath:application.properties")
public class TestService {

    private static final Logger logger = LogManager.getLogger(TestService.class.getName());

    @Value("${elasticsearch.cluster.name}")
    private String elasticClusterName;

    @Value("#{'${elasticsearch.hosts}'.split(',')}")
    private List<String> elasticHosts;


    private TransportClient client;

    private ObjectMapper mapper = new ObjectMapper();



    @PostConstruct
    public void init() throws IOException {
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

        configureElasticsearch();

    }




    private void configureElasticsearch() throws IOException {

        logger.info("elasticClusterName: {}", elasticClusterName);

        Settings settings = Settings.builder()
                .put("cluster.name", elasticClusterName)
                .put("client.transport.sniff", true).build();

        client = new PreBuiltTransportClient(settings);

        try {
            for (String host : elasticHosts) {
                logger.info("add elastic search host: {}", host);
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
            }
        } catch (UnknownHostException e) {
            logger.error("fail to create connection to elasticsearch ", e);
            throw e;
        }
    }



    @RequestMapping(value = "/.test", method = RequestMethod.GET)
    String test() {
            return  "hello";

    }



}
