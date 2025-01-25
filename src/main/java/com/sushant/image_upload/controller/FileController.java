package com.sushant.image_upload.controller;

import com.sushant.image_upload.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file){
        log.info("Request to upload file");
        try{
            String fileId = fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully with ID: " + fileId);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileId") String fileId){
        log.info("Request to download file with fileId: {}", fileId);
        try{
            GridFsResource resource = fileService.downloadFile(fileId);
            log.info("FileName: {}, ContentType: {}", resource.getFilename(), resource.getContentType());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(new InputStreamResource(resource.getInputStream()));
        }catch (Exception e){
            log.error("Exception occur: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
