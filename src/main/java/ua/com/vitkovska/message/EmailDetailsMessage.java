package ua.com.vitkovska.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


/**
 * Represents a message containing email details.
 * This class is used for transferring email information.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class EmailDetailsMessage {
    private String subject;
    private String content;
}
