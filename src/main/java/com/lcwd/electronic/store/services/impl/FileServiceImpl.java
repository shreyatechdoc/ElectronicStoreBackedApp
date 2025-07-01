package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String orignalFilename = file.getOriginalFilename();
        String filename= UUID.randomUUID().toString();
        String extension=orignalFilename.substring(orignalFilename.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
        String fullPathWithFileName=path+ File.separator+fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg"))
        {
            // file save
            File folder=new File(path);
            if(!folder.exists())
            {
                folder.mkdirs();
            }

            // upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        else {
        throw new BadApiRequestException("File with this "+ extension +"not allowed !!");
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullpath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullpath);
        return inputStream;
    }
}
