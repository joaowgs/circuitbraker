package com.circuitbraker.circuitbraker.consumer;

import com.circuitbraker.circuitbraker.config.custom.JsonKafkaListener;
import com.circuitbraker.circuitbraker.core.dto.BoletoObject;
import com.circuitbraker.circuitbraker.core.service.BoletoService;
import com.circuitbraker.circuitbraker.core.utils.MDCUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.core.convert.ConversionException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BoletoPdfConsumer {

    private final BoletoService boletoService;

    @RetryableTopic(
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            dltTopicSuffix = "-boleto-dlt",
            backoff = @Backoff(delay = 200, multiplier = 3.0, maxDelay = 0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            exclude = { DeserializationException.class,
                        SerializationException.class,
                        MessageConversionException.class,
                        ConversionException.class,
                        MethodArgumentResolutionException.class,
                        NoSuchMethodException.class,
                        ClassCastException.class}
    )
    @JsonKafkaListener(id = "GeracaoArquivoBoleto", topics = "GERACAO_BOLETO_PDF")
    public void consumer(BoletoObject boletoObject) {
        try {
            MDCUtil.putCorrelationId(boletoObject.getCorrelationId());
            log.info("Iniciando geração do boleto");
            String url = boletoService.gerarBoleto(boletoObject);
            log.info("Link download boleto: {}", url);
        } catch (Exception exception) {
            log.error("Erro ao gerar arquivo de boleto para o banco xpto", exception);
        } finally {
            MDCUtil.removeCorrelationId();
        }
    }

    @DltHandler
    public void dlt(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info(message + " from " + topic);
    }
}
