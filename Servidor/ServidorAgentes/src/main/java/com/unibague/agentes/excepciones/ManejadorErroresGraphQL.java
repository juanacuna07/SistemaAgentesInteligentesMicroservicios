package com.unibague.agentes.excepciones;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * Convierte las excepciones de validacion del servidor en errores GraphQL con un mensaje
 * legible para los clientes. Sin esta clase Spring solo devolveria "INTERNAL_ERROR".
 */
@Component
public class ManejadorErroresGraphQL extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage() != null ? ex.getMessage() : "Error inesperado en el servidor")
                .build();
    }
}
