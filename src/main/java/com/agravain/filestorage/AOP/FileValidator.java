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

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class FileValidator {
    private PropertiesLoader props;

    @Autowired
    public void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        this.props = propertiesLoader;
    }

    @Before("execution(public * <String> uploadFile())")
    public void beforeUploadingAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MultipartFile file = (MultipartFile) args[1];
        String [] propsFileTypes = props.getTypes();
        List<String> fileTypes = new ArrayList<>();
        for (int i = 0; i < propsFileTypes.length; i++){
        fileTypes.add(propsFileTypes[i]);
        }
        if (file.getSize() > props.getMaxSize()){
        throw new IncorrectFileSizeException(
                "File to fat! Max size is "+ props.getMaxSize());
        }
        if (file.getOriginalFilename().contains("..")){
        throw new IncorrectFileNameException("Incorrect file name!");
        }
        if (!fileTypes.contains(file.getContentType())){
        throw new IncorrectFileTypeException(
                "Incorrect content type! acceptable types: " + fileTypes);
        }
    }


}
