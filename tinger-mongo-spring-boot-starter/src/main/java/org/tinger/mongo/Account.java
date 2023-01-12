package org.tinger.mongo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String username;
    private String password;
}
