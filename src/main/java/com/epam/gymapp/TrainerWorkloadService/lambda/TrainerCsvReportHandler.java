
package com.epam.gymapp.TrainerWorkloadService.lambda;

import com.epam.gymapp.TrainerWorkloadService.model.MonthSummary;
import com.epam.gymapp.TrainerWorkloadService.model.TrainerWorkload;
import com.epam.gymapp.TrainerWorkloadService.model.YearSummary;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Year;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TrainerCsvReportHandler implements RequestHandler<Object, String> {

    private final String TABLE_NAME = System.getenv().getOrDefault("TABLE_NAME", "TrainerWorkload");
    private final String BUCKET_NAME = System.getenv().getOrDefault("BUCKET_NAME", "yodgorawebapp");
    private final String REPORT_PREFIX = System.getenv().getOrDefault("REPORT_PREFIX", "reports/");
    private final Region AWS_REGION = Region.of(System.getenv().getOrDefault("AWS_REGION", "us-east-1"));

    private final DynamoDbEnhancedClient enhancedClient;
    private final S3Client s3;

    public TrainerCsvReportHandler() {
        // Use environment credentials provider (Lambda provides credentials automatically)
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(AWS_REGION)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

        this.s3 = S3Client.builder()
                .region(AWS_REGION)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Override
    public String handleRequest(Object input, Context context) {
        LambdaLogger logger = context.getLogger();
        try {
            logger.log("Starting Trainers CSV Report Lambda\n");

            // Determine current month/year in Asia/Tashkent
            ZoneId tz = ZoneId.of("Asia/Tashkent");
            LocalDate now = LocalDate.now(tz);
            Year currentYear = Year.of(now.getYear());
            Month currentMonth = now.getMonth();

            String yyyy = String.format("%04d", now.getYear());
            String mm = String.format("%02d", now.getMonthValue());
            String fileName = String.format("Trainers_Trainings_summary_%s_%s.csv", yyyy, mm);
            String s3Key = REPORT_PREFIX + fileName;

            DynamoDbTable<TrainerWorkload> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(TrainerWorkload.class));

            // Scan all trainer workload items
            List<TrainerWorkload> trainers = table.scan(ScanEnhancedRequest.builder().build())
                    .items()
                    .stream()
                    .collect(Collectors.toList());

            StringBuilder csv = new StringBuilder();
            // Header
            csv.append("Trainer First Name,Trainer Last Name,CurrentMonthDuration").append("\n");

            for (TrainerWorkload t : trainers) {
                if (t == null) continue;
                String firstName = t.getFirstName() != null ? t.getFirstName() : "";
                String lastName = t.getLastName() != null ? t.getLastName() : "";

                int monthDuration = extractMonthDuration(t, currentYear, currentMonth);

                // Apply the include rules: include active trainers always; include inactive only if duration > 0
                boolean include = t.isActive() || (!t.isActive() && monthDuration > 0);

                if (include) {
                    csv.append(escapeCsv(firstName)).append(",")
                            .append(escapeCsv(lastName)).append(",")
                            .append(monthDuration).append("\n");
                }
            }

            byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);

            // Upload to S3
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(s3Key)
                    .contentType("text/csv")
                    .contentLength((long) bytes.length)
                    .build();

            s3.putObject(putReq, RequestBody.fromBytes(bytes));

            String result = String.format("Uploaded %d bytes to s3://%s/%s", bytes.length, BUCKET_NAME, s3Key);
            logger.log(result + "\n");
            return result;

        } catch (DynamoDbException e) {
            context.getLogger().log("DynamoDB error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            context.getLogger().log("Unhandled exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private int extractMonthDuration(TrainerWorkload t, Year currentYear, Month currentMonth) {
        if (t.getYears() == null) return 0;
        for (YearSummary ys : t.getYears()) {
            if (ys == null || ys.getYear() == null) continue;
            if (ys.getYear().equals(currentYear)) {
                if (ys.getMonths() == null) return 0;
                for (MonthSummary ms : ys.getMonths()) {
                    if (ms == null || ms.getMonth() == null) continue;
                    if (ms.getMonth().equals(currentMonth)) {
                        return (ms.getTotalDuration() != null) ? ms.getTotalDuration() : 0;
                    }
                }
            }
        }
        return 0;
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        String result = s;
        if (result.contains(",") || result.contains("\n") || result.contains("\r") || result.contains("\"")) {
            result = result.replace("\"", "\"\"");
            result = "\"" + result + "\"";
        }
        return result;
    }
}


