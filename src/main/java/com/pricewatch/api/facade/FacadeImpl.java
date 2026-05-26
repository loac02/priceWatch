package com.pricewatch.api.facade;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Component("facade")
public class FacadeImpl implements IFacade {

    private final ApplicationContext context;

    public FacadeImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> T executaService(Class<?> boClass, String nomeMetodo, Object... parametros) {
        try {
            Object boInstance = context.getBean(boClass);

            Class<?>[] tiposParametros = new Class<?>[parametros.length];
            for (int i = 0; i < parametros.length; i++) {
                tiposParametros[i] = parametros[i].getClass();
            }

            Method metodo = boClass.getMethod(nomeMetodo, tiposParametros);

            @SuppressWarnings("unchecked")
            T resultado = (T) metodo.invoke(boInstance, parametros);

            return resultado;

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Método " + nomeMetodo + " não encontrado no BO " + boClass.getName(), e);
        } catch (Exception e) {
            if (e.getCause() != null) {
                throw new RuntimeException(e.getCause().getMessage(), e.getCause());
            }
            throw new RuntimeException("Erro na execução do BO: " + e.getMessage(), e);
        }
    }
}