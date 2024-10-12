package com.coopang.apiconfig.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapper strictMapper() {
        ModelMapper strictMapper = new ModelMapper();
        strictMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return strictMapper;
    }

    @Bean
    public ModelMapper standardMapper() {
        ModelMapper standardMapper = new ModelMapper();
        standardMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return standardMapper;
    }

    @Bean
    public ModelMapper looseMapper() {
        ModelMapper looseMapper = new ModelMapper();
        looseMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return looseMapper;
    }
}