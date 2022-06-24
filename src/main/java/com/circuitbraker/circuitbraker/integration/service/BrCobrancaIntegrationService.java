package com.circuitbraker.circuitbraker.integration.service;

import com.circuitbraker.circuitbraker.integration.feign.CobrancaRepository;
import com.circuitbraker.circuitbraker.core.dto.DataObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrCobrancaIntegrationService {

    private final CobrancaRepository cobrancaRepository;

    @CircuitBreaker(name = "geracao-arquivo-boleto")
    public ResponseEntity<byte[]> gerarArquivoBoleto(Integer bank, String type, DataObject data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Chamada Api geração de boletos");
            String json = mapper.writeValueAsString(data);
            return cobrancaRepository.gerarArquivoBoleto(returnParamBank(bank), type, json);
        } catch (FeignException exception) {
            throw exception;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String returnParamBank(Integer bank) {
        switch (bank)  {
            case 341: return "itau";
            case 1: return "banco_brasil";
            case 33: return "santander";
            case 237: return "bradesco";
            case 104: return  "caixa";
            default: return "";

        }
    }

}
