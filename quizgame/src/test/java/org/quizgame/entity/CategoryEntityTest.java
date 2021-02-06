package org.quizgame.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryEntityTest extends EntityTestBase{
    @Test
    public void testTooLongName() {
        String name = "123456789112345678911234567891123456789112345678911234567891123456789112345678911234567891123456789112345678911234567891123456789112345678911234567891";
        Category cat = createCategory(name);

        assertFalse(persistInATransaction(cat));

        Category cat2 = createCategory("JA");

        assertTrue(persistInATransaction(cat2));
    }

    @Test
    public void testUniqueName() {
        Category cat = createCategory("yes");
        assertTrue(persistInATransaction(cat));

        Category cat2 = createCategory("yes");
        assertFalse(persistInATransaction(cat2));
    }
}
