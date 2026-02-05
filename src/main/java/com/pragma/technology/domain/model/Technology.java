package com.pragma.technology.domain.model;

import com.pragma.technology.domain.exception.TechnologyDuplicateException;
import com.pragma.technology.domain.exception.TechnologyInvalidException;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;
import java.util.function.Predicate;

@Value
@Builder
public class Technology {
    String id;
    String name;
    String description;

    private final static int MAX_NAME_LENGTH = 50;

    private final static int MAX_DESCRIPTION_LENGTH = 90;

    private static final Predicate<String> NAME_VALID =
            name -> name != null && !name.isBlank() && name.length() <= MAX_NAME_LENGTH;

    private static final Predicate<String> DESCRIPTION_VALID =
            description -> description != null && !description.isBlank() && description.length() <= MAX_DESCRIPTION_LENGTH;

    public static Technology create (String name, String description) {
        validateName(name);
        validateDescription(description);

        return Technology.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .description(description)
                .build();
    }

    public static Technology reconstructor(String id, String name, String description) {
        return Technology.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public  Technology update (String name, String description) {
        validateName(name);
        validateDescription(description);

        return Technology.builder()
                .id(this.id)
                .name(name)
                .description(description)
                .build();
    }

    private static void validateName(String name) {
        if (!NAME_VALID.test(name)) {
            throw new TechnologyDuplicateException(
                    "El nombre es obligatorio y debe tener máximo " + MAX_NAME_LENGTH + " caracteres"
            );
        }
    }

    private static void validateDescription( String description) {
        if (!DESCRIPTION_VALID.test(description)) {
            throw new TechnologyInvalidException(
                    "La descripción es obligatoria y debe tener máximo " + MAX_DESCRIPTION_LENGTH + " caracteres"
            );
        }
    }

}
