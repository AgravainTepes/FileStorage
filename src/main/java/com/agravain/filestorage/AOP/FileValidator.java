package com.agravain.filestorage.AOP;

import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileNameException;
import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileSizeException;
import com.agravain.filestorage.Exceptions.FileExceptions.IncorrectFileTypeException;
import com.agravain.filestorage.PropertiesLoader.PropertiesLoader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class FileValidator {

    private PropertiesLoader props;

    @Autowired
    public void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        this.props = propertiesLoader;
    }

    @Before("execution(public * uploadFile(*))")
    public void beforeUploadingAdvice(JoinPoint joinPoint) {

        List<Object> pointArgs = Arrays.asList(joinPoint.getArgs());

        MultipartFile file = (MultipartFile) pointArgs.get(0);

        List<String> fileTypes = props.getTypeList();

        if (file.getSize() > props.getMaxSize())
            throw new IncorrectFileSizeException(
                    "File too fat! Max size = " + props.getMaxSize() + " bytes. " +
                            "Yours file size = " + file.getSize());

        if (file.getOriginalFilename().contains(".."))
            throw new IncorrectFileNameException("Incorrect file name!");

        if (!fileTypes.contains(file.getContentType()))
            throw new IncorrectFileTypeException(
                    "Incorrect content type! acceptable types: " + fileTypes);

    }
}
