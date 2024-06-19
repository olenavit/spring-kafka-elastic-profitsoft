package ua.com.vitkovska.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;


/**
 * Represents an email data.
 * This class is annotated to be used as a document in Elasticsearch.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "team_email")
public class EmailData {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String recipient;

    @Field(type = FieldType.Text)
    private EmailStatus status;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Integer)
    private int attemptCount;

    @Field(type = FieldType.Date,format = DateFormat.date_time)
    private Instant lastAttemptTime;
}
