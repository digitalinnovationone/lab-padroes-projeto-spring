package com.yas.voxai.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/speech")
public class SpeechController {

  @PostMapping("/transcribe")
  public Map<String,String> transcribe(@RequestParam MultipartFile file){
    return Map.of("transcription","Olá Yas, mock funcionando.");
  }
}
