package com.pricewatch.api.facade;

public interface IFacade {
    <T> T executaService(Class<?> boClass, String nomeMetodo, Object... parametros);
}