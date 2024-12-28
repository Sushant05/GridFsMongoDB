package com.sushant.image_upload.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Uploading file");
        InputStream inputStream = file.getInputStream();
        return gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType()).toString();
    }

    public GridFsResource downloadFile(String fileId) throws IOException {
        log.info("Downloading file with fileId: {}", fileId);
        ObjectId objectId = new ObjectId(fileId);

        Query query = new Query(Criteria.where("_id").is(objectId));
        GridFSFile gridFsFile = gridFsTemplate.findOne(query);

        return gridFsTemplate.getResource(gridFsFile);
    }
}
