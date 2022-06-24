package com.circuitbraker.circuitbraker.integration.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gerador-boletos", url="${gerador-boletos.api.baseurl}")
public interface CobrancaRepository {

    @GetMapping(value = "/api/boleto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces =
            MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> gerarArquivoBoleto(@RequestParam String bank,
                                           @RequestParam String type,
                                           @RequestParam String data);
}
