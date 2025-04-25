package de.uniwue.jpp.mineswueper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RevealFieldsResult {
    private Collection<Field> fields;
    Collection<Field> fieldCollection;
    public enum RevealFieldState {
        FIELD_NOT_REVEALED, FIELDS_REVEALED, FOUND_MINE
    }

    public RevealFieldsResult() {
        this(new ArrayList<Field>());
    }

    public RevealFieldsResult(Collection<Field> fields) {
        this.fields = fields;
    }

    public Collection<Field> getRevealedFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public RevealFieldState getState() {
          if(fields.isEmpty()) {return RevealFieldState.FIELD_NOT_REVEALED;
          }
          else if(fields.stream().anyMatch(Field::hasMine)) {return RevealFieldState.FOUND_MINE;}
          else {return RevealFieldState.FIELDS_REVEALED;}
    }
}
