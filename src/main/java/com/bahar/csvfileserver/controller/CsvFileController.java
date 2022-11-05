package com.bahar.csvfileserver.controller;

import com.bahar.csvfileserver.CsvFileProps;
import com.bahar.csvfileserver.model.CsvFileRecord;
import com.bahar.csvfileserver.repository.CsvFileRecordRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/csvFile")
@Slf4j
public class CsvFileController {

    private final CsvFileRecordRepository repository;
    private final CsvFileProps csvFileProps;

    public CsvFileController(CsvFileRecordRepository repository, CsvFileProps csvFileProps) {
        this.repository = repository;
        this.csvFileProps = csvFileProps;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile csvFile) {
        if (csvFile.isEmpty()) {
            return new ResponseEntity("file is empty", HttpStatus.BAD_REQUEST);
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {

            // create csv bean reader
            CsvToBean<CsvFileRecord> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CsvFileRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // convert `CsvToBean` object to list of records
            List<CsvFileRecord> csvFileRecords = csvToBean.parse();
            repository.saveAll(new ArrayList(csvFileRecords));
        } catch (IOException e) {
            return new ResponseEntity("can not pars the filed : " + e.getMessage() , HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CsvFileRecord>> csvFileRecords(@RequestParam Optional<Integer> pageSize) {

        List<CsvFileRecord> records = repository.findAll(PageRequest.of(0, pageSize.orElseGet(csvFileProps::getPageSize)));
        return ResponseEntity.ok(records);

    }

    @GetMapping("/{code}")
    public ResponseEntity<CsvFileRecord> csvFileRecord(@PathVariable("code") String code) {
        log.info("code to be fetched is: " + code);
        Optional<CsvFileRecord> optionalRecord = repository.findById(code);

        if (optionalRecord.isEmpty())
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(optionalRecord.get());

    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllRecords() {
        repository.deleteAll();
    }

}
