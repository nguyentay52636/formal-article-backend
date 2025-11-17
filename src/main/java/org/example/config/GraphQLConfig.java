package org.example.config;

import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.lang.NonNull;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQlSource graphQlSource(@NonNull GraphQLSchema schema) {
        return GraphQlSource.builder(schema).build();
    }

    @Bean
    public GraphQLSchema graphQLSchema(@NonNull ApplicationContext applicationContext) {
        GraphQLSchemaGenerator generator = new GraphQLSchemaGenerator()
                .withBasePackages("org.example");

        var graphQlApis = applicationContext.getBeansOfType(Object.class)
                .values()
                .stream()
                .filter(bean -> bean.getClass().getPackageName().startsWith("org.example.graphql"))
                .toList();

        if (!graphQlApis.isEmpty()) {
            generator = generator.withOperationsFromSingletons(graphQlApis.toArray());
        }

        return generator.generate();
    }
}