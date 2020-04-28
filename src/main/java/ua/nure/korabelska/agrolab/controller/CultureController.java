package ua.nure.korabelska.agrolab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.korabelska.agrolab.model.Culture;
import ua.nure.korabelska.agrolab.service.CultureService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cultures")
public class CultureController {

    CultureService cultureService;

    @Autowired
    public CultureController(CultureService cultureService) {
        this.cultureService = cultureService;
    }

    @GetMapping
    public ResponseEntity<List<Culture>> getAllVisible() {
        List<Culture> cultures = cultureService.findAllCulturesVisible();
        return ResponseEntity.ok(cultures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Culture> getByIdAndVisible(@PathVariable Long id) {
        Culture culture = cultureService.findCultureByIdAndVisible(id);
        if(culture != null) {
            return ResponseEntity.ok(culture);
        }
        return ResponseEntity.notFound().build();
    }



}
