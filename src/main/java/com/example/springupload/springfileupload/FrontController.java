package com.example.springupload.springfileupload;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class FrontController {

    @Autowired
    FileService fileService;

    @Autowired
    FilesController filesController;

    @GetMapping("/")
    public String index(Model model,
            @RequestParam(value = "success", required = false) Boolean success) {
        List<FileResponse> list = filesController.list();
        model.addAttribute("listFiles", list);
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
        try {
            fileService.save(file);
            redirectAttrs.addAttribute("success", true);
            return "redirect:/";
        } catch (Exception e) {
            redirectAttrs.addAttribute("success", false);
            return "redirect:/";

        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String details(Model model, @PathVariable(name = "id") String id) {
        Optional<FileEntity> fOptional = fileService.getFile(id);

        if (!fOptional.isPresent()) {
            return "redirect:/";
        }

        FileEntity fileEntity = fOptional.get();

        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileEntity.getId())
                .toUriString();
        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(fileEntity.getId());
        fileResponse.setName(fileEntity.getName());
        fileResponse.setContentType(fileEntity.getContentType());
        fileResponse.setSize(fileEntity.getSize());
        fileResponse.setUrl(downloadURL);

        model.addAttribute("file", fileResponse);
        return "detail";
    }
}