package com.example.demogateway;

import com.hazelcast.config.*;
import com.hazelcast.map.listener.MapListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.demogateway.DemogatewayApplication.*;

@Configuration
public class HazelcastConfig {

    public static final String PRINCIPAL_NAME_ATTRIBUTE = "principalName";
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Bean
    public MapListener mapListener() {
        return new com.example.demogateway.HazelcastEntryListener();
    }

    @Bean
    public Config config() {

        final Config config = new Config();
        config.setInstanceName(HAZELCAST_INSTANCE_NAME);
        config.getGroupConfig().setName(GROUP_CONFIG_NAME);
        config.addMapConfig(mapConfig());

        return config;
    }

    // Since we are creating the map it's important to evict sessions
    // by setting a reasonable value for time to live
    // as the default is 0 which means infinite.
    // In this example I have set it to a low value of 30 seconds for demonstration purposes.
    private MapConfig mapConfig() {
        final MapConfig mapConfig = new MapConfig();
        mapConfig.setName(MAP_CONFIG_NAME);
        mapConfig.setTimeToLiveSeconds(30);
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfig.addEntryListenerConfig(new EntryListenerConfig(com.example.demogateway.HazelcastEntryListener.class.getName(), false, false ) );
        return mapConfig;
    }
}
