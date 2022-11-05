package com.bahar.csvfileserver.repository;

import com.bahar.csvfileserver.model.CsvFileRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CsvFileRecordRepository extends CrudRepository<CsvFileRecord, String> {
    List<CsvFileRecord> findAll(Pageable pageable);
}
