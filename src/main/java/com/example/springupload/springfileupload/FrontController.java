package com.example.springupload.springfileupload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}
