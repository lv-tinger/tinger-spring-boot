package org.tinger.jdbc.scan;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * Created by tinger on 2022-10-21
 */
@Component
@Import(JdbcRepositoryRegister.class)
public class JdbcRepositoryScannerImporter {
}