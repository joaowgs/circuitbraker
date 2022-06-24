package com.circuitbraker.circuitbraker.core.service;

import com.circuitbraker.circuitbraker.core.exception.BoletoException;
import com.circuitbraker.circuitbraker.integration.service.BrCobrancaIntegrationService;
import com.circuitbraker.circuitbraker.core.dto.BoletoObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoletoService {

    private final BrCobrancaIntegrationService brCobrancaIntegrationService;

    private final ServiceBucketS3 serviceBucketS3;

    public String gerarBoleto(BoletoObject boletoObject) {
        try {
            log.info("Realizando chamada API-Boletos");
            byte[] stream = brCobrancaIntegrationService.gerarArquivoBoleto(boletoObject.getBank(),
                    boletoObject.getType(),
                    boletoObject.getData()).getBody();

            if (!isNull(stream)) {
                log.info("Boleto gerado!");
                InputStream inputStream = new ByteArrayInputStream(stream);
                return serviceBucketS3.enviar(boletoObject.getBank(), boletoObject.getCorrelationId(), inputStream);
            }
            throw new BoletoException("O boleto não foi gerado será necessário reprocessa-lo!");

        } catch (Exception e) {
            log.error("Ocorreu erro durante a geração do boleto.", e);
            throw new BoletoException("Ocorreu erro durante a geração do boleto.");
        }
    }

}
