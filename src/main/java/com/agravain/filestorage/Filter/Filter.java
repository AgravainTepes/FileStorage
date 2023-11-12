package com.agravain.filestorage.Filter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {
    private String nameForFilterQuery = "";
    private LocalDateTime startDateTimeForFilterQuery;
    private LocalDateTime endDateTimeForFilterQuery;
    private List<String> fileTypesForFilterQuery;

    public String getNameForFilterQuery() {
        return nameForFilterQuery;
    }

    public void setNameForFilterQuery(String params) {
        List<String> names = new ArrayList<>();
        Pattern pattern = Pattern.compile("<\\w+>");
        Matcher matcher = pattern.matcher(params);
        for (int i = 0; matcher.find(); i++) {
            String middleResult = "";
            String finalResult = "";
            middleResult = matcher.group();
            if (!middleResult.isEmpty())
                finalResult = middleResult.substring(1, middleResult.length() - 1);
            if (!names.contains(matcher.group()))
                names.add(i, finalResult);
            if (!names.isEmpty()) {
                this.nameForFilterQuery = finalResult;
                break;
            }
        }
    }

    public LocalDateTime getStartDateTimeForFilterQuery() {
        return startDateTimeForFilterQuery;
    }

    public void setStartDateTimeForFilterQuery(String params) {
        String prefix = "start";
        this.startDateTimeForFilterQuery = parseLocalDateTime(params, prefix);
    }

    public LocalDateTime getEndDateTimeForFilterQuery() {
        return endDateTimeForFilterQuery;
    }

    public void setEndDateTimeForFilterQuery(String params) {
        String prefix = "end";
        this.endDateTimeForFilterQuery = parseLocalDateTime(params, prefix);
    }

    public List<String> getFileTypesForFilterQuery() {
        return fileTypesForFilterQuery;
    }

    public void setFileTypesForFilterQuery(String params) {
        List<String> types = new ArrayList<>();
        Pattern pattern = Pattern.compile("[<[.]\\w+>]+");
        Matcher matcher = pattern.matcher(params);
        for (int i = 0; matcher.find(); i++) {
            String middleResult = "";
            String finalResult = "";
            middleResult = matcher.group();
            if (!middleResult.isEmpty())
                finalResult = middleResult.substring(1, middleResult.length() - 1);
            if (!types.contains(finalResult)){
                types.add(finalResult);
                this.fileTypesForFilterQuery = types;
            }
        }
    }

    private LocalDateTime parseLocalDateTime(String params,
                                             String methodPrefix) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");
        List<String> parsedLDT = new ArrayList<>();
        Pattern localDateTimePattern = Pattern.compile(
                "<[1-2][0-9][0-9][0-9]-" +
                        "(0[1-9]|1[0-2])-" +
                        "(0[1-9]|[1-2][0-9]|3[0-1])\\s" +
                        "(0[0-9]|1[0-9]|2[0-4]):" +
                        "(0[0-9]|[1-5][0-9]):" +
                        "(0[0-9]|[1-5][0-9])>");
        Matcher matcher = localDateTimePattern.matcher(params);
        for (int i = 0; matcher.find(); i++) {
            String middleResult = "";
            middleResult = matcher.group();
           if (!middleResult.isEmpty())
            parsedLDT.add(middleResult.substring( 1 ,middleResult.length() - 1 ));
        }
        if (parsedLDT.size() == 2) {
            String firstDateTime = parsedLDT.get(0);
            String secondDateTime = parsedLDT.get(1);
            LocalDateTime firstLDT = LocalDateTime
                    .parse(firstDateTime, formatter);
            LocalDateTime secondLDT = LocalDateTime
                    .parse(secondDateTime, formatter);
            if (firstLDT.isBefore(secondLDT) && methodPrefix.equals("start"))
                return firstLDT;
            else if (firstLDT.isBefore(secondLDT) && methodPrefix.equals("end"))
                return secondLDT;
            return firstLDT;
        }
        return null;
    }

    public boolean isEmpty() {
        if (nameForFilterQuery.isEmpty()
                && startDateTimeForFilterQuery == null
                && endDateTimeForFilterQuery == null
                && fileTypesForFilterQuery.isEmpty())
            return true;
        return false;
    }
    public String ifTypeExist(int id){
     int size = fileTypesForFilterQuery.size();
        if (id < size)
        return fileTypesForFilterQuery.get(id);
        return null;
    }
}
