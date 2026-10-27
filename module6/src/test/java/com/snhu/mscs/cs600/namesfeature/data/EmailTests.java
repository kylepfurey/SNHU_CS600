// .java
// Email Generation Tests
// by Kyle Furey

package com.snhu.mscs.cs600.namesfeature.data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class EmailTests {
    Name newName(String first, String last) {
        Name name = new Name();
        name.setFirst(first);
        name.setLast(last);
        return name;
    }

    @Test
    void correctName() {
        Assertions.assertEquals("jane.doe@example.com", newName("Jane", "Doe").getEmail());
    }

    // BAD INPUT

    @Test
    void capitalsAndWhitespace() {
        Assertions.assertEquals("mary.smith@example.com", newName(" MARY ", "Smith ").getEmail());
    }

    @Test
    void badApostrophe() {
        Assertions.assertEquals("sean.oconnor@example.com", newName("Sean", "O'Connor").getEmail());
    }

    @Test
    void okHyphen() {
        Assertions.assertEquals("ana.lopez-diaz@example.com", newName("Ana", "Lopez-Diaz").getEmail());
    }

    // EMPTY

    @Test
    void allEmpty() {
        Assertions.assertEquals("", newName("", "").getEmail());
    }

    @Test
    void emptyFirst() {
        Assertions.assertEquals("", newName("", "Doe").getEmail());
    }

    @Test
    void emptyLast() {
        Assertions.assertEquals("", newName("Jane", "").getEmail());
    }

    // NULL

    @Test
    void allNull() {
        Assertions.assertEquals("", newName(null, null).getEmail());
    }

    @Test
    void nullFirst() {
        Assertions.assertEquals("", newName(null, "Doe").getEmail());
    }

    @Test
    void nullLast() {
        Assertions.assertEquals("", newName("Jane", null).getEmail());
    }
}
