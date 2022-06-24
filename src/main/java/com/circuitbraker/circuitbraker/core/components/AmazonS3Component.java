package com.circuitbraker.circuitbraker.core.components;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;

@Component
public class AmazonS3Component {

    @Qualifier("s3Configuration")
    @Autowired
    private AmazonS3 amazonS3;

    public PutObjectResult enviarArquivoParaBucket(String bucketName, String caminhoDoArquivo,
                                                   InputStream arquivo) {
        PutObjectRequest arquivoAEnviar = new PutObjectRequest(bucketName, caminhoDoArquivo,arquivo,new ObjectMetadata());
        arquivoAEnviar.setCannedAcl(CannedAccessControlList.PublicRead);
        return amazonS3.putObject(arquivoAEnviar);
    }

    public URL retornaUrlArquivo(String bucketName, String nomeArquivo) {
        return amazonS3.getUrl(bucketName, nomeArquivo);
    }

}