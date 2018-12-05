package com.huawei.springkafka.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.ClientIdSuffixAware;
import org.springframework.kafka.core.ConsumerFactory;

import com.huaweicloud.dis.DISConfig;
import com.huaweicloud.dis.adapter.kafka.consumer.DISKafkaConsumer;

public class DISKafkaConsumerFactory<K, V> implements ConsumerFactory<K, V>, ClientIdSuffixAware<K, V> {
    private final Map<String, Object> configs;

    private Deserializer<K> keyDeserializer;

    private Deserializer<V> valueDeserializer;

    public DISKafkaConsumerFactory(Map<String, Object> configs) {
        this(configs, null, null);
    }

    public DISKafkaConsumerFactory(Map<String, Object> configs,
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer) {
        this.configs = new HashMap<>(configs);
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
    }

    public void setKeyDeserializer(Deserializer<K> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public void setValueDeserializer(Deserializer<V> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    /**
     * Return an unmodifiable reference to the configuration map for this factory.
     * Useful for cloning to make a similar factory.
     * @return the configs.
     * @since 1.0.6
     */
    public Map<String, Object> getConfigurationProperties() {
        return Collections.unmodifiableMap(this.configs);
    }

    @Override
    public Consumer<K, V> createConsumer() {
        return createKafkaConsumer();
    }

    @Override
    public Consumer<K, V> createConsumer(String clientIdSuffix) {
        return createKafkaConsumer(clientIdSuffix);
    }

    protected Consumer<K, V> createKafkaConsumer() {
        return createKafkaConsumer(this.configs);
    }

    protected Consumer<K, V> createKafkaConsumer(String clientIdSuffix) {
        if (!this.configs.containsKey(ConsumerConfig.CLIENT_ID_CONFIG) || clientIdSuffix == null) {
            return createKafkaConsumer();
        }
        else {
            Map<String, Object> modifiedClientIdConfigs = new HashMap<>(this.configs);
            modifiedClientIdConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG,
                    modifiedClientIdConfigs.get(ConsumerConfig.CLIENT_ID_CONFIG) + clientIdSuffix);
            return createKafkaConsumer(modifiedClientIdConfigs);
        }
    }

    protected Consumer<K, V> createKafkaConsumer(Map<String, Object> configs) {
        DISConfig config = new DISConfig();
        config.set("group.id", "group");
        config.setEndpoint("https://10.40.48.158:21250");
        config.setProperty("manager.endpoint", "https://10.40.48.158:21250");
        config.setAK("CWQCEIGWRDFYNYTHXVBD");
        config.setSK("hWGwgAsWdqinap7TlgYiqxsGGtP2rADP9UXWPNlV");
        config.setProjectId("2fdbbc096fb0420489021acc8b802105");
        config.setRegion("southchina");
        
        return new DISKafkaConsumer(config);
    }

    @Override
    public boolean isAutoCommit() {
        Object auto = this.configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
        return auto instanceof Boolean ? (Boolean) auto
                : auto instanceof String ? Boolean.valueOf((String) auto) : true;
    }
}
