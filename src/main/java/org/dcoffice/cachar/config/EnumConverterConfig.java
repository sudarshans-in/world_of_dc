package org.dcoffice.cachar.config;

import org.dcoffice.cachar.entity.ComplaintStatus;
import org.dcoffice.cachar.entity.Department;
import org.dcoffice.cachar.entity.Priority;
import org.dcoffice.cachar.entity.TaskScope;
import org.dcoffice.cachar.entity.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

/**
 * Registers lenient reading converters for all enums so that MongoDB documents
 * containing unknown enum string values fall back to a safe default instead of
 * throwing IllegalArgumentException and crashing the entire request.
 */
@Configuration
public class EnumConverterConfig {

    private static final Logger logger = LoggerFactory.getLogger(EnumConverterConfig.class);

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new StringToDepartmentConverter(),
                new StringToTaskStatusConverter(),
                new StringToTaskScopeConverter(),
                new StringToPriorityConverter(),
                new StringToComplaintStatusConverter()
        ));
    }

    @ReadingConverter
    public static class StringToDepartmentConverter implements Converter<String, Department> {
        @Override
        public Department convert(String source) {
            String normalized = source.trim().toUpperCase()
                    .replace(" ", "_").replace("-", "_").replace("/", "_");
            try {
                return Department.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown Department value '{}' (normalized: '{}'), defaulting to UNASSIGNED",
                        source, normalized);
                return Department.UNASSIGNED;
            }
        }
    }

    @ReadingConverter
    public static class StringToTaskStatusConverter implements Converter<String, TaskStatus> {
        @Override
        public TaskStatus convert(String source) {
            String normalized = source.trim().toUpperCase()
                    .replace(" ", "_").replace("-", "_");
            try {
                return TaskStatus.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown TaskStatus value '{}' (normalized: '{}'), defaulting to CREATED",
                        source, normalized);
                return TaskStatus.CREATED;
            }
        }
    }

    @ReadingConverter
    public static class StringToTaskScopeConverter implements Converter<String, TaskScope> {
        @Override
        public TaskScope convert(String source) {
            String normalized = source.trim().toUpperCase()
                    .replace(" ", "_").replace("-", "_");
            try {
                return TaskScope.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown TaskScope value '{}' (normalized: '{}'), defaulting to DEPARTMENT",
                        source, normalized);
                return TaskScope.DEPARTMENT;
            }
        }
    }

    @ReadingConverter
    public static class StringToPriorityConverter implements Converter<String, Priority> {
        @Override
        public Priority convert(String source) {
            String normalized = source.trim().toUpperCase()
                    .replace(" ", "_").replace("-", "_");
            try {
                return Priority.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown Priority value '{}' (normalized: '{}'), defaulting to MEDIUM",
                        source, normalized);
                return Priority.MEDIUM;
            }
        }
    }

    @ReadingConverter
    public static class StringToComplaintStatusConverter implements Converter<String, ComplaintStatus> {
        @Override
        public ComplaintStatus convert(String source) {
            String normalized = source.trim().toUpperCase()
                    .replace(" ", "_").replace("-", "_");
            try {
                return ComplaintStatus.valueOf(normalized);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown ComplaintStatus value '{}' (normalized: '{}'), defaulting to CREATED",
                        source, normalized);
                return ComplaintStatus.CREATED;
            }
        }
    }
}
