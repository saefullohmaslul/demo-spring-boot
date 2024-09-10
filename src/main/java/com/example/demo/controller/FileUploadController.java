package com.example.demo.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@Validated
public class FileUploadController {
    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB
    private static final String UPLOAD_DIR = "uploads";

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Tolong masukan file sebelum disubmit!");
            return "redirect:/upload";
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            redirectAttributes.addFlashAttribute("message", "Ukuran file terlalu besar, masukan file kurang dari 1MB!");
            return "redirect:/upload";
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String randomPrefix = UUID.randomUUID().toString();
            String originalFileName = file.getOriginalFilename();

            String newFileName = randomPrefix + "_" + originalFileName;

            Path path = Paths.get(UPLOAD_DIR, newFileName);

            Files.write(path, file.getBytes());

            redirectAttributes.addFlashAttribute("message", "File berhasil diupload: " + newFileName);
            return "redirect:/upload";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Gagal mengupload file!");
            return "redirect:/upload";
        }
    }


    @GetMapping("/upload/{filename}")
    public void getFile(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        if (Files.exists(filePath)) {
            response.setContentType(Files.probeContentType(filePath));
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }
}
