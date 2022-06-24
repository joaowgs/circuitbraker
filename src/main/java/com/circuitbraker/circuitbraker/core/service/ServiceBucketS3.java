package com.circuitbraker.circuitbraker.core.service;

import com.circuitbraker.circuitbraker.core.components.AmazonS3Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceBucketS3 {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3Component amazonS3Component;

    public String enviar(Integer bank, String correlationId, InputStream arquivoPdf) throws IOException {
        String nomeArquivo = gerarNomeArquivo(bank, correlationId);

        log.info("Enviando PDF do boleto para bucket - Bucket: {}, Path: {}", bucketName, nomeArquivo);
        amazonS3Component.enviarArquivoParaBucket(bucketName, nomeArquivo, arquivoPdf);
        log.info("Finalizado envio do PDF do Boleto - Bucket: {}, Path: {}", bucketName, nomeArquivo);
        return amazonS3Component.retornaUrlArquivo(bucketName,nomeArquivo).toString();

    }

    private String gerarNomeArquivo(Integer bank, String correlationId){
        return String.format("%s/%s", bank.toString().concat("-").concat(correlationId), "PDF");
    }
}
