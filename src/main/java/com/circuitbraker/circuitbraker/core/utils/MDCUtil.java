package com.circuitbraker.circuitbraker.core.utils;

import org.slf4j.MDC;

public abstract class MDCUtil {

    private static final String CORRELATION_ID = "CorrelationId";

    public static void putCorrelationId(String correlationId){
        MDC.put(CORRELATION_ID, correlationId);
    }

    public static void removeCorrelationId(){
        MDC.remove(CORRELATION_ID);
    }

    public static String getCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }

}