package com.bahar.csvfileserver.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvFileRecord {
    @Id
    @CsvBindByName
    private String code;

    @CsvBindByName
    private String source;

    @CsvBindByName
    private String codeListCode;

    @CsvBindByName
    private String displayValue;

    @CsvBindByName
    private String longDescription;

    @CsvBindByName
    private String fromDate;

    @CsvBindByName
    private String toDate;

    @CsvBindByName
    private int sortingPriority;

}
