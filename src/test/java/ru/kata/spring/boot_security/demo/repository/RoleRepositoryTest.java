package ru.kata.spring.boot_security.demo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kata.spring.boot_security.demo.BaseIT;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleRepositoryTest extends BaseIT {

    @Test
    @DisplayName("when findAllByNameIn called then success")
    public void testFindAllByNameIn() {
        var roles = List.of(new Role(1, "ADMIN"), new Role(2, "USER"));

        roleRepository.saveAll(roles);

        var result = roleRepository.findAllByNameIn(List.of("ADMIN","USER"));

        assertEquals(roles.size(), result.size());
    }

    @Test
    @DisplayName("when findAllByNameIn called then not found")
    public void testFindAllByNameInEmptyList() {

        var result = roleRepository.findAllByNameIn(Collections.emptyList());

        assertTrue(result.isEmpty());
    }

}
